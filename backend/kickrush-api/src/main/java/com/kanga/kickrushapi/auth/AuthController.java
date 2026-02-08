package com.kanga.kickrushapi.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kanga.kickrushapi.auth.dto.LoginRequest;
import com.kanga.kickrushapi.auth.dto.LoginResponse;
import com.kanga.kickrushapi.auth.dto.RefreshTokenRequest;
import com.kanga.kickrushapi.auth.dto.RefreshTokenResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }
}
