package com.kanga.kickrushapi.security;

import com.kanga.kickrushapi.common.ApiError;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    private final MockTokenService tokenService = new MockTokenService();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            if (requiresAuth(path, request.getMethod())) {
                String token = resolveToken(request);
                if (token == null) {
                    writeError(response, HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
                    return;
                }
                AuthUser user = tokenService.parseToken(token);
                if (user == null) {
                    writeError(response, HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
                    return;
                }
                if (path.startsWith("/api/admin") && user.role() != MemberRole.ADMIN) {
                    writeError(response, HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, "권한이 없습니다.");
                    return;
                }
                AuthContext.set(user);
            }
            filterChain.doFilter(request, response);
        } finally {
            AuthContext.clear();
        }
    }

    private boolean requiresAuth(String path, String method) {
        if (path.startsWith("/api/my")) {
            return true;
        }
        if (path.startsWith("/api/orders") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        return path.startsWith("/api/admin");
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring("Bearer ".length());
    }

    private void writeError(HttpServletResponse response, HttpStatus status, ErrorCode code, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        ApiError error = ApiError.of(code, message);
        response.getWriter().write("{\"code\":\"" + error.code() + "\",\"message\":\"" + error.message() +
                "\",\"timestamp\":\"" + error.timestamp() + "\"}");
    }
}
