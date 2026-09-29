package com.elearning.users.dto;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    String role,
    Boolean emailVerified,
    String avatarUrl,
    String bio,
    LocalDateTime createdAt
) {}
