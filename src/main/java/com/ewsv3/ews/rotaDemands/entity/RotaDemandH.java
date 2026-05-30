package com.ewsv3.ews.rotaDemands.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_ROTA_DEMANDS_H")
public class RotaDemandH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROTA_DEMAND_ID")
    private Long rotaDemandId;

    @Column(name = "DEMAND_NAME", nullable = false, unique = true, length = 1000)
    private String demandName;

    @Column(name = "IS_ACTIVE", length = 1)
    private String isActive;

    @Column(name = "WORK_ROTATION_ID", nullable = false)
    private Long workRotationId;

    @Column(name = "PROFILE_ID")
    private Long profileId;

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

    public RotaDemandH() {}

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
