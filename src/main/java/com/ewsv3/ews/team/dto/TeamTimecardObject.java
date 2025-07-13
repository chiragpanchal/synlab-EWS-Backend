package com.ewsv3.ews.team.dto;

import java.time.LocalDate;

public class TeamTimecardObject {

    private Long personId;
    private LocalDate effectiveDate;
    private Long departmentId;
    private Long jobTitleId;
    private Long workLocationId;
    private String jobTitle;
    private String departmentName;
    private String locationName;
    private String onCall;
    private String emergency;
    private Long workDurationId;
    private String workDurationCode;
    private Double schHrs;
    private Double actHrs;
    private Integer leaveCounts;
    private Integer holidayCounts;
    private Integer violationCounts;

    public TeamTimecardObject() {
    }

    public TeamTimecardObject(Long personId, LocalDate effectiveDate, Long departmentId, Long jobTitleId,
            Long workLocationId, String jobTitle, String departmentName, String locationName, String onCall,
            String emergency, Long workDurationId, String workDurationCode, Double schHrs, Double actHrs,
            Integer leaveCounts, Integer holidayCounts, Integer violationCounts) {
        this.personId = personId;
        this.effectiveDate = effectiveDate;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.workLocationId = workLocationId;
        this.jobTitle = jobTitle;
        this.departmentName = departmentName;
        this.locationName = locationName;
        this.onCall = onCall;
        this.emergency = emergency;
        this.workDurationId = workDurationId;
        this.workDurationCode = workDurationCode;
        this.schHrs = schHrs;
        this.actHrs = actHrs;
        this.leaveCounts = leaveCounts;
        this.holidayCounts = holidayCounts;
        this.violationCounts = violationCounts;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
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

    public Long getWorkLocationId() {
        return workLocationId;
    }

    public void setWorkLocationId(Long workLocationId) {
        this.workLocationId = workLocationId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getOnCall() {
        return onCall;
    }

    public void setOnCall(String onCall) {
        this.onCall = onCall;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public String getWorkDurationCode() {
        return workDurationCode;
    }

    public void setWorkDurationCode(String workDurationCode) {
        this.workDurationCode = workDurationCode;
    }

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public Double getActHrs() {
        return actHrs;
    }

    public void setActHrs(Double actHrs) {
        this.actHrs = actHrs;
    }

    public Integer getLeaveCounts() {
        return leaveCounts;
    }

    public void setLeaveCounts(Integer leaveCounts) {
        this.leaveCounts = leaveCounts;
    }

    public Integer getHolidayCounts() {
        return holidayCounts;
    }

    public void setHolidayCounts(Integer holidayCounts) {
        this.holidayCounts = holidayCounts;
    }

    public Integer getViolationCounts() {
        return violationCounts;
    }

    public void setViolationCounts(Integer violationCounts) {
        this.violationCounts = violationCounts;
    }
}
