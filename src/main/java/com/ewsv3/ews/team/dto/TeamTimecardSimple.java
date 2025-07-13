package com.ewsv3.ews.team.dto;

import java.util.List;

public class TeamTimecardSimple {
    private long personId;
    private String employeeNumber;
    private String personName;
    private String jobTitle;
    private String departmentName;
    private List<TeamTimecardSimpleChild> children;

    public TeamTimecardSimple() {
    }

    public TeamTimecardSimple(long personId, String employeeNumber, String fullName, String jobTitle, String departmentName) {
        this.personId = personId;
        this.employeeNumber = employeeNumber;
        this.personName = fullName;
        this.jobTitle = jobTitle;
        this.departmentName = departmentName;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
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

    public List<TeamTimecardSimpleChild> getChildren() {
        return children;
    }

    public void setChildren(List<TeamTimecardSimpleChild> children) {
        this.children = children;
    }
}