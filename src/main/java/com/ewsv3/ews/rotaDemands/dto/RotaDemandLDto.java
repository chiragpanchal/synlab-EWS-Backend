package com.ewsv3.ews.rotaDemands.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class RotaDemandLDto {

    private Long rotaDemandLineId;
    private Long rotaDemandId;
    private String lineName;
    private Long workRotationLineId;
    private Long seq;
    private Long departmentId;
    private Long jobTitleId;
    private Double fteReq;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastUpdatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;

    public RotaDemandLDto() {}

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
