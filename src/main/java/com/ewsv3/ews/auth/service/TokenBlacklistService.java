package com.ewsv3.ews.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long expirationTimeMillis) {
        try {
            String key = BLACKLIST_KEY_PREFIX + token;
            redisTemplate.opsForValue().set(key, "blacklisted", expirationTimeMillis, TimeUnit.MILLISECONDS);
            logger.debug("Token blacklisted successfully: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
        } catch (Exception e) {
            logger.warn("Failed to blacklist token (Redis unavailable): {}", e.getMessage());
            // In production, you might want to use an alternative storage mechanism
        }
    }

    public boolean isTokenBlacklisted(String token) {
        try {
            String key = BLACKLIST_KEY_PREFIX + token;
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.warn("Failed to check token blacklist (Redis unavailable): {}", e.getMessage());
            // If Redis is unavailable, assume token is not blacklisted
            // In production, you might want to use an alternative storage mechanism
            return false;
        }
    }
}
