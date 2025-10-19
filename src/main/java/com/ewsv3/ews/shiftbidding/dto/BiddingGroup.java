package com.ewsv3.ews.shiftbidding.dto;

import java.time.LocalDate;
import java.util.List;

public class BiddingGroup {

    private Long biddingGroupId;
    private Long profileId;
    private Long seq;
    private Long openDays;
    private Long lastUniqueBidsCheck;
    private String bidPref;
    private Long createdBy;
    private LocalDate createdOn;
    private Long lastUpdatedBy;
    private LocalDate lastUpdateDate;
    private List<BiddingPersonPref> personPreferences;

    public BiddingGroup() {
    }

    public BiddingGroup(Long biddingGroupId, Long profileId, Long seq, Long openDays,
                       Long lastUniqueBidsCheck, String bidPref, Long createdBy,
                       LocalDate createdOn, Long lastUpdatedBy, LocalDate lastUpdateDate) {
        this.biddingGroupId = biddingGroupId;
        this.profileId = profileId;
        this.seq = seq;
        this.openDays = openDays;
        this.lastUniqueBidsCheck = lastUniqueBidsCheck;
        this.bidPref = bidPref;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getBiddingGroupId() {
        return biddingGroupId;
    }

    public void setBiddingGroupId(Long biddingGroupId) {
        this.biddingGroupId = biddingGroupId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getOpenDays() {
        return openDays;
    }

    public void setOpenDays(Long openDays) {
        this.openDays = openDays;
    }

    public Long getLastUniqueBidsCheck() {
        return lastUniqueBidsCheck;
    }

    public void setLastUniqueBidsCheck(Long lastUniqueBidsCheck) {
        this.lastUniqueBidsCheck = lastUniqueBidsCheck;
    }

    public String getBidPref() {
        return bidPref;
    }

    public void setBidPref(String bidPref) {
        this.bidPref = bidPref;
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

    public List<BiddingPersonPref> getPersonPreferences() {
        return personPreferences;
    }

    public void setPersonPreferences(List<BiddingPersonPref> personPreferences) {
        this.personPreferences = personPreferences;
    }

    @Override
    public String toString() {
        return "BiddingGroup{" +
                "biddingGroupId=" + biddingGroupId +
                ", profileId=" + profileId +
                ", seq=" + seq +
                ", openDays=" + openDays +
                ", lastUniqueBidsCheck=" + lastUniqueBidsCheck +
                ", bidPref='" + bidPref + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdateDate=" + lastUpdateDate +
                ", personPreferences=" + personPreferences +
                '}';
    }
}
