package com.ewsv3.ews.auth.controller;

import com.ewsv3.ews.auth.dto.*;
import com.ewsv3.ews.auth.service.JwtService;
import com.ewsv3.ews.auth.service.RefreshTokenService;
import com.ewsv3.ews.auth.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    com.ewsv3.ews.auth.repository.UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<?> testAuthEndpoint() {
        return ResponseEntity.ok(new MessageResponse("Auth endpoint is working!"));
    }

    @GetMapping("/users")
    public ResponseEntity<?> listUsers() {
        // This is just for testing - remove in production
        java.util.List<com.ewsv3.ews.auth.dto.User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().limit(5).map(user -> {
            java.util.Map<String, Object> userInfo = new java.util.HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("userName", user.getUserName());
            userInfo.put("userType", user.getUserType());
            return userInfo;
        }).collect(java.util.stream.Collectors.toList()));
    }

    @GetMapping("/test-users")
    public ResponseEntity<?> listAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> {
            java.util.Map<String, Object> userInfo = new java.util.HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("userName", user.getUserName());
            userInfo.put("userType", user.getUserType());
            return userInfo;
        }).collect(java.util.stream.Collectors.toList()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getUserType(),
                userDetails.getEnterpriseId(),
                userDetails.getEmployeeId()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateTokenFromUsername(user.getUserName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Date expirationDate = jwtService.getExpirationDateFromJwtToken(jwt);
            long expirationTimeMillis = expirationDate.getTime() - System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(jwt, expirationTimeMillis);
        }

        // The refresh token should be removed from the client side.
        // Optionally, you can invalidate the refresh token on the server side as well.
        // For example, by getting it from the request and deleting it from the database.

        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/test-menus")
    public ResponseEntity<?> testMenusEndpoint() {
        return ResponseEntity.ok(new MessageResponse("Menus endpoint test is working!"));
    }

    @GetMapping("/test-token")
    public ResponseEntity<?> testToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Token is valid!");
            response.put("userId", userPrincipal.getUserId());
            response.put("username", userPrincipal.getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
    }

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
