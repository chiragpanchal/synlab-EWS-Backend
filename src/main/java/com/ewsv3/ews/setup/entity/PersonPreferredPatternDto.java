package com.ewsv3.ews.setup.entity;

import java.util.Date;

public class PersonPreferredPatternDto {

    private Long personPreferredPatternId;
    private Long personId;
    private Long workRotationId;
    private String workRotationName;
    private Long createdBy;
    private Date createdOn;
    private Long lastUpdatedBy;
    private Date lastUpdateDate;

    public PersonPreferredPatternDto() {}

    public Long getPersonPreferredPatternId() {
        return personPreferredPatternId;
    }

    public void setPersonPreferredPatternId(Long personPreferredPatternId) {
        this.personPreferredPatternId = personPreferredPatternId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getWorkRotationId() {
        return workRotationId;
    }

    public void setWorkRotationId(Long workRotationId) {
        this.workRotationId = workRotationId;
    }

    public String getWorkRotationName() {
        return workRotationName;
    }

    public void setWorkRotationName(String workRotationName) {
        this.workRotationName = workRotationName;
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
