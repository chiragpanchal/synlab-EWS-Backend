package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SC_APPOINT_RULES")
@SequenceGenerator(name = "appoint_rule_id_gen", sequenceName = "appoint_rule_id_sq", allocationSize = 1)
public class AppointRule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appoint_rule_id_gen")
    @Column(name = "APPOINT_RULE_ID")
    private Long appointRuleId;

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @Column(name = "REQ_FTE")
    private BigDecimal reqFte;

    @Column(name = "JOB_TITLE_ID")
    private Long jobTitleId;

    @Column(name = "NOS_APPOINTMENTS")
    private BigDecimal nosAppointments;

    public AppointRule() {}

    public AppointRule(Long appointRuleId, Long profileId, BigDecimal reqFte, Long jobTitleId, BigDecimal nosAppointments) {
        this.appointRuleId = appointRuleId;
        this.profileId = profileId;
        this.reqFte = reqFte;
        this.jobTitleId = jobTitleId;
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

    public BigDecimal getNosAppointments() {
        return nosAppointments;
    }

    public void setNosAppointments(BigDecimal nosAppointments) {
        this.nosAppointments = nosAppointments;
    }
}
