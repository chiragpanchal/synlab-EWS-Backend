package com.ewsv3.ews.setup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class LabEquipmentDto {

    private Long labEquipmentId;
    private String eqCode;
    private String eqName;
    private String isActive;
    private Long cleanupTime;
    private Long prepTime;
    private Long utilizeTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastUpdatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;

    public LabEquipmentDto() {}

    public Long getLabEquipmentId() {
        return labEquipmentId;
    }

    public void setLabEquipmentId(Long labEquipmentId) {
        this.labEquipmentId = labEquipmentId;
    }

    public String getEqCode() {
        return eqCode;
    }

    public void setEqCode(String eqCode) {
        this.eqCode = eqCode;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Long getCleanupTime() {
        return cleanupTime;
    }

    public void setCleanupTime(Long cleanupTime) {
        this.cleanupTime = cleanupTime;
    }

    public Long getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Long prepTime) {
        this.prepTime = prepTime;
    }

    public Long getUtilizeTime() {
        return utilizeTime;
    }

    public void setUtilizeTime(Long utilizeTime) {
        this.utilizeTime = utilizeTime;
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
