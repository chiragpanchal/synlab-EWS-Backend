package com.ewsv3.ews.setup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class LabEquipmentJobSkillDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long labEquipmentsJobSkillId;

    private Long labEquipmentsJobId;

    private Long skillId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String skillName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastUpdatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;

    public LabEquipmentJobSkillDto() {}

    public Long getLabEquipmentsJobSkillId() {
        return labEquipmentsJobSkillId;
    }

    public void setLabEquipmentsJobSkillId(Long labEquipmentsJobSkillId) {
        this.labEquipmentsJobSkillId = labEquipmentsJobSkillId;
    }

    public Long getLabEquipmentsJobId() {
        return labEquipmentsJobId;
    }

    public void setLabEquipmentsJobId(Long labEquipmentsJobId) {
        this.labEquipmentsJobId = labEquipmentsJobId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
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
