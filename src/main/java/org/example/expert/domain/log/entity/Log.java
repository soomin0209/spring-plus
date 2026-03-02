package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long todoId;
    private Long userId;
    private Long managerUserId;
    private boolean isSuccess;
    private String message;
    private LocalDateTime createdAt;

    public Log(Long todoId, Long userId, Long managerUserId, boolean isSuccess, String message) {
        this.todoId = todoId;
        this.userId = userId;
        this.managerUserId = managerUserId;
        this.isSuccess = isSuccess;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
