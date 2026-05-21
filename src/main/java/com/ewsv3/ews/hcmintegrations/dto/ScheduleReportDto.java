package com.ewsv3.ews.hcmintegrations.dto;

public class ScheduleReportDto {

    private Long scheduleId;
    private String scheduleName;
    private String scheduleDesc;
    private String scheduleTypeCode;
    private String scheduleCategory;
    private String effectiveFromDate;
    private String effectiveToDate;
    private String firstWeek;
    private Long patternId;
    private Long patternSeqNum;
    private String patternName;
    private String patternTypeCode;
    private String lengthDaysNum;
    private String startDay;
    private String endDay;
    private Long shiftId;
    private String shiftName;
    private String shiftTypeCode;
    private String code;
    private String shiftCategoryCode;
    private String startTime;
    private String duration;
    private Long dur;

    public ScheduleReportDto() {}

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public String getScheduleName() { return scheduleName; }
    public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }

    public String getScheduleDesc() { return scheduleDesc; }
    public void setScheduleDesc(String scheduleDesc) { this.scheduleDesc = scheduleDesc; }

    public String getScheduleTypeCode() { return scheduleTypeCode; }
    public void setScheduleTypeCode(String scheduleTypeCode) { this.scheduleTypeCode = scheduleTypeCode; }

    public String getScheduleCategory() { return scheduleCategory; }
    public void setScheduleCategory(String scheduleCategory) { this.scheduleCategory = scheduleCategory; }

    public String getEffectiveFromDate() { return effectiveFromDate; }
    public void setEffectiveFromDate(String effectiveFromDate) { this.effectiveFromDate = effectiveFromDate; }

    public String getEffectiveToDate() { return effectiveToDate; }
    public void setEffectiveToDate(String effectiveToDate) { this.effectiveToDate = effectiveToDate; }

    public String getFirstWeek() { return firstWeek; }
    public void setFirstWeek(String firstWeek) { this.firstWeek = firstWeek; }

    public Long getPatternId() { return patternId; }
    public void setPatternId(Long patternId) { this.patternId = patternId; }

    public Long getPatternSeqNum() { return patternSeqNum; }
    public void setPatternSeqNum(Long patternSeqNum) { this.patternSeqNum = patternSeqNum; }

    public String getPatternName() { return patternName; }
    public void setPatternName(String patternName) { this.patternName = patternName; }

    public String getPatternTypeCode() { return patternTypeCode; }
    public void setPatternTypeCode(String patternTypeCode) { this.patternTypeCode = patternTypeCode; }

    public String getLengthDaysNum() { return lengthDaysNum; }
    public void setLengthDaysNum(String lengthDaysNum) { this.lengthDaysNum = lengthDaysNum; }

    public String getStartDay() { return startDay; }
    public void setStartDay(String startDay) { this.startDay = startDay; }

    public String getEndDay() { return endDay; }
    public void setEndDay(String endDay) { this.endDay = endDay; }

    public Long getShiftId() { return shiftId; }
    public void setShiftId(Long shiftId) { this.shiftId = shiftId; }

    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }

    public String getShiftTypeCode() { return shiftTypeCode; }
    public void setShiftTypeCode(String shiftTypeCode) { this.shiftTypeCode = shiftTypeCode; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getShiftCategoryCode() { return shiftCategoryCode; }
    public void setShiftCategoryCode(String shiftCategoryCode) { this.shiftCategoryCode = shiftCategoryCode; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Long getDur() { return dur; }
    public void setDur(Long dur) { this.dur = dur; }
}
