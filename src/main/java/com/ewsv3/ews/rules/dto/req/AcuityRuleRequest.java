package com.ewsv3.ews.rules.dto.req;

public class AcuityRuleRequest {
    private Long profileId;
    private Long acuityLevelId;
    private Long ratioId;

    public AcuityRuleRequest() {}

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
}
