package com.ewsv3.ews.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TokenBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";

    // Circuit breaker: skip Redis entirely once it's confirmed unavailable.
    // Resets to false if blacklistToken succeeds (Redis comes back up).
    private final AtomicBoolean redisUnavailable = new AtomicBoolean(false);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long expirationTimeMillis) {
        try {
            String key = BLACKLIST_KEY_PREFIX + token;
            redisTemplate.opsForValue().set(key, "blacklisted", expirationTimeMillis, TimeUnit.MILLISECONDS);
            redisUnavailable.set(false);
            logger.debug("Token blacklisted successfully: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
        } catch (Exception e) {
            redisUnavailable.set(true);
            logger.warn("Failed to blacklist token (Redis unavailable): {}", e.getMessage());
        }
    }

    public boolean isTokenBlacklisted(String token) {
        if (redisUnavailable.get()) {
            return false;
        }
        try {
            String key = BLACKLIST_KEY_PREFIX + token;
            boolean result = Boolean.TRUE.equals(redisTemplate.hasKey(key));
            redisUnavailable.set(false);
            return result;
        } catch (Exception e) {
            redisUnavailable.set(true);
            logger.warn("Failed to check token blacklist (Redis unavailable): {}", e.getMessage());
            return false;
        }
    }
}
