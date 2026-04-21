package com.ewsv3.ews.setup.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_PERSON_PREFERRED_PATTERNS")
public class PersonPreferredPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_PREFERRED_PATTERN_ID")
    private Long personPreferredPatternId;

    @Column(name = "PERSON_ID", nullable = false)
    private Long personId;

    @Column(name = "WORK_ROTATION_ID", nullable = false)
    private Long workRotationId;

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

    public PersonPreferredPattern() {}

    public PersonPreferredPattern(Long personPreferredPatternId, Long personId, Long workRotationId,
                                   Long createdBy, Date createdOn, Long lastUpdatedBy, Date lastUpdateDate) {
        this.personPreferredPatternId = personPreferredPatternId;
        this.personId = personId;
        this.workRotationId = workRotationId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

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
