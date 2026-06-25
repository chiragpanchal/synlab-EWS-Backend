package com.ewsv3.ews.rules.dto;

import java.math.BigDecimal;

public class AcuityJobMappingDto {
    private Long acuityJobMappingId;
    private Long profileId;
    private Long forJobTitleId;
    private BigDecimal forFte;
    private Long reqJobTitleId;
    private BigDecimal reqFte;

    public AcuityJobMappingDto() {}

    public AcuityJobMappingDto(Long acuityJobMappingId, Long profileId, Long forJobTitleId,
                              BigDecimal forFte, Long reqJobTitleId, BigDecimal reqFte) {
        this.acuityJobMappingId = acuityJobMappingId;
        this.profileId = profileId;
        this.forJobTitleId = forJobTitleId;
        this.forFte = forFte;
        this.reqJobTitleId = reqJobTitleId;
        this.reqFte = reqFte;
    }

    public Long getAcuityJobMappingId() {
        return acuityJobMappingId;
    }

    public void setAcuityJobMappingId(Long acuityJobMappingId) {
        this.acuityJobMappingId = acuityJobMappingId;
    }

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
