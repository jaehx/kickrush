package com.kanga.kickrushapi.security;

import com.kanga.kickrushapi.mock.MemberRole;

public record AuthUser(Long memberId, MemberRole role) {
}
