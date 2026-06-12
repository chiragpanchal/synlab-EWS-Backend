package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;
import java.util.Date;

public class RotaDemandSuggestionDto {
    private Long personId;
    private String employeeNumber;
    private String fullName;
    private String jobTitle;
    private String lineName;
    private Double fteReq;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long workRotationId;

    public RotaDemandSuggestionDto() {
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Double getFteReq() {
        return fteReq;
    }

    public void setFteReq(Double fteReq) {
        this.fteReq = fteReq;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getWorkRotationId() {
        return workRotationId;
    }

    public void setWorkRotationId(Long workRotationId) {
        this.workRotationId = workRotationId;
    }
}
