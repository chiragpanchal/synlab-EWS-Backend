package com.ewsv3.ews.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class UserPrincipal implements UserDetails {
    
    private Long userId;
    private String username;
    private String password;
    private String userType;
    private Long enterpriseId;
    private Long employeeId;
    private Date startDate;
    private Date endDate;
    
    public UserPrincipal(User user) {
        this.userId = user.getUserId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.userType = user.getUserType();
        this.enterpriseId = user.getEnterpriseId();
        this.employeeId = user.getEmployeeId();
        this.startDate = user.getStartDate();
        this.endDate = user.getEndDate();
    }
    
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can map userType to roles/authorities here
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType));
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        // Check if account is not expired based on END_DATE
        return endDate == null || endDate.after(new Date());
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement locking logic if needed
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement credential expiration logic if needed
    }
    
    @Override
    public boolean isEnabled() {
        // Check if user is enabled based on START_DATE and END_DATE
        Date now = new Date();
        return (startDate == null || startDate.before(now) || startDate.equals(now)) &&
               (endDate == null || endDate.after(now));
    }
    
    // Getters for additional user information
    public Long getUserId() {
        return userId;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
}
