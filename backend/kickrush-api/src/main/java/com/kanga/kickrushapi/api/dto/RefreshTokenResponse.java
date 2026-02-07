package com.kanga.kickrushapi.api.dto;

public record RefreshTokenResponse(String accessToken, int expiresIn, String tokenType) {
}
