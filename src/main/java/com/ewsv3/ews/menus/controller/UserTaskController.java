package com.ewsv3.ews.menus.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.menus.model.UserTask;
import com.ewsv3.ews.menus.service.UserTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserTaskController {
    private static final Logger logger = LoggerFactory.getLogger(UserTaskController.class);

    @Autowired
    private UserTaskService userTaskService;

    /**
     * Get all accessible tasks for the authenticated user
     */
    @GetMapping("/user-tasks")
    public ResponseEntity<?> getUserTasks() {
        logger.info("GET_USER_TASKS - Entry - Time: {}", LocalDateTime.now());
        try {
            Long userId = getCurrentUserId();
            List<UserTask> userTasks = userTaskService.getUserTasks(userId);
            logger.info("GET_USER_TASKS - Exit - Time: {}, UserId: {}, Response Count: {}", LocalDateTime.now(), userId, userTasks.size());
            return ResponseEntity.ok(userTasks);
        } catch (Exception e) {
            logger.error("GET_USER_TASKS - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error fetching user tasks: " + e.getMessage()));
        }
    }

    

    /**
     * Get current user information from token
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        logger.info("GET_CURRENT_USER - Entry - Time: {}", LocalDateTime.now());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                Map<String, Object> userInfo = Map.of(
                    "userId", userPrincipal.getUserId(),
                    "username", userPrincipal.getUsername(),
                    "userType", userPrincipal.getUserType(),
                    "enterpriseId", userPrincipal.getEnterpriseId(),
                    "employeeId", userPrincipal.getEmployeeId()
                );
                logger.info("GET_CURRENT_USER - Exit - Time: {}, Response: {}", LocalDateTime.now(), userInfo);
                return ResponseEntity.ok(userInfo);
            }
            logger.info("GET_CURRENT_USER - Exit - Time: {}, Response: User not authenticated", LocalDateTime.now());
            return ResponseEntity.badRequest().body(new ErrorResponse("User not authenticated"));
        } catch (Exception e) {
            logger.error("GET_CURRENT_USER - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error fetching current user: " + e.getMessage()));
        }
    }

    /**
     * Simple test endpoint to verify authentication
     */
    @GetMapping("/test")
    public ResponseEntity<?> testMenusEndpoint() {
        logger.info("TEST_MENUS_ENDPOINT - Entry - Time: {}", LocalDateTime.now());
        try {
            Long userId = getCurrentUserId();
            Map<String, Object> response = Map.of(
                "message", "Menus endpoint is working!",
                "authenticatedUserId", userId,
                "timestamp", System.currentTimeMillis()
            );
            logger.info("TEST_MENUS_ENDPOINT - Exit - Time: {}, UserId: {}, Response: {}", LocalDateTime.now(), userId, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("TEST_MENUS_ENDPOINT - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error in test endpoint: " + e.getMessage()));
        }
    }

    /**
     * Debug endpoint to check authentication status
     */
    @GetMapping("/debug")
    public ResponseEntity<?> debugAuthentication() {
        logger.info("DEBUG_AUTHENTICATION - Entry - Time: {}", LocalDateTime.now());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Map<String, Object> debugInfo = new java.util.HashMap<>();
            debugInfo.put("authenticated", authentication != null && authentication.isAuthenticated());
            debugInfo.put("principal", authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "null");
            debugInfo.put("authorities", authentication != null ? authentication.getAuthorities().toString() : "null");

            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                debugInfo.put("userId", userPrincipal.getUserId());
                debugInfo.put("username", userPrincipal.getUsername());
            } else {
                debugInfo.put("principalType", authentication != null ? authentication.getPrincipal().toString() : "null");
            }

            logger.info("DEBUG_AUTHENTICATION - Exit - Time: {}, Response: {}", LocalDateTime.now(), debugInfo);
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            logger.error("DEBUG_AUTHENTICATION - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Debug error: " + e.getMessage()));
        }
    }

    /**
     * Debug endpoint to see raw data before duplicate removal
     */
    @GetMapping("/user-tasks/raw")
    public ResponseEntity<?> getUserTasksRaw() {
        logger.info("GET_USER_TASKS_RAW - Entry - Time: {}", LocalDateTime.now());
        try {
            Long userId = getCurrentUserId();
            List<UserTask> rawTasks = userTaskService.getUserTasksRaw(userId);
            Map<String, Object> response = Map.of(
                "totalCount", rawTasks.size(),
                "tasks", rawTasks
            );
            logger.info("GET_USER_TASKS_RAW - Exit - Time: {}, UserId: {}, Total Count: {}", LocalDateTime.now(), userId, rawTasks.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_USER_TASKS_RAW - Exception - Time: {}, Error: {}", LocalDateTime.now(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Error fetching raw user tasks: " + e.getMessage()));
        }
    }

    /**
     * Helper method to extract user ID from JWT token
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }





    /**
     * Error response class
     */
    public static class ErrorResponse {
        private String error;
        private long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
