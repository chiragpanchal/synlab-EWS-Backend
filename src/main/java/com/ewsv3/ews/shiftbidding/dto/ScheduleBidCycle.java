package com.ewsv3.ews.shiftbidding.dto;

import java.time.LocalDate;
import java.util.List;

public class ScheduleBidCycle {

    private Long scheduleBidCycleId;
    private Long profileId;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private Long bidDuration;
    private Long bidOpenDays;
    private Long createdBy;
    private LocalDate createdOn;
    private Long lastUpdatedBy;
    private LocalDate lastUpdateDate;
    private List<ScheduleBidPattern> patterns;

    public ScheduleBidCycle() {
    }

    public ScheduleBidCycle(Long scheduleBidCycleId, Long profileId, LocalDate validityStartDate,
                           LocalDate validityEndDate, Long bidDuration, Long bidOpenDays,
                           Long createdBy, LocalDate createdOn, Long lastUpdatedBy,
                           LocalDate lastUpdateDate) {
        this.scheduleBidCycleId = scheduleBidCycleId;
        this.profileId = profileId;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
        this.bidDuration = bidDuration;
        this.bidOpenDays = bidOpenDays;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getScheduleBidCycleId() {
        return scheduleBidCycleId;
    }

    public void setScheduleBidCycleId(Long scheduleBidCycleId) {
        this.scheduleBidCycleId = scheduleBidCycleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public LocalDate getValidityStartDate() {
        return validityStartDate;
    }

    public void setValidityStartDate(LocalDate validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public LocalDate getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(LocalDate validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public Long getBidDuration() {
        return bidDuration;
    }

    public void setBidDuration(Long bidDuration) {
        this.bidDuration = bidDuration;
    }

    public Long getBidOpenDays() {
        return bidOpenDays;
    }

    public void setBidOpenDays(Long bidOpenDays) {
        this.bidOpenDays = bidOpenDays;
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

    public List<ScheduleBidPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<ScheduleBidPattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        return "ScheduleBidCycle{" +
                "scheduleBidCycleId=" + scheduleBidCycleId +
                ", profileId=" + profileId +
                ", validityStartDate=" + validityStartDate +
                ", validityEndDate=" + validityEndDate +
                ", bidDuration=" + bidDuration +
                ", bidOpenDays=" + bidOpenDays +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdateDate=" + lastUpdateDate +
                ", patterns=" + patterns +
                '}';
    }
}
