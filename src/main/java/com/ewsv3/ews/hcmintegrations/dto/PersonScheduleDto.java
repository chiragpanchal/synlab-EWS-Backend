package com.ewsv3.ews.hcmintegrations.dto;

import java.time.LocalDate;

public class PersonScheduleDto {

    private Long scheduleAssignmentId;
    private Long personId;
    private String personNumber;
    private String assignmentNumber;
    private String scheduleName;
    private LocalDate effectiveFromDate;
    private LocalDate effectiveToDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String primary;

    public PersonScheduleDto() {}

    public Long getScheduleAssignmentId() { return scheduleAssignmentId; }
    public void setScheduleAssignmentId(Long scheduleAssignmentId) { this.scheduleAssignmentId = scheduleAssignmentId; }

    public Long getPersonId() { return personId; }
    public void setPersonId(Long personId) { this.personId = personId; }

    public String getPersonNumber() { return personNumber; }
    public void setPersonNumber(String personNumber) { this.personNumber = personNumber; }

    public String getAssignmentNumber() { return assignmentNumber; }
    public void setAssignmentNumber(String assignmentNumber) { this.assignmentNumber = assignmentNumber; }

    public String getScheduleName() { return scheduleName; }
    public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }

    public LocalDate getEffectiveFromDate() { return effectiveFromDate; }
    public void setEffectiveFromDate(LocalDate effectiveFromDate) { this.effectiveFromDate = effectiveFromDate; }

    public LocalDate getEffectiveToDate() { return effectiveToDate; }
    public void setEffectiveToDate(LocalDate effectiveToDate) { this.effectiveToDate = effectiveToDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPrimary() { return primary; }
    public void setPrimary(String primary) { this.primary = primary; }
}
