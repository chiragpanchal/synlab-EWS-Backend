package com.ewsv3.ews.rosters.dto.healthcare;

public class ScheduledFteDto {
    private Long jobTitleId;
    private Integer scheduledFte;

    public ScheduledFteDto() {}

    public ScheduledFteDto(Long jobTitleId, Integer scheduledFte) {
        this.jobTitleId = jobTitleId;
        this.scheduledFte = scheduledFte;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Integer getScheduledFte() {
        return scheduledFte;
    }

    public void setScheduledFte(Integer scheduledFte) {
        this.scheduledFte = scheduledFte;
    }
}
