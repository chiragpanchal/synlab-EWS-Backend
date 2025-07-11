package com.ewsv3.ews.auth.config;

import com.ewsv3.ews.auth.service.CustomUserDetailsService;
import com.ewsv3.ews.auth.service.JwtService;
import com.ewsv3.ews.auth.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Skip JWT processing for public auth endpoints only
        String path = request.getRequestURI();
        if (path.equals("/api/auth/login") || 
            path.equals("/api/auth/test") || 
            path.equals("/api/auth/users") || 
            path.equals("/api/auth/test-users") || 
            path.equals("/api/auth/test-menus") || 
            path.equals("/api/auth/refreshtoken")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String jwt = parseJwt(request);
            logger.info("JWT Filter - Path: {}, JWT present: {}", path, jwt != null);
            
            if (jwt != null) {
                logger.info("JWT Filter - Token: {}", jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
                boolean isValid = jwtService.validateJwtToken(jwt);
                boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(jwt);
                logger.info("JWT Filter - Token valid: {}, Blacklisted: {}", isValid, isBlacklisted);
                
                if (isValid && !isBlacklisted) {
                    String username = jwtService.getUserNameFromJwtToken(jwt);
                    logger.info("JWT Filter - Username from token: {}", username);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("JWT Filter - Authentication set for user: {}", username);
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
