package com.ewsv3.ews.dashboard.dto;

import java.util.Objects;

public final class TeamViolations {
    private String personName;
    private  String employeeNumber;
    private  String emailAddress;
    private  Long personId;
    private  String jobTitle;
    private  String departmentName;
    private  Long violationCounts;
    private  Long currViolationCounts;
    private  Long prevViolationCounts;

    public TeamViolations(
            String personName,
            String employeeNumber,
            String emailAddress,
            Long personId,
            String jobTitle,
            String departmentName,
            Long violationCounts,
            Long currViolationCounts,
            Long prevViolationCounts
    ) {
        this.personName = personName;
        this.employeeNumber = employeeNumber;
        this.emailAddress = emailAddress;
        this.personId = personId;
        this.jobTitle = jobTitle;
        this.departmentName = departmentName;
        this.violationCounts = violationCounts;
        this.currViolationCounts = currViolationCounts;
        this.prevViolationCounts = prevViolationCounts;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
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

    public Long getViolationCounts() {
        return violationCounts;
    }

    public void setViolationCounts(Long violationCounts) {
        this.violationCounts = violationCounts;
    }

    public Long getCurrViolationCounts() {
        return currViolationCounts;
    }

    public void setCurrViolationCounts(Long currViolationCounts) {
        this.currViolationCounts = currViolationCounts;
    }

    public Long getPrevViolationCounts() {
        return prevViolationCounts;
    }

    public void setPrevViolationCounts(Long prevViolationCounts) {
        this.prevViolationCounts = prevViolationCounts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TeamViolations) obj;
        return Objects.equals(this.personName, that.personName) &&
                Objects.equals(this.employeeNumber, that.employeeNumber) &&
                Objects.equals(this.emailAddress, that.emailAddress) &&
                Objects.equals(this.personId, that.personId) &&
                Objects.equals(this.jobTitle, that.jobTitle) &&
                Objects.equals(this.departmentName, that.departmentName) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, employeeNumber, emailAddress, personId, jobTitle, departmentName, violationCounts, currViolationCounts, prevViolationCounts);
    }

    @Override
    public String toString() {
        return "TeamViolations[" +
                "personName=" + personName + ", " +
                "employeeNumber=" + employeeNumber + ", " +
                "emailAddress=" + emailAddress + ", " +
                "personId=" + personId + ", " +
                "jobTitle=" + jobTitle + ", " +
                "departmentName=" + departmentName + ", " +
                "violationCounts=" + violationCounts + ", " +
                "currViolationCounts=" + currViolationCounts + ", " +
                "prevViolationCounts=" + prevViolationCounts + ']';
    }

}
