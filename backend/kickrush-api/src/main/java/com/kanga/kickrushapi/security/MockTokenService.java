package com.kanga.kickrushapi.security;

import com.kanga.kickrushapi.mock.MemberRole;

import java.util.Map;

public class MockTokenService {
    private static final String USER_TOKEN = "mock-user-token";
    private static final String ADMIN_TOKEN = "mock-admin-token";

    private static final Map<String, AuthUser> TOKENS = Map.of(
            USER_TOKEN, new AuthUser(1L, MemberRole.USER),
            ADMIN_TOKEN, new AuthUser(2L, MemberRole.ADMIN)
    );

    public String issueToken(MemberRole role) {
        return role == MemberRole.ADMIN ? ADMIN_TOKEN : USER_TOKEN;
    }

    public AuthUser parseToken(String token) {
        return TOKENS.get(token);
    }
}
