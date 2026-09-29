package com.elearning.common.security;

public record AuthenticatedUser(Long id, String email, String role) {
}
