package com.ewsv3.ews.shiftbidding.dto;

import java.time.LocalDate;

public class BiddingPersonPref {

    private Long biddingPersonPrefId;
    private Long biddingGroupId;
    private Long personId;
    private Long priorityLevel;
    private Long lastUniqueBidsCheck;
    private Long createdBy;
    private LocalDate createdOn;
    private Long lastUpdatedBy;
    private LocalDate lastUpdateDate;

    public BiddingPersonPref() {
    }

    public BiddingPersonPref(Long biddingPersonPrefId, Long biddingGroupId, Long personId,
                            Long priorityLevel, Long lastUniqueBidsCheck, Long createdBy,
                            LocalDate createdOn, Long lastUpdatedBy, LocalDate lastUpdateDate) {
        this.biddingPersonPrefId = biddingPersonPrefId;
        this.biddingGroupId = biddingGroupId;
        this.personId = personId;
        this.priorityLevel = priorityLevel;
        this.lastUniqueBidsCheck = lastUniqueBidsCheck;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getBiddingPersonPrefId() {
        return biddingPersonPrefId;
    }

    public void setBiddingPersonPrefId(Long biddingPersonPrefId) {
        this.biddingPersonPrefId = biddingPersonPrefId;
    }

    public Long getBiddingGroupId() {
        return biddingGroupId;
    }

    public void setBiddingGroupId(Long biddingGroupId) {
        this.biddingGroupId = biddingGroupId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Long priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Long getLastUniqueBidsCheck() {
        return lastUniqueBidsCheck;
    }

    public void setLastUniqueBidsCheck(Long lastUniqueBidsCheck) {
        this.lastUniqueBidsCheck = lastUniqueBidsCheck;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "BiddingPersonPref{" +
                "biddingPersonPrefId=" + biddingPersonPrefId +
                ", biddingGroupId=" + biddingGroupId +
                ", personId=" + personId +
                ", priorityLevel=" + priorityLevel +
                ", lastUniqueBidsCheck=" + lastUniqueBidsCheck +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
