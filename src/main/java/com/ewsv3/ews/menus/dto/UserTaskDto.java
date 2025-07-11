package com.ewsv3.ews.menus.dto;

public class UserTaskDto {
    private Long seq;
    private Long enterpriseId;
    private Long employeeId;
    private String groupName;
    private Long groupSeq;
    private Long taskId;
    private String taskName;
    private String taskCode;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canView;
    private Long userId;

    // Default constructor
    public UserTaskDto() {}

    // Constructor from entity
    public UserTaskDto(com.ewsv3.ews.menus.model.UserTask userTask) {
        this.seq = userTask.getSeq();
        this.enterpriseId = userTask.getEnterpriseId();
        this.employeeId = userTask.getEmployeeId();
        this.groupName = userTask.getGroupName();
        this.groupSeq = userTask.getGroupSeq();
        this.taskId = userTask.getTaskId();
        this.taskName = userTask.getTaskName();
        this.taskCode = userTask.getTaskCode();
        this.canCreate = "Y".equals(userTask.getCanCreate());
        this.canEdit = "Y".equals(userTask.getCanEdit());
        this.canDelete = "Y".equals(userTask.getCanDelete());
        this.canView = "Y".equals(userTask.getCanView());
        this.userId = userTask.getUserId();
    }

    // Getters and setters
    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupSeq() {
        return groupSeq;
    }

    public void setGroupSeq(Long groupSeq) {
        this.groupSeq = groupSeq;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
