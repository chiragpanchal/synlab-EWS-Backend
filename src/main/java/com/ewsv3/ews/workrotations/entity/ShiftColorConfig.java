package com.ewsv3.ews.workrotations.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SC_SHIFT_COLOR_CONFIG")
public class ShiftColorConfig {

    @Id
    @Column(name = "SHIFT_COLOR_CONFIG_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_color_config_seq")
    @SequenceGenerator(name = "shift_color_config_seq", sequenceName = "SHIFT_COLOR_CONFIG_ID_SQ", allocationSize = 1)
    private Long shiftColorConfigId;

    @Column(name = "START_TIME", nullable = false)
    private Double startTime;

    @Column(name = "END_TIME", nullable = false)
    private Double endTime;

    @Column(name = "COLOR", nullable = false, length = 100)
    private String color;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "LAST_UPDATED_BY")
    private Long lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdOn = now;
        this.lastUpdateDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateDate = LocalDateTime.now();
    }

    public ShiftColorConfig() {
    }

    public ShiftColorConfig(Double startTime, Double endTime, String color, Long createdBy, Long lastUpdatedBy) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getShiftColorConfigId() {
        return shiftColorConfigId;
    }

    public void setShiftColorConfigId(Long shiftColorConfigId) {
        this.shiftColorConfigId = shiftColorConfigId;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
