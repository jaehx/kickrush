package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.LoginRequest;
import com.kanga.kickrushapi.api.dto.LoginResponse;
import com.kanga.kickrushapi.api.dto.RefreshTokenRequest;
import com.kanga.kickrushapi.api.dto.RefreshTokenResponse;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.Member;
import com.kanga.kickrushapi.mock.MockStore;
import com.kanga.kickrushapi.security.MockTokenService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final int ACCESS_TOKEN_EXPIRES = 900;

    private final MockTokenService tokenService = new MockTokenService();

    public LoginResponse login(LoginRequest request) {
        Member member = MockStore.findMemberByEmail(request.email())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.INVALID_CREDENTIALS,
                        "이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!member.password().equals(request.password())) {
            throw MockStore.apiError(ErrorCode.INVALID_CREDENTIALS,
                    "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        String accessToken = tokenService.issueToken(member.role());
        String refreshToken = "mock-refresh-token";
        return new LoginResponse(accessToken, refreshToken, ACCESS_TOKEN_EXPIRES, "Bearer");
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw MockStore.apiError(ErrorCode.INVALID_PARAMETER, "리프레시 토큰이 필요합니다.");
        }
        String accessToken = tokenService.issueToken(MockStore.MEMBERS.get(0).role());
        return new RefreshTokenResponse(accessToken, ACCESS_TOKEN_EXPIRES, "Bearer");
    }
}
