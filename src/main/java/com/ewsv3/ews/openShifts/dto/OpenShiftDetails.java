package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OpenShiftDetails {
    Long openShiftLineId;
    Long openShiftId;
    Long demandTemplateLineId;
    Long departmentId;
    Long jobTitleId;
    Long locationId;
    Long workDurationId;
    LocalDate effectiveDate;
    Long fteRequested;
    Long createdBy;
    LocalDateTime createdOn;
    Long lastUpdatedBy;
    LocalDateTime lastUpdateDate;
    List<OpenShiftDetailSkills> openShiftDetailSkills;

    public OpenShiftDetails() {
    }

    public OpenShiftDetails(Long openShiftLineId, Long openShiftId, Long demandTemplateLineId, Long departmentId, Long jobTitleId, Long locationId, Long workDurationId, LocalDate effectiveDate, Long fteRequested, Long createdBy, LocalDateTime createdOn, Long lastUpdatedBy, LocalDateTime lastUpdateDate, List<OpenShiftDetailSkills> openShiftDetailSkills) {
        this.openShiftLineId = openShiftLineId;
        this.openShiftId = openShiftId;
        this.demandTemplateLineId = demandTemplateLineId;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.locationId = locationId;
        this.workDurationId = workDurationId;
        this.effectiveDate = effectiveDate;
        this.fteRequested = fteRequested;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
        this.openShiftDetailSkills = openShiftDetailSkills;
    }

    public Long getOpenShiftLineId() {
        return openShiftLineId;
    }

    public void setOpenShiftLineId(Long openShiftLineId) {
        this.openShiftLineId = openShiftLineId;
    }

    public Long getOpenShiftId() {
        return openShiftId;
    }

    public void setOpenShiftId(Long openShiftId) {
        this.openShiftId = openShiftId;
    }

    public Long getDemandTemplateLineId() {
        return demandTemplateLineId;
    }

    public void setDemandTemplateLineId(Long demandTemplateLineId) {
        this.demandTemplateLineId = demandTemplateLineId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getFteRequested() {
        return fteRequested;
    }

    public void setFteRequested(Long fteRequested) {
        this.fteRequested = fteRequested;
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

    public List<OpenShiftDetailSkills> getOpenShiftDetailSkills() {
        return openShiftDetailSkills;
    }

    public void setOpenShiftDetailSkills(List<OpenShiftDetailSkills> openShiftDetailSkills) {
        this.openShiftDetailSkills = openShiftDetailSkills;
    }
}
