package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.DownloadImageResponse;
import org.example.expert.domain.user.dto.response.UploadImageResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    // 프로필 이미지 업로드
    @PostMapping("/users/profile")
    public ResponseEntity<UploadImageResponse> uploadImage(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam("image") MultipartFile image) {
        String key = userService.uploadImage(authUser.getId(), image);
        return ResponseEntity.ok(new UploadImageResponse(key));
    }

    // 프로필 이미지 다운로드
    @GetMapping("/users/profile")
    public ResponseEntity<DownloadImageResponse> downloadImage(
            @AuthenticationPrincipal AuthUser authUser) {
        URL url = userService.downloadImage(authUser.getId());
        return ResponseEntity.ok(new DownloadImageResponse(url.toString()));
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/users/profile")
    public ResponseEntity<Void> deleteImage(@AuthenticationPrincipal AuthUser authUser) {
        userService.deleteImage(authUser.getId());
        return ResponseEntity.noContent().build();
    }
}
