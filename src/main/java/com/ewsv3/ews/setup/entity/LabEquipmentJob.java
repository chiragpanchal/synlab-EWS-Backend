package com.ewsv3.ews.setup.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_LAB_EQUIPMENTS_JOBS")
public class LabEquipmentJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAB_EQUIPMENTS_JOB_ID")
    private Long labEquipmentsJobId;

    @Column(name = "LAB_EQUIPMENT_ID", nullable = false)
    private Long labEquipmentId;

    @Column(name = "JOB_TITLE_ID", nullable = false)
    private Long jobTitleId;

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

    public LabEquipmentJob() {}

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
