package com.ewsv3.ews.team.dto;

import java.util.List;

public  class TeamMembers {
    private  Long personId;
    private  Long personUserId;
    private  String employeeNumber;
    private  String personName;
    private  String emailAddress;
    private  String departmentName;
    private  String positionName;
    private  String jobTitle;
    private  String locationName;
    private  String shiftType;
    private  String band;
    private  Long departmentId;
    private  Long jobTitleId;
    private  Long workLocationId;
    private  Long businessUnitId;
    private  Long legalEntityId;
    private  Long employeeTypeId;
    private  String jobFamily;
    private  String citizenshipLookup;
    private  String genderLookup;
    private  String religionLookup;
    private  Long gradeId;
    private  List<TimecardLine> timecardLines;

    public TeamMembers() {
    }

    public TeamMembers(Long personId, Long personUserId, String employeeNumber, String personName, String emailAddress, String departmentName, String positionName, String jobTitle, String locationName, String shiftType, String band, Long departmentId, Long jobTitleId, Long workLocationId, Long businessUnitId, Long legalEntityId, Long employeeTypeId, String jobFamily, String citizenshipLookup, String genderLookup, String religionLookup, Long gradeId, List<TimecardLine> timecardLines) {
        this.personId = personId;
        this.personUserId = personUserId;
        this.employeeNumber = employeeNumber;
        this.personName = personName;
        this.emailAddress = emailAddress;
        this.departmentName = departmentName;
        this.positionName = positionName;
        this.jobTitle = jobTitle;
        this.locationName = locationName;
        this.shiftType = shiftType;
        this.band = band;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.workLocationId = workLocationId;
        this.businessUnitId = businessUnitId;
        this.legalEntityId = legalEntityId;
        this.employeeTypeId = employeeTypeId;
        this.jobFamily = jobFamily;
        this.citizenshipLookup = citizenshipLookup;
        this.genderLookup = genderLookup;
        this.religionLookup = religionLookup;
        this.gradeId = gradeId;
        this.timecardLines = timecardLines;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPersonUserId() {
        return personUserId;
    }

    public void setPersonUserId(Long personUserId) {
        this.personUserId = personUserId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
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

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Long getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(Long legalEntityId) {
        this.legalEntityId = legalEntityId;
    }

    public Long getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public String getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(String jobFamily) {
        this.jobFamily = jobFamily;
    }

    public String getCitizenshipLookup() {
        return citizenshipLookup;
    }

    public void setCitizenshipLookup(String citizenshipLookup) {
        this.citizenshipLookup = citizenshipLookup;
    }

    public String getGenderLookup() {
        return genderLookup;
    }

    public void setGenderLookup(String genderLookup) {
        this.genderLookup = genderLookup;
    }

    public String getReligionLookup() {
        return religionLookup;
    }

    public void setReligionLookup(String religionLookup) {
        this.religionLookup = religionLookup;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public List<TimecardLine> getTimecardLines() {
        return timecardLines;
    }

    public void setTimecardLines(List<TimecardLine> timecardLines) {
        this.timecardLines = timecardLines;
    }
}
