package com.ewsv3.ews.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request body for the Oracle Fusion SSO login endpoint (/api/auth/login-jwt).
 * Carries the JWT that Oracle Fusion appends to the launch URL as "?jwt=...",
 * without the "jwt=" prefix.
 */
public class JwtLoginRequest {

    @JsonProperty("jwt")
    private String jwt;

    // Default constructor
    public JwtLoginRequest() {}

    // Constructor with parameters
    public JwtLoginRequest(String jwt) {
        this.jwt = jwt;
    }

    // Getters and Setters
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
