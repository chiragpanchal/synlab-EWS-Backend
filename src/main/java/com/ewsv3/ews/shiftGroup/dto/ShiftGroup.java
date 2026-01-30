package com.ewsv3.ews.shiftGroup.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ShiftGroup {
    Long ShiftGroupId;
    String ShiftGroupName;
    String Enable;
    Long CreatedBy;
    LocalDateTime CreatedOn;
    Long LastUpdatedBy;
    LocalDateTime LastUpdateDate;
    String lastUpdatedByUserName;
    List<ShiftGroupShifts> shiftGroupShifts;

    public ShiftGroup() {
    }

    public Long getShiftGroupId() {
        return ShiftGroupId;
    }

    public void setShiftGroupId(Long shiftGroupId) {
        ShiftGroupId = shiftGroupId;
    }

    public String getShiftGroupName() {
        return ShiftGroupName;
    }

    public void setShiftGroupName(String shiftGroupName) {
        ShiftGroupName = shiftGroupName;
    }

    public String getEnable() {
        return Enable;
    }

    public void setEnable(String enable) {
        Enable = enable;
    }

    public Long getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(Long createdBy) {
        CreatedBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        CreatedOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return LastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        LastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedByUserName() {
        return lastUpdatedByUserName;
    }

    public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
        this.lastUpdatedByUserName = lastUpdatedByUserName;
    }

    public List<ShiftGroupShifts> getShiftGroupShifts() {
        return shiftGroupShifts;
    }

    public void setShiftGroupShifts(List<ShiftGroupShifts> shiftGroupShifts) {
        this.shiftGroupShifts = shiftGroupShifts;
    }
}
