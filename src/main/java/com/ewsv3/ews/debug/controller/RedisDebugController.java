package com.ewsv3.ews.debug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/debug/redis")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RedisDebugController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Get all Redis keys
     */
    @GetMapping("/keys")
    public ResponseEntity<?> getAllKeys() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            return ResponseEntity.ok(Map.of(
                "keys", keys,
                "count", keys != null ? keys.size() : 0
            ));
        } catch (Exception e) {
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
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
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
        try {
            String value = redisTemplate.opsForValue().get(keyName);
            Boolean exists = redisTemplate.hasKey(keyName);
            Long ttl = redisTemplate.getExpire(keyName);
            
            return ResponseEntity.ok(Map.of(
                "key", keyName,
                "value", value != null ? value : "null",
                "exists", exists,
                "ttl", ttl + " seconds"
            ));
        } catch (Exception e) {
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
        try {
            Set<String> keys = redisTemplate.keys("jwt:blacklist:*");
            int deletedCount = 0;
            if (keys != null && !keys.isEmpty()) {
                deletedCount = redisTemplate.delete(keys).intValue();
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "JWT blacklist cleared",
                "deletedKeys", deletedCount
            ));
        } catch (Exception e) {
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
        try {
            // Try to execute a simple operation
            redisTemplate.opsForValue().set("test:ping", "pong");
            String value = redisTemplate.opsForValue().get("test:ping");
            redisTemplate.delete("test:ping");
            
            return ResponseEntity.ok(Map.of(
                "status", "connected",
                "ping", "pong",
                "testValue", value
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "status", "disconnected",
                "error", e.getMessage()
            ));
        }
    }
}
