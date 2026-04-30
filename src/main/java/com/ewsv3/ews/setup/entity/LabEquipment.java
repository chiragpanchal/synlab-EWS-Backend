package com.ewsv3.ews.setup.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_LAB_EQUIPMENTS_H")
public class LabEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAB_EQUIPMENT_ID")
    private Long labEquipmentId;

    @Column(name = "EQ_CODE", nullable = false, unique = true, length = 1000)
    private String eqCode;

    @Column(name = "EQ_NAME", nullable = false, unique = true, length = 1000)
    private String eqName;

    @Column(name = "IS_ACTIVE", length = 1)
    private String isActive;

    @Column(name = "CLEANUP_TIME")
    private Long cleanupTime;

    @Column(name = "PREP_TIME")
    private Long prepTime;

    @Column(name = "UTILIZE_TIME")
    private Long utilizeTime;

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

    public LabEquipment() {}

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
