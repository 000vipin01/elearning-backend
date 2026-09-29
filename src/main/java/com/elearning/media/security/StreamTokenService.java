package com.elearning.media.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class StreamTokenService {

    private final byte[] secret;
    private final long ttlSeconds;

    public StreamTokenService(@Value("${jwt.secret}") String secret,
                              @Value("${app.media.stream-token-ttl}") long ttlSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(Long userId, Long lessonId) {
        long expiry = System.currentTimeMillis() + (ttlSeconds * 1000);
        String payload = userId + ":" + lessonId + ":" + expiry;
        String signature = hmacSha256(payload);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes()) + "." + signature;
    }

    public boolean validateToken(String token, Long userId, Long lessonId) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) return false;

            String payload = new String(Base64.getUrlDecoder().decode(parts[0]));
            String[] payloadParts = payload.split(":");
            if (payloadParts.length != 3) return false;

            Long tokenUserId = Long.parseLong(payloadParts[0]);
            Long tokenLessonId = Long.parseLong(payloadParts[1]);
            long expiry = Long.parseLong(payloadParts[2]);

            if (!tokenUserId.equals(userId) || !tokenLessonId.equals(lessonId)) return false;
            if (System.currentTimeMillis() > expiry) return false;

            String expectedSignature = hmacSha256(payload);
            return expectedSignature.equals(parts[1]);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }
}
