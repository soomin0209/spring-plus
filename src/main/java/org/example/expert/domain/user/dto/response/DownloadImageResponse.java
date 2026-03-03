package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class DownloadImageResponse {

    private final String url;

    public DownloadImageResponse(String url) {
        this.url = url;
    }
}
