package com.ewsv3.ews.auth.dto;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sc_users")
public class User {
    
    @Id
    @Column(name = "USER_ID")
    private Long userId;
    
    @Column(name = "USER_NAME", length = 1000)
    private String userName;
    
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;
    
    @Column(name = "USER_TYPE", length = 1000)
    private String userType;
    
    @Column(name = "ENTERPRISE_ID")
    private Long enterpriseId;
    
    @Column(name = "PASSWORD", length = 1000)
    private String password;

    @Column(name = "ROLE", length = 100)
    private String role;
    
    // Default constructor
    public User() {}
    
    // Constructor with all fields
    public User(Long userId, String userName, Date startDate, Date endDate, 
                Long employeeId, String userType, Long enterpriseId, String password, String role) {
        this.userId = userId;
        this.userName = userName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeId = employeeId;
        this.userType = userType;
        this.enterpriseId = enterpriseId;
        this.password = password;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
