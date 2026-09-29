package com.elearning.media;

import com.elearning.media.security.StreamTokenService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamTokenServiceTest {

    private final StreamTokenService tokenService = new StreamTokenService(
        "testSecretKeyForElearningAppThatIsLongEnoughForHS256Algorithm1234567890", 300);

    @Test
    void generateAndValidateToken() {
        String token = tokenService.generateToken(1L, 100L);
        assertNotNull(token);
        assertTrue(tokenService.validateToken(token, 1L, 100L));
    }

    @Test
    void validateToken_wrongUser() {
        String token = tokenService.generateToken(1L, 100L);
        assertFalse(tokenService.validateToken(token, 2L, 100L));
    }

    @Test
    void validateToken_wrongLesson() {
        String token = tokenService.generateToken(1L, 100L);
        assertFalse(tokenService.validateToken(token, 1L, 200L));
    }

    @Test
    void validateToken_forgedToken() {
        String token = tokenService.generateToken(1L, 100L);
        String forged = token.substring(0, token.length() - 5) + "XXXXX";
        assertFalse(tokenService.validateToken(forged, 1L, 100L));
    }

    @Test
    void validateToken_expiredToken() throws InterruptedException {
        StreamTokenService shortLived = new StreamTokenService(
            "testSecretKeyForElearningAppThatIsLongEnoughForHS256Algorithm1234567890", 1);
        String token = shortLived.generateToken(1L, 100L);
        Thread.sleep(1100);
        assertFalse(shortLived.validateToken(token, 1L, 100L));
    }

    @Test
    void validateToken_malformedToken() {
        assertFalse(tokenService.validateToken("not-a-valid-token", 1L, 100L));
        assertFalse(tokenService.validateToken("", 1L, 100L));
        assertFalse(tokenService.validateToken(null, 1L, 100L));
    }
}
