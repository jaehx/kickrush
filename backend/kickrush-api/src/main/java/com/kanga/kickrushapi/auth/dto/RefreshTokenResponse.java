package com.kanga.kickrushapi.auth.dto;

public record RefreshTokenResponse(String accessToken, int expiresIn, String tokenType) {
}
