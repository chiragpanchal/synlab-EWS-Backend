package com.ewsv3.ews.rosters.dto.healthcare;

import java.math.BigDecimal;

public class PersonMatchingDto {
    private Long personId;
    private Long assignmentId;
    private Long departmentId;
    private Long jobTitleId;
    private Long workLocationId;
    private String personName;
    private String employeeNumber;
    private String gradeName;
    private BigDecimal schHrs;
    private BigDecimal rate;
    private BigDecimal matchedPerc;

    public PersonMatchingDto() {}

//    public PersonMatchingDto(Long personId, String personName, String employeeNumber,
//                            String gradeName, BigDecimal schHrs, BigDecimal rate, BigDecimal matchedPerc) {
//        this.personId = personId;
//        this.personName = personName;
//        this.employeeNumber = employeeNumber;
//        this.gradeName = gradeName;
//        this.schHrs = schHrs;
//        this.rate = rate;
//        this.matchedPerc = matchedPerc;
//    }

    public PersonMatchingDto(Long personId, Long assignmentId, Long departmentId, Long jobTitleId, Long workLocationId, String personName, String employeeNumber, String gradeName, BigDecimal schHrs, BigDecimal rate, BigDecimal matchedPerc) {
        this.personId = personId;
        this.assignmentId = assignmentId;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.workLocationId = workLocationId;
        this.personName = personName;
        this.employeeNumber = employeeNumber;
        this.gradeName = gradeName;
        this.schHrs = schHrs;
        this.rate = rate;
        this.matchedPerc = matchedPerc;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public BigDecimal getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(BigDecimal schHrs) {
        this.schHrs = schHrs;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMatchedPerc() {
        return matchedPerc;
    }

    public void setMatchedPerc(BigDecimal matchedPerc) {
        this.matchedPerc = matchedPerc;
    }
}
