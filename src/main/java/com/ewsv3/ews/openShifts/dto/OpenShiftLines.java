package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OpenShiftLines {
    Long openShiftLineId;
    Long openShiftId;
    Long demandTemplateLineId;
    Long departmentId;
    Long jobTitleId;
    Long locationId;
    Long workDurationId;
    Long sun;
    Long mon;
    Long tue;
    Long wed;
    Long thu;
    Long fri;
    Long sat;
    Long createdBy;
    LocalDateTime createdOn;
    Long lastUpdatedBy;
    LocalDateTime lastUpdateDate;
    List<OpenShiftDetailSkills> openShiftDetailSkills;

    public OpenShiftLines() {
    }

    public Long getOpenShiftLineId() {
        return openShiftLineId;
    }

    public Long getOpenShiftId() {
        return openShiftId;
    }

    public Long getDemandTemplateLineId() {
        return demandTemplateLineId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public Long getSun() {
        return sun;
    }

    public Long getMon() {
        return mon;
    }

    public Long getTue() {
        return tue;
    }

    public Long getWed() {
        return wed;
    }

    public Long getThu() {
        return thu;
    }

    public Long getFri() {
        return fri;
    }

    public Long getSat() {
        return sat;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setOpenShiftLineId(Long openShiftLineId) {
        this.openShiftLineId = openShiftLineId;
    }

    public void setOpenShiftId(Long openShiftId) {
        this.openShiftId = openShiftId;
    }

    public List<OpenShiftDetailSkills> getOpenShiftDetailSkills() {
        return openShiftDetailSkills;
    }

    public void setOpenShiftDetailSkills(List<OpenShiftDetailSkills> openShiftDetailSkills) {
        this.openShiftDetailSkills = openShiftDetailSkills;
    }
}
