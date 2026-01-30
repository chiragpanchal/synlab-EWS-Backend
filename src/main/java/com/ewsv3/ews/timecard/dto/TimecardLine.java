package com.ewsv3.ews.timecard.dto;


import com.ewsv3.ews.request.dto.RequestResp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TimecardLine {
    private Long timecardId;
    private Long personRosterId;
    private LocalDate effectiveDate;
    private Long jobTitleId;
    private String jobTitle;
    private Long departmentId;
    private String departmentName;
    private Long workLocationId;
    private String locationName;
    private String onCall;
    private String emergency;
    private String project;
    private String task;
    private LocalDateTime schTimeStart;
    private LocalDateTime schTimeEnd;
    private Double schHrs;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private Double actHrs;
    private String primaryRow;
    private String timeType;
    private String violationCode;
    private Integer occurences;
    private String workDurationCode;
    private Integer requestCounts;
    private List<TimecardActuals> timecardActuals;
    private List<RequestResp> requestRespList;

    public TimecardLine() {
    }

    public TimecardLine(Long timecardId, Long personRosterId, LocalDate effectiveDate, Long jobTitleId, String jobTitle, Long departmentId, String departmentName, Long workLocationId, String locationName, String onCall, String emergency, String project, String task, LocalDateTime schTimeStart, LocalDateTime schTimeEnd, Double schHrs, LocalDateTime inTime, LocalDateTime outTime, Double actHrs, String primaryRow, String timeType, String violationCode,Integer occurences, String workDurationCode, Integer requestCounts, List<TimecardActuals> timecardActuals, List<RequestResp> requestRespList) {
        this.timecardId = timecardId;
        this.personRosterId = personRosterId;
        this.effectiveDate = effectiveDate;
        this.jobTitleId = jobTitleId;
        this.jobTitle = jobTitle;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.workLocationId = workLocationId;
        this.locationName = locationName;
        this.onCall = onCall;
        this.emergency = emergency;
        this.project = project;
        this.task = task;
        this.schTimeStart = schTimeStart;
        this.schTimeEnd = schTimeEnd;
        this.schHrs = schHrs;
        this.inTime = inTime;
        this.outTime = outTime;
        this.actHrs = actHrs;
        this.primaryRow = primaryRow;
        this.timeType = timeType;
        this.violationCode = violationCode;
        this.occurences = occurences;
        this.workDurationCode = workDurationCode;
        this.requestCounts = requestCounts;
        this.timecardActuals = timecardActuals;
        this.requestRespList = requestRespList;
    }

    public Long getTimecardId() {
        return timecardId;
    }

    public void setTimecardId(Long timecardId) {
        this.timecardId = timecardId;
    }

    public Long getPersonRosterId() {
        return personRosterId;
    }

    public void setPersonRosterId(Long personRosterId) {
        this.personRosterId = personRosterId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getWorkLocationId() {
        return workLocationId;
    }

    public void setWorkLocationId(Long workLocationId) {
        this.workLocationId = workLocationId;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public LocalDateTime getSchTimeStart() {
        return schTimeStart;
    }

    public void setSchTimeStart(LocalDateTime schTimeStart) {
        this.schTimeStart = schTimeStart;
    }

    public LocalDateTime getSchTimeEnd() {
        return schTimeEnd;
    }

    public void setSchTimeEnd(LocalDateTime schTimeEnd) {
        this.schTimeEnd = schTimeEnd;
    }

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    public Double getActHrs() {
        return actHrs;
    }

    public void setActHrs(Double actHrs) {
        this.actHrs = actHrs;
    }

    public String getPrimaryRow() {
        return primaryRow;
    }

    public void setPrimaryRow(String primaryRow) {
        this.primaryRow = primaryRow;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public Integer getOccurences() {
        return occurences;
    }

    public void setOccurences(Integer occurences) {
        this.occurences = occurences;
    }

    public String getWorkDurationCode() {
        return workDurationCode;
    }

    public void setWorkDurationCode(String workDurationCode) {
        this.workDurationCode = workDurationCode;
    }

    public Integer getRequestCounts() {
        return requestCounts;
    }

    public void setRequestCounts(Integer requestCounts) {
        this.requestCounts = requestCounts;
    }

    public List<TimecardActuals> getTimecardActuals() {
        return timecardActuals;
    }

    public void setTimecardActuals(List<TimecardActuals> timecardActuals) {
        this.timecardActuals = timecardActuals;
    }

    public List<RequestResp> getRequestRespList() {
        return requestRespList;
    }

    public void setRequestRespList(List<RequestResp> requestRespList) {
        this.requestRespList = requestRespList;
    }
}
