package com.ewsv3.ews.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/debug/redis")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RedisDebugController {
    private static final Logger logger = LoggerFactory.getLogger(RedisDebugController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Get all Redis keys
     */
    @GetMapping("/keys")
    public ResponseEntity<?> getAllKeys() {
        logger.info("GET_ALL_REDIS_KEYS - Entry - Time: {}", LocalDateTime.now());
        try {
            Set<String> keys = redisTemplate.keys("*");
            Map<String, Object> response = Map.of(
                "keys", keys,
                "count", keys != null ? keys.size() : 0
            );
            logger.info("GET_ALL_REDIS_KEYS - Exit - Time: {}, Keys Count: {}", LocalDateTime.now(), keys != null ? keys.size() : 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_ALL_REDIS_KEYS - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "error", "Redis connection failed: " + e.getMessage(),
                "keys", new String[0],
                "count", 0
            ));
        }
    }

    /**
     * Get all JWT blacklist keys
     */
    @GetMapping("/jwt-blacklist")
    public ResponseEntity<?> getJwtBlacklistKeys() {
        logger.info("GET_JWT_BLACKLIST_KEYS - Entry - Time: {}", LocalDateTime.now());
        try {
            Set<String> keys = redisTemplate.keys("jwt:blacklist:*");
            Map<String, Object> result = new HashMap<>();
            result.put("blacklistKeys", keys);
            result.put("count", keys != null ? keys.size() : 0);

            // Get values for each key
            if (keys != null && !keys.isEmpty()) {
                Map<String, String> keyValues = new HashMap<>();
                for (String key : keys) {
                    try {
                        String value = redisTemplate.opsForValue().get(key);
                        keyValues.put(key, value);
                    } catch (Exception e) {
                        keyValues.put(key, "Error reading: " + e.getMessage());
                    }
                }
                result.put("keyValues", keyValues);
            }

            logger.info("GET_JWT_BLACKLIST_KEYS - Exit - Time: {}, Keys Count: {}", LocalDateTime.now(), keys != null ? keys.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("GET_JWT_BLACKLIST_KEYS - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "error", "Redis connection failed: " + e.getMessage(),
                "blacklistKeys", new String[0],
                "count", 0
            ));
        }
    }

    /**
     * Get value of a specific key
     */
    @GetMapping("/key/{keyName}")
    public ResponseEntity<?> getKeyValue(@PathVariable String keyName) {
        logger.info("GET_KEY_VALUE - Entry - Time: {}, KeyName: {}", LocalDateTime.now(), keyName);
        try {
            String value = redisTemplate.opsForValue().get(keyName);
            Boolean exists = redisTemplate.hasKey(keyName);
            Long ttl = redisTemplate.getExpire(keyName);

            Map<String, Object> response = Map.of(
                "key", keyName,
                "value", value != null ? value : "null",
                "exists", exists,
                "ttl", ttl + " seconds"
            );
            logger.info("GET_KEY_VALUE - Exit - Time: {}, KeyName: {}, Exists: {}", LocalDateTime.now(), keyName, exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_KEY_VALUE - Exception - Time: {}, KeyName: {}, Error: {}", LocalDateTime.now(), keyName, e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "error", "Redis connection failed: " + e.getMessage(),
                "key", keyName,
                "value", null,
                "exists", false
            ));
        }
    }

    /**
     * Clear all JWT blacklist keys
     */
    @DeleteMapping("/jwt-blacklist")
    public ResponseEntity<?> clearJwtBlacklist() {
        logger.info("CLEAR_JWT_BLACKLIST - Entry - Time: {}", LocalDateTime.now());
        try {
            Set<String> keys = redisTemplate.keys("jwt:blacklist:*");
            int deletedCount = 0;
            if (keys != null && !keys.isEmpty()) {
                deletedCount = redisTemplate.delete(keys).intValue();
            }

            Map<String, Object> response = Map.of(
                "message", "JWT blacklist cleared",
                "deletedKeys", deletedCount
            );
            logger.info("CLEAR_JWT_BLACKLIST - Exit - Time: {}, Deleted Keys Count: {}", LocalDateTime.now(), deletedCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("CLEAR_JWT_BLACKLIST - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "error", "Redis connection failed: " + e.getMessage(),
                "deletedKeys", 0
            ));
        }
    }

    /**
     * Redis connection test
     */
    @GetMapping("/ping")
    public ResponseEntity<?> pingRedis() {
        logger.info("PING_REDIS - Entry - Time: {}", LocalDateTime.now());
        try {
            // Try to execute a simple operation
            redisTemplate.opsForValue().set("test:ping", "pong");
            String value = redisTemplate.opsForValue().get("test:ping");
            redisTemplate.delete("test:ping");

            Map<String, Object> response = Map.of(
                "status", "connected",
                "ping", "pong",
                "testValue", value
            );
            logger.info("PING_REDIS - Exit - Time: {}, Status: connected", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("PING_REDIS - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "status", "disconnected",
                "error", e.getMessage()
            ));
        }
    }
}
