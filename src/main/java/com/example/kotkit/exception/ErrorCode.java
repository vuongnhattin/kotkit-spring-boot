package com.example.kotkit.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Video not found");

    ErrorCode(int status, String code) {
        this.code = code;
        this.status = status;
    }
    private final int status;
    private final String code;

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
