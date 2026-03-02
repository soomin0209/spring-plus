package org.example.expert.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String status;
    private int code;
    private String message;
}