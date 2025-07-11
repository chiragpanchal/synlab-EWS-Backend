package com.ewsv3.ews.menus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sc_user_tasks_v")
public class UserTask {
    
    @EmbeddedId
    private UserTaskId id;
    
    @Column(name = "ENTERPRISE_ID")
    private Long enterpriseId;
    
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;
    
    @Column(name = "TASK_NAME")
    private String taskName;
    
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    @Column(name = "GROUP_SEQ")
    private Long groupSeq;
    
    @Column(name = "GROUP_NAME")
    private String groupName;
    
    @Column(name = "CAN_CREATE")
    private String canCreate;
    
    @Column(name = "CAN_VIEW")
    private String canView;
    
    @Column(name = "CAN_EDIT")
    private String canEdit;
    
    @Column(name = "CAN_DELETE")
    private String canDelete;
    
    // Default constructor
    public UserTask() {}
    
    // Getters and setters
    public UserTaskId getId() {
        return id;
    }
    
    public void setId(UserTaskId id) {
        this.id = id;
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
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getTaskCode() {
        return taskCode;
    }
    
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
    
    public Long getGroupSeq() {
        return groupSeq;
    }
    
    public void setGroupSeq(Long groupSeq) {
        this.groupSeq = groupSeq;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getCanCreate() {
        return canCreate;
    }
    
    public void setCanCreate(String canCreate) {
        this.canCreate = canCreate;
    }
    
    public String getCanView() {
        return canView;
    }
    
    public void setCanView(String canView) {
        this.canView = canView;
    }
    
    public String getCanEdit() {
        return canEdit;
    }
    
    public void setCanEdit(String canEdit) {
        this.canEdit = canEdit;
    }
    
    public String getCanDelete() {
        return canDelete;
    }
    
    public void setCanDelete(String canDelete) {
        this.canDelete = canDelete;
    }
    
    // Convenience methods to access composite key fields
    public Long getUserId() {
        return id != null ? id.getUserId() : null;
    }
    
    public Long getTaskId() {
        return id != null ? id.getTaskId() : null;
    }
    
    public Long getSeq() {
        return id != null ? id.getSeq() : null;
    }
}
