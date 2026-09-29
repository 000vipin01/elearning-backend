package com.elearning.auth.service;

import com.elearning.auth.dto.*;
import com.elearning.common.error.BadRequestException;
import com.elearning.common.error.ConflictException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.error.UnauthorizedException;
import com.elearning.common.security.JwtService;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(User.Role.STUDENT);
        user.setEmailVerified(false);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public void requestPasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // Log reset link to console when no email provider is configured
        String resetLink = "/reset-password?token=" + token;
        log.info("Password reset link for {}: {}", user.getEmail(), resetLink);
    }

    @Transactional
    public void confirmPasswordReset(PasswordResetConfirm request) {
        User user = userRepository.findByPasswordResetToken(request.token())
            .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        if (user.getPasswordResetExpiresAt() == null || user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        userRepository.save(user);
    }
}
