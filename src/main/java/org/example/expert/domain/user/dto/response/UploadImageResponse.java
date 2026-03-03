package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UploadImageResponse {

    private final String key;

    public UploadImageResponse(String key) {
        this.key = key;
    }
}
