package com.ewsv3.ews.auth.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userPrincipal.getUserId());
        response.put("username", userPrincipal.getUsername());
        response.put("userType", userPrincipal.getUserType());
        response.put("enterpriseId", userPrincipal.getEnterpriseId());
        response.put("employeeId", userPrincipal.getEmployeeId());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test-protected")
    public ResponseEntity<?> testProtectedEndpoint() {
        return ResponseEntity.ok(new MessageResponse("You accessed a protected endpoint!"));
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
