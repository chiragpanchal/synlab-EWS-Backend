package com.ewsv3.ews.setup.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_LAB_EQUIPMENTS_JOB_SKILLS")
public class LabEquipmentJobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAB_EQUIPMENTS_JOB_SKILL_ID")
    private Long labEquipmentsJobSkillId;

    @Column(name = "LAB_EQUIPMENTS_JOB_ID", nullable = false)
    private Long labEquipmentsJobId;

    @Column(name = "SKILL_ID", nullable = false)
    private Long skillId;

    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "LAST_UPDATED_BY", nullable = false)
    private Long lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    public LabEquipmentJobSkill() {}

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
