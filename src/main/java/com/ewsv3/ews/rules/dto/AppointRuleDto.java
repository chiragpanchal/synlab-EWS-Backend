package com.ewsv3.ews.rules.dto;

import java.math.BigDecimal;

public class AppointRuleDto {
    private Long appointRuleId;
    private Long profileId;
    private BigDecimal reqFte;
    private Long jobTitleId;
    private Long workDurationId;
    private BigDecimal nosAppointments;

    public AppointRuleDto() {}

    public AppointRuleDto(Long appointRuleId, Long profileId, BigDecimal reqFte, Long jobTitleId, Long workDurationId, BigDecimal nosAppointments) {
        this.appointRuleId = appointRuleId;
        this.profileId = profileId;
        this.reqFte = reqFte;
        this.jobTitleId = jobTitleId;
        this.workDurationId = workDurationId;
        this.nosAppointments = nosAppointments;
    }

    public Long getAppointRuleId() {
        return appointRuleId;
    }

    public void setAppointRuleId(Long appointRuleId) {
        this.appointRuleId = appointRuleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
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

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public BigDecimal getNosAppointments() {
        return nosAppointments;
    }

    public void setNosAppointments(BigDecimal nosAppointments) {
        this.nosAppointments = nosAppointments;
    }
}
