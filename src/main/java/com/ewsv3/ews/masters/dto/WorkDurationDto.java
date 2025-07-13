package com.ewsv3.ews.masters.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;


public class WorkDurationDto {

        private long workDurationId;
        private String workDurationCode;
        private String workDurationName;
        private LocalDate validFrom;
        private LocalDate validTo;
        private LocalDateTime timeStart;
        private LocalDateTime breakStart;
        private LocalDateTime breakEnd;
        private LocalDateTime timeEnd;
        private long enterpriseId;
        private String mon;
        private String tue;
        private String wed;
        private String thu;
        private String fri;
        private String sat;
        private String sun;
        private String colorCode;
        private double duration;
        private Long workDurationCategoryId;
        private String exceptionEvents;
        private Double minWorkHrs;
        private Double maxWorkHrs;
        private String workUnit;
        private Long hcmScheduleId;
        private String erosterCode;
//        private List<WorkDurationDetailsDto> detailsDtoList;

    public WorkDurationDto() {
    }

    public WorkDurationDto(long workDurationId, String workDurationCode, String workDurationName, LocalDate validFrom, LocalDate validTo, LocalDateTime timeStart, LocalDateTime breakStart, LocalDateTime breakEnd, LocalDateTime timeEnd, long enterpriseId, String mon, String tue, String wed, String thu, String fri, String sat, String sun, String colorCode, double duration, long workDurationCategoryId, String exceptionEvents, Double minWorkHrs, Double maxWorkHrs, String workUnit, Long hcmScheduleId, String erosterCode) {
        this.workDurationId = workDurationId;
        this.workDurationCode = workDurationCode;
        this.workDurationName = workDurationName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.timeStart = timeStart;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.timeEnd = timeEnd;
        this.enterpriseId = enterpriseId;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.colorCode = colorCode;
        this.duration = duration;
        this.workDurationCategoryId = workDurationCategoryId;
        this.exceptionEvents = exceptionEvents;
        this.minWorkHrs = minWorkHrs;
        this.maxWorkHrs = maxWorkHrs;
        this.workUnit = workUnit;
        this.hcmScheduleId = hcmScheduleId;
        this.erosterCode = erosterCode;
//        this.detailsDtoList = detailsDtoList;
    }

    public long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(long workDurationId) {
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

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(LocalDateTime breakStart) {
        this.breakStart = breakStart;
    }

    public LocalDateTime getBreakEnd() {
        return breakEnd;
    }

    public void setBreakEnd(LocalDateTime breakEnd) {
        this.breakEnd = breakEnd;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Long getWorkDurationCategoryId() {
        return workDurationCategoryId;
    }

    public void setWorkDurationCategoryId(Long workDurationCategoryId) {
        this.workDurationCategoryId = workDurationCategoryId;
    }

    public String getExceptionEvents() {
        return exceptionEvents;
    }

    public void setExceptionEvents(String exceptionEvents) {
        this.exceptionEvents = exceptionEvents;
    }

    public Double getMinWorkHrs() {
        return minWorkHrs;
    }

    public void setMinWorkHrs(Double minWorkHrs) {
        this.minWorkHrs = minWorkHrs;
    }

    public Double getMaxWorkHrs() {
        return maxWorkHrs;
    }

    public void setMaxWorkHrs(Double maxWorkHrs) {
        this.maxWorkHrs = maxWorkHrs;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public Long getHcmScheduleId() {
        return hcmScheduleId;
    }

    public void setHcmScheduleId(Long hcmScheduleId) {
        this.hcmScheduleId = hcmScheduleId;
    }

    public String getErosterCode() {
        return erosterCode;
    }

    public void setErosterCode(String erosterCode) {
        this.erosterCode = erosterCode;
    }

    //    public void setDetailsDtoList(List<WorkDurationDetailsDto> detailsDtoList) {
//        this.detailsDtoList = detailsDtoList;
//    }

    @Override
    public String toString() {
        return "WorkDurationDto{" +
                "workDurationId=" + workDurationId +
                ", workDurationCode='" + workDurationCode + '\'' +
                ", workDurationName='" + workDurationName + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", timeStart=" + timeStart +
                ", breakStart=" + breakStart +
                ", breakEnd=" + breakEnd +
                ", timeEnd=" + timeEnd +
                ", enterpriseId=" + enterpriseId +
                ", mon='" + mon + '\'' +
                ", tue='" + tue + '\'' +
                ", wed='" + wed + '\'' +
                ", thu='" + thu + '\'' +
                ", fri='" + fri + '\'' +
                ", sat='" + sat + '\'' +
                ", sun='" + sun + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", duration=" + duration +
                ", workDurationCategoryId=" + workDurationCategoryId +
                ", exceptionEvents='" + exceptionEvents + '\'' +
                ", minWorkHrs=" + minWorkHrs +
                ", maxWorkHrs=" + maxWorkHrs +
                ", workUnit='" + workUnit + '\'' +
                ", hcmScheduleId=" + hcmScheduleId +
                ", erosterCode='" + erosterCode + '\'' +
                '}';
    }
}
