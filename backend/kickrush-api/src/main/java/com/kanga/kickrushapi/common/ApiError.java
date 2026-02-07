package com.kanga.kickrushapi.common;

import java.time.LocalDateTime;

public record ApiError(String code, String message, String timestamp) {
    public static ApiError of(ErrorCode code, String message) {
        return new ApiError(code.name(), message, LocalDateTime.now().toString());
    }
}
