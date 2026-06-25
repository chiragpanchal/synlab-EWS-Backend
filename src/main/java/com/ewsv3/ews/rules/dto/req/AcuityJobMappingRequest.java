package com.ewsv3.ews.rules.dto.req;

import java.math.BigDecimal;

public class AcuityJobMappingRequest {
    private Long profileId;
    private Long forJobTitleId;
    private BigDecimal forFte;
    private Long reqJobTitleId;
    private BigDecimal reqFte;

    public AcuityJobMappingRequest() {}

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getForJobTitleId() {
        return forJobTitleId;
    }

    public void setForJobTitleId(Long forJobTitleId) {
        this.forJobTitleId = forJobTitleId;
    }

    public BigDecimal getForFte() {
        return forFte;
    }

    public void setForFte(BigDecimal forFte) {
        this.forFte = forFte;
    }

    public Long getReqJobTitleId() {
        return reqJobTitleId;
    }

    public void setReqJobTitleId(Long reqJobTitleId) {
        this.reqJobTitleId = reqJobTitleId;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
