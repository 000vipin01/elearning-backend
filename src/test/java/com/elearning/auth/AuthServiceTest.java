package com.elearning.auth;

import com.elearning.auth.dto.AuthResponse;
import com.elearning.auth.dto.LoginRequest;
import com.elearning.auth.dto.SignupRequest;
import com.elearning.auth.service.AuthService;
import com.elearning.common.error.UnauthorizedException;
import com.elearning.common.security.JwtService;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_success() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encoded_password");
        user.setRole(User.Role.STUDENT);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);
        when(jwtService.generateToken(1L, "test@example.com")).thenReturn("mock-jwt-token");

        AuthResponse response = authService.login(new LoginRequest("test@example.com", "password"));

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.token());
        assertEquals("test@example.com", response.email());
        assertEquals("STUDENT", response.role());
    }

    @Test
    void login_invalidCredentials() {
        when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () ->
            authService.login(new LoginRequest("wrong@example.com", "password")));
    }

    @Test
    void signup_createsStudent() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(1L, "new@example.com")).thenReturn("mock-jwt-token");

        AuthResponse response = authService.signup(new SignupRequest("New User", "new@example.com", "password123"));

        assertNotNull(response);
        assertEquals("STUDENT", response.role());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_duplicateEmail() {
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(Exception.class, () ->
            authService.signup(new SignupRequest("User", "existing@example.com", "password123")));
    }
}
