package com.ewsv3.ews.setup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class LabEquipmentJobDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long labEquipmentsJobId;

    private Long labEquipmentId;

    private Long jobTitleId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String eqName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String jobTitle;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastUpdatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;

    public LabEquipmentJobDto() {}

    public Long getLabEquipmentsJobId() {
        return labEquipmentsJobId;
    }

    public void setLabEquipmentsJobId(Long labEquipmentsJobId) {
        this.labEquipmentsJobId = labEquipmentsJobId;
    }

    public Long getLabEquipmentId() {
        return labEquipmentId;
    }

    public void setLabEquipmentId(Long labEquipmentId) {
        this.labEquipmentId = labEquipmentId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
