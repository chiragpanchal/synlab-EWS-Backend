package com.ewsv3.ews.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("type")
    private String type = "Bearer";

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("userType")
    private String userType;

    @JsonProperty("enterpriseId")
    private Long enterpriseId;

    @JsonProperty("employeeId")
    private Long employeeId;

    // Default constructor
    public JwtResponse() {}

    // Constructor with token and user details
    public JwtResponse(String accessToken, String refreshToken, Long userId, String username, String userType,
                      Long enterpriseId, Long employeeId) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.userType = userType;
        this.enterpriseId = enterpriseId;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
