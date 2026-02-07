package com.kanga.kickrushapi.api;

import com.kanga.kickrushapi.api.dto.LoginRequest;
import com.kanga.kickrushapi.api.dto.LoginResponse;
import com.kanga.kickrushapi.api.dto.RefreshTokenRequest;
import com.kanga.kickrushapi.api.dto.RefreshTokenResponse;
import com.kanga.kickrushapi.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
