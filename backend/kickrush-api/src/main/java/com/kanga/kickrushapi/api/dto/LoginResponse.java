package com.kanga.kickrushapi.api.dto;

public record LoginResponse(String accessToken, String refreshToken, int expiresIn, String tokenType) {
}
