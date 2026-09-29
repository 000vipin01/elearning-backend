package com.elearning.auth.controller;

import com.elearning.auth.dto.*;
import com.elearning.auth.service.AuthService;
import com.elearning.common.security.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RateLimiter rateLimiter;

    public AuthController(AuthService authService, RateLimiter rateLimiter) {
        this.authService = authService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String key = "login:" + httpRequest.getRemoteAddr();
        if (!rateLimiter.allowRequest(key)) {
            return ResponseEntity.status(429).build();
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request, HttpServletRequest httpRequest) {
        String key = "signup:" + httpRequest.getRemoteAddr();
        if (!rateLimiter.allowRequest(key)) {
            return ResponseEntity.status(429).build();
        }
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email"));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Map<String, String>> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirm request) {
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }
}
