package com.ewsv3.ews.timesheets.dto;

import java.time.LocalDate;
import java.util.List;

public class TimesheetDateSummary {
    private Long personId;
    private LocalDate effectiveDate;
    private Double schHrs;
    private Double punchHrs;
    private Double breakHrs;
    private Integer violationCounts;
    private Integer leaveCounts;
    private List<TimesheetTableSummary> timesheetTableSummaries;


    public TimesheetDateSummary() {
    }

    public TimesheetDateSummary(Long personId, LocalDate effectiveDate, Double schHrs, Double punchHrs, Double breakHrs, Integer violationCounts, Integer leaveCounts, List<TimesheetTableSummary> timesheetTableSummaries) {
        this.personId = personId;
        this.effectiveDate = effectiveDate;
        this.schHrs = schHrs;
        this.punchHrs = punchHrs;
        this.breakHrs = breakHrs;
        this.violationCounts = violationCounts;
        this.leaveCounts = leaveCounts;
        this.timesheetTableSummaries = timesheetTableSummaries;
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

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public Double getPunchHrs() {
        return punchHrs;
    }

    public void setPunchHrs(Double punchHrs) {
        this.punchHrs = punchHrs;
    }

    public Double getBreakHrs() {
        return breakHrs;
    }

    public void setBreakHrs(Double breakHrs) {
        this.breakHrs = breakHrs;
    }

    public Integer getViolationCounts() {
        return violationCounts;
    }

    public void setViolationCounts(Integer violationCounts) {
        this.violationCounts = violationCounts;
    }

    public Integer getLeaveCounts() {
        return leaveCounts;
    }

    public void setLeaveCounts(Integer leaveCounts) {
        this.leaveCounts = leaveCounts;
    }

    public List<TimesheetTableSummary> getTimesheetTableSummaries() {
        return timesheetTableSummaries;
    }

    public void setTimesheetTableSummaries(List<TimesheetTableSummary> timesheetTableSummaries) {
        this.timesheetTableSummaries = timesheetTableSummaries;
    }
}
