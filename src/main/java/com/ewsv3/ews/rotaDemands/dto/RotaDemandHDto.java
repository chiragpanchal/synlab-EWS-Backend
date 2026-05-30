package com.ewsv3.ews.rotaDemands.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class RotaDemandHDto {

    private Long rotaDemandId;
    private String demandName;
    private String isActive;
    private Long workRotationId;
    private Long profileId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastUpdatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;

    public RotaDemandHDto() {}

    public Long getRotaDemandId() {
        return rotaDemandId;
    }

    public void setRotaDemandId(Long rotaDemandId) {
        this.rotaDemandId = rotaDemandId;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Long getWorkRotationId() {
        return workRotationId;
    }

    public void setWorkRotationId(Long workRotationId) {
        this.workRotationId = workRotationId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
