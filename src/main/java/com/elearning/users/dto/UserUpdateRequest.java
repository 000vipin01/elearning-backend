package com.elearning.users.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 2, max = 100) String name,
    String avatarUrl,
    String bio
) {}
