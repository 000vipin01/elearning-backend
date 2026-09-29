package com.elearning.common.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void generateAndParseToken() {
        String token = jwtService.generateToken(1L, "test@example.com");
        assertNotNull(token);

        Claims claims = jwtService.parseToken(token);
        assertEquals("1", claims.getSubject());
        assertEquals("test@example.com", claims.get("email"));
    }

    @Test
    void getUserId() {
        String token = jwtService.generateToken(42L, "user@example.com");
        assertEquals(42L, jwtService.getUserId(token));
    }

    @Test
    void getEmail() {
        String token = jwtService.generateToken(1L, "user@example.com");
        assertEquals("user@example.com", jwtService.getEmail(token));
    }
}
