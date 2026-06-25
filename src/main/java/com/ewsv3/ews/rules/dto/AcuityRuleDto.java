package com.ewsv3.ews.rules.dto;

import java.math.BigDecimal;

public class AcuityRuleDto {
    private Long acuityRuleId;
    private Long profileId;
    private Long acuityLevelId;
    private Long ratioId;
    private String levelName;
    private String ratioName;
    private BigDecimal beds;
    private BigDecimal reqFte;

    public AcuityRuleDto() {}

    public AcuityRuleDto(Long acuityRuleId, Long profileId, Long acuityLevelId, Long ratioId,
                         String levelName, String ratioName, BigDecimal beds, BigDecimal reqFte) {
        this.acuityRuleId = acuityRuleId;
        this.profileId = profileId;
        this.acuityLevelId = acuityLevelId;
        this.ratioId = ratioId;
        this.levelName = levelName;
        this.ratioName = ratioName;
        this.beds = beds;
        this.reqFte = reqFte;
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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getRatioName() {
        return ratioName;
    }

    public void setRatioName(String ratioName) {
        this.ratioName = ratioName;
    }

    public BigDecimal getBeds() {
        return beds;
    }

    public void setBeds(BigDecimal beds) {
        this.beds = beds;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
