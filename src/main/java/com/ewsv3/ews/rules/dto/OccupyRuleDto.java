package com.ewsv3.ews.rules.dto;

import java.math.BigDecimal;

public class OccupyRuleDto {
    private Long occupyRuleId;
    private Long profileId;
    private BigDecimal occBeds;
    private Long workDurationId;
    private BigDecimal reqFte;
    private Long jobTitleId;

    public OccupyRuleDto() {}

    public OccupyRuleDto(Long occupyRuleId, Long profileId, BigDecimal occBeds,
                         Long workDurationId, BigDecimal reqFte, Long jobTitleId) {
        this.occupyRuleId = occupyRuleId;
        this.profileId = profileId;
        this.occBeds = occBeds;
        this.workDurationId = workDurationId;
        this.reqFte = reqFte;
        this.jobTitleId = jobTitleId;
    }

    public Long getOccupyRuleId() {
        return occupyRuleId;
    }

    public void setOccupyRuleId(Long occupyRuleId) {
        this.occupyRuleId = occupyRuleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public BigDecimal getOccBeds() {
        return occBeds;
    }

    public void setOccBeds(BigDecimal occBeds) {
        this.occBeds = occBeds;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
}
