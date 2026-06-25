package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SC_OCCUPY_RULES")
@SequenceGenerator(name = "occupy_rule_id_gen", sequenceName = "occupy_rule_id_sq", allocationSize = 1)
public class OccupyRule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "occupy_rule_id_gen")
    @Column(name = "OCCUPY_RULE_ID")
    private Long occupyRuleId;

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @Column(name = "OCC_BEDS")
    private BigDecimal occBeds;

    @Column(name = "WORK_DURATION_ID")
    private Long workDurationId;

    @Column(name = "REQ_FTE")
    private BigDecimal reqFte;

    @Column(name = "JOB_TITLE_ID")
    private Long jobTitleId;

    public OccupyRule() {}

    public OccupyRule(Long occupyRuleId, Long profileId, BigDecimal occBeds,
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
