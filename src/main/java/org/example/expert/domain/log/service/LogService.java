package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    // 매니저 등록 실패해도 독립 트랜잭션으로 로그 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Long todoId, Long userId, Long managerUserId, boolean isSuccess, String message) {
        Log log = new Log(todoId, userId, managerUserId, isSuccess, message);
        logRepository.save(log);
    }
}
