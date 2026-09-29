package com.elearning.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RoleUpdateRequest(
    @NotBlank @Pattern(regexp = "STUDENT|INSTRUCTOR|ADMIN") String role
) {}
