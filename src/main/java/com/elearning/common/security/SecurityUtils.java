package com.elearning.common.security;

import com.elearning.common.error.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new UnauthorizedException("Not authenticated");
        }
        return user;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().id();
    }

    public static String getCurrentUserRole() {
        return getCurrentUser().role();
    }
}
