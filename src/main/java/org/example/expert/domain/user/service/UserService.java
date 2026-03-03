package org.example.expert.domain.user.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Presigned URL의 유효기간: 7일
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    // 프로필 이미지 업로드
    @Transactional
    public String uploadImage(long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        // 기존 이미지 있으면 S3에서 먼저 삭제
        if (user.getProfileImageUrl() != null) {
            s3Template.deleteObject(bucket, user.getProfileImageUrl());
        }

        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + image.getOriginalFilename();
            s3Template.upload(bucket, key, image.getInputStream());
            // DB 저장
            user.saveProfileImageUrl(key);
            return key;
        } catch (IOException e) {
            throw new ServerException("프로필 이미지 업로드 실패");
        }
    }

    // 프로필 이미지 다운로드
    public URL downloadImage(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (user.getProfileImageUrl() == null) {
            throw new InvalidRequestException("프로필 이미지가 없습니다.");
        }

        return s3Template.createSignedGetURL(bucket, user.getProfileImageUrl(), PRESIGNED_URL_EXPIRATION);
    }

    // 프로필 이미지 삭제
    @Transactional
    public void deleteImage(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (user.getProfileImageUrl() == null) {
            throw new InvalidRequestException("프로필 이미지가 없습니다.");
        }

        s3Template.deleteObject(bucket, user.getProfileImageUrl());
        user.saveProfileImageUrl(null);
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }
}
