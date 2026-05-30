package com.ewsv3.ews.rotaDemands.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_ROTA_DEMANDS_L")
public class RotaDemandL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROTA_DAMAND_LINE_ID")
    private Long rotaDemandLineId;

    @Column(name = "ROTA_DEMAND_ID", nullable = false)
    private Long rotaDemandId;

    @Column(name = "LINE_NAME", nullable = false, length = 1000)
    private String lineName;

    @Column(name = "WORK_ROTATION_LINE_ID", nullable = false)
    private Long workRotationLineId;

    @Column(name = "SEQ", nullable = false)
    private Long seq;

    @Column(name = "DEPARTMENT_ID", nullable = true)
    private Long departmentId;

    @Column(name = "JOB_TITLE_ID", nullable = false)
    private Long jobTitleId;

    @Column(name = "FTE_REQ", nullable = false)
    private Double fteReq;

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

    public RotaDemandL() {}

    public Long getRotaDemandLineId() {
        return rotaDemandLineId;
    }

    public void setRotaDemandLineId(Long rotaDemandLineId) {
        this.rotaDemandLineId = rotaDemandLineId;
    }

    public Long getRotaDemandId() {
        return rotaDemandId;
    }

    public void setRotaDemandId(Long rotaDemandId) {
        this.rotaDemandId = rotaDemandId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Long getWorkRotationLineId() {
        return workRotationLineId;
    }

    public void setWorkRotationLineId(Long workRotationLineId) {
        this.workRotationLineId = workRotationLineId;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Double getFteReq() {
        return fteReq;
    }

    public void setFteReq(Double fteReq) {
        this.fteReq = fteReq;
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
