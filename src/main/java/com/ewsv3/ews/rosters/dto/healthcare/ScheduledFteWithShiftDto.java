package com.ewsv3.ews.rosters.dto.healthcare;

public class ScheduledFteWithShiftDto {
    private Long jobTitleId;
    private Long workDurationId;
    private Integer scheduledFte;

    public ScheduledFteWithShiftDto() {}

    public ScheduledFteWithShiftDto(Long jobTitleId, Long workDurationId, Integer scheduledFte) {
        this.jobTitleId = jobTitleId;
        this.workDurationId = workDurationId;
        this.scheduledFte = scheduledFte;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public Integer getScheduledFte() {
        return scheduledFte;
    }

    public void setScheduledFte(Integer scheduledFte) {
        this.scheduledFte = scheduledFte;
    }
}
