package com.ewsv3.ews.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomPasswordEncoder implements PasswordEncoder {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public CustomPasswordEncoder() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Check if the password is already BCrypt encoded (starts with $2a$, $2b$, or $2y$)
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
            return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        }
        
        // For backward compatibility, also check direct string match
        return rawPassword.toString().equals(encodedPassword);
    }
}
