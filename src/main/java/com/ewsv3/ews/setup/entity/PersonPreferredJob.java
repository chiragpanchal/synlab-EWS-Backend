package com.ewsv3.ews.setup.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SC_PERSON_PREFERRED_JOBS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PERSON_ID", "JOB_TITLE_ID"})
})
public class PersonPreferredJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_PREFERRED_JOB_ID")
    private Long personPreferredJobId;

    @Column(name = "JOB_TITLE_ID")
    private Long jobTitleId;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "LAST_UPDATED_BY")
    private Long lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "PERSON_ID")
    private Long personId;

    @Column(name = "PER_HR_SAL")
    private BigDecimal perHrSal;

    @Column(name = "CURRENCY_ID")
    private Long currencyId;

    @Column(name = "PAY_CODE_ID")
    private Long payCodeId;

    // Default constructor
    public PersonPreferredJob() {}

    // Constructor with all fields
    public PersonPreferredJob(Long personPreferredJobId, Long jobTitleId, Long createdBy, Date createdOn,
                             Long lastUpdatedBy, Date lastUpdateDate, Long personId, BigDecimal perHrSal,
                             Long currencyId, Long payCodeId) {
        this.personPreferredJobId = personPreferredJobId;
        this.jobTitleId = jobTitleId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
        this.personId = personId;
        this.perHrSal = perHrSal;
        this.currencyId = currencyId;
        this.payCodeId = payCodeId;
    }

    // Getters and Setters
    public Long getPersonPreferredJobId() {
        return personPreferredJobId;
    }

    public void setPersonPreferredJobId(Long personPreferredJobId) {
        this.personPreferredJobId = personPreferredJobId;
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

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public BigDecimal getPerHrSal() {
        return perHrSal;
    }

    public void setPerHrSal(BigDecimal perHrSal) {
        this.perHrSal = perHrSal;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getPayCodeId() {
        return payCodeId;
    }

    public void setPayCodeId(Long payCodeId) {
        this.payCodeId = payCodeId;
    }
}
