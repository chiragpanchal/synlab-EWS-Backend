package com.ewsv3.ews.rosters.dto.rosters;

import java.util.List;

public class RosterLines {
    private long personId;
    private long assignmentId;
    private String employeeNumber;
    private String personName;
    private String jobTitle;
    private String departmentName;
    private Double normalHours;
    private String frequency;
    private String errorString;
    private List<RosterLinesChildDates> children;

    public RosterLines() {
    }

    public RosterLines(long personId, long assignmentId, String employeeNumber, String personName, String jobTitle,
                       String departmentName,Double normalHours,String frequency,  String errorString, List<RosterLinesChildDates> children) {
        this.personId = personId;
        this.assignmentId = assignmentId;
        this.employeeNumber = employeeNumber;
        this.personName = personName;
        this.jobTitle = jobTitle;
        this.departmentName = departmentName;
        this.normalHours= normalHours;
        this.frequency= frequency;
        this.errorString = errorString;
        this.children = children;
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

    public Double getNormalHrs() {
        return normalHours;
    }

    public void setNormalHrs(Double normalHours) {
        this.normalHours = normalHours;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public List<RosterLinesChildDates> getChildren() {
        return children;
    }

    public void setChildren(List<RosterLinesChildDates> children) {
        this.children = children;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}
