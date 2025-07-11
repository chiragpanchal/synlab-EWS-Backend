package com.ewsv3.ews.menus.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTaskId implements Serializable {
    
    @Column(name = "USER_ID")
    private Long userId;
    
    @Column(name = "TASK_ID")
    private Long taskId;
    
    @Column(name = "SEQ")
    private Long seq;
    
    public UserTaskId() {}
    
    public UserTaskId(Long userId, Long taskId, Long seq) {
        this.userId = userId;
        this.taskId = taskId;
        this.seq = seq;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Long getSeq() {
        return seq;
    }
    
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTaskId that = (UserTaskId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(taskId, that.taskId) &&
               Objects.equals(seq, that.seq);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, taskId, seq);
    }
}
