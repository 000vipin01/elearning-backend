package com.elearning.common.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int maxAttempts;
    private final long windowSeconds;

    public RateLimiter() {
        this(5, 60);
    }

    public RateLimiter(int maxAttempts, long windowSeconds) {
        this.maxAttempts = maxAttempts;
        this.windowSeconds = windowSeconds;
    }

    public synchronized boolean allowRequest(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket());
        return bucket.tryConsume(maxAttempts, windowSeconds);
    }

    private static class Bucket {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile Instant windowStart = Instant.now();

        synchronized boolean tryConsume(int maxAttempts, long windowSeconds) {
            Instant now = Instant.now();
            if (now.isAfter(windowStart.plusSeconds(windowSeconds))) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= maxAttempts;
        }
    }
}
