package com.ewsv3.ews.auth.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile() {
        logger.info("GET_USER_PROFILE - Entry - Time: {}", LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userPrincipal.getUserId());
        response.put("username", userPrincipal.getUsername());
        response.put("userType", userPrincipal.getUserType());
        response.put("enterpriseId", userPrincipal.getEnterpriseId());
        response.put("personId", userPrincipal.getEmployeeId());

        logger.info("GET_USER_PROFILE - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test-protected")
    public ResponseEntity<?> testProtectedEndpoint() {
        logger.info("TEST_PROTECTED_ENDPOINT - Entry - Time: {}", LocalDateTime.now());
        MessageResponse response = new MessageResponse("You accessed a protected endpoint!");
        logger.info("TEST_PROTECTED_ENDPOINT - Exit - Time: {}, Response: {}", LocalDateTime.now(), response.getMessage());
        return ResponseEntity.ok(response);
    }
    
    // Inner class for simple message responses
    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
