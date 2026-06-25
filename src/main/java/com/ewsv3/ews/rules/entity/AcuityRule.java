package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SC_ACUITY_RULES")
@SequenceGenerator(name = "acuity_rule_id_gen", sequenceName = "acuity_rule_id_sq", allocationSize = 1)
public class AcuityRule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acuity_rule_id_gen")
    @Column(name = "ACUITY_RULE_ID")
    private Long acuityRuleId;

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @Column(name = "ACUITY_LEVEL_ID", nullable = false)
    private Long acuityLevelId;

    @Column(name = "RATIO_ID", nullable = false)
    private Long ratioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACUITY_LEVEL_ID", referencedColumnName = "ACUITY_LEVEL_ID", insertable = false, updatable = false)
    private AcuityLevel acuityLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RATIO_ID", referencedColumnName = "RATIO_ID", insertable = false, updatable = false)
    private AcuityRatio acuityRatio;

    public AcuityRule() {}

    public AcuityRule(Long acuityRuleId, Long profileId, Long acuityLevelId, Long ratioId) {
        this.acuityRuleId = acuityRuleId;
        this.profileId = profileId;
        this.acuityLevelId = acuityLevelId;
        this.ratioId = ratioId;
    }

    public Long getAcuityRuleId() {
        return acuityRuleId;
    }

    public void setAcuityRuleId(Long acuityRuleId) {
        this.acuityRuleId = acuityRuleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getAcuityLevelId() {
        return acuityLevelId;
    }

    public void setAcuityLevelId(Long acuityLevelId) {
        this.acuityLevelId = acuityLevelId;
    }

    public Long getRatioId() {
        return ratioId;
    }

    public void setRatioId(Long ratioId) {
        this.ratioId = ratioId;
    }

    public AcuityLevel getAcuityLevel() {
        return acuityLevel;
    }

    public void setAcuityLevel(AcuityLevel acuityLevel) {
        this.acuityLevel = acuityLevel;
    }

    public AcuityRatio getAcuityRatio() {
        return acuityRatio;
    }

    public void setAcuityRatio(AcuityRatio acuityRatio) {
        this.acuityRatio = acuityRatio;
    }
}
