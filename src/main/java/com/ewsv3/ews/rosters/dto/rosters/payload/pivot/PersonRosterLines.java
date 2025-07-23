package com.ewsv3.ews.rosters.dto.rosters.payload.pivot;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonRosterLines {

    private long personId;
    private long assignmentId;
    private String employeeNumber;
    private String fullName;
    private long departmentId;
    private String departmentName;
    private long jobTitleId;
    private String jobTitle;
    private long workLocationId;
    private String locationName;
    private String employeeTypes;
    private String emailAddress;
    private String positionName;
    private LocalDate hireDate;
    private String gradeName;
    private LocalDate effectiveDate;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private Double schHrs;
    private Long schDepartmentId;
    private Long schJobTitleId;
    private Long schWorkLocationId;
    private String schDepartment;
    private String schJobTitle;
    private String schLocation;
    private String onCall;
    private String emergency;
    private String published;
    private Long workDurationId;
    private String workDurationCode;
    private String workDurationName;


    public PersonRosterLines() {
    }

    public PersonRosterLines(long personId, long assignmentId, String employeeNumber, String fullName, long departmentId, String departmentName, long jobTitleId, String jobTitle, long workLocationId, String locationName, String employeeTypes, String emailAddress, String positionName, LocalDate hireDate, String gradeName, LocalDate effectiveDate, LocalDateTime timeStart, LocalDateTime timeEnd, Double schHrs, Long schDepartmentId, Long schJobTitleId, Long schWorkLocationId, String schDepartment, String schJobTitle, String schLocation, String onCall, String emergency, String published, Long workDurationId, String workDurationCode, String workDurationName) {
        this.personId = personId;
        this.assignmentId = assignmentId;
        this.employeeNumber = employeeNumber;
        this.fullName = fullName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.jobTitleId = jobTitleId;
        this.jobTitle = jobTitle;
        this.workLocationId = workLocationId;
        this.locationName = locationName;
        this.employeeTypes = employeeTypes;
        this.emailAddress = emailAddress;
        this.positionName = positionName;
        this.hireDate = hireDate;
        this.gradeName = gradeName;
        this.effectiveDate = effectiveDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.schHrs = schHrs;
        this.schDepartmentId = schDepartmentId;
        this.schJobTitleId = schJobTitleId;
        this.schWorkLocationId = schWorkLocationId;
        this.schDepartment = schDepartment;
        this.schJobTitle = schJobTitle;
        this.schLocation = schLocation;
        this.onCall = onCall;
        this.emergency = emergency;
        this.published = published;
        this.workDurationId = workDurationId;
        this.workDurationCode = workDurationCode;
        this.workDurationName = workDurationName;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public long getWorkLocationId() {
        return workLocationId;
    }

    public void setWorkLocationId(long workLocationId) {
        this.workLocationId = workLocationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getEmployeeTypes() {
        return employeeTypes;
    }

    public void setEmployeeTypes(String employeeTypes) {
        this.employeeTypes = employeeTypes;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public Long getSchDepartmentId() {
        return schDepartmentId;
    }

    public void setSchDepartmentId(Long schDepartmentId) {
        this.schDepartmentId = schDepartmentId;
    }

    public Long getSchJobTitleId() {
        return schJobTitleId;
    }

    public void setSchJobTitleId(Long schJobTitleId) {
        this.schJobTitleId = schJobTitleId;
    }

    public Long getSchWorkLocationId() {
        return schWorkLocationId;
    }

    public void setSchWorkLocationId(Long schWorkLocationId) {
        this.schWorkLocationId = schWorkLocationId;
    }

    public String getSchDepartment() {
        return schDepartment;
    }

    public void setSchDepartment(String schDepartment) {
        this.schDepartment = schDepartment;
    }

    public String getSchJobTitle() {
        return schJobTitle;
    }

    public void setSchJobTitle(String schJobTitle) {
        this.schJobTitle = schJobTitle;
    }

    public String getSchLocation() {
        return schLocation;
    }

    public void setSchLocation(String schLocation) {
        this.schLocation = schLocation;
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

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
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

    public String getWorkDurationName() {
        return workDurationName;
    }

    public void setWorkDurationName(String workDurationName) {
        this.workDurationName = workDurationName;
    }
}
