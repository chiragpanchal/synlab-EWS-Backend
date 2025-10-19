package com.ewsv3.ews.shiftbidding.dto;

import java.time.LocalDate;

public class ScheduleBidPatternSkill {

    private Long scheduleBidPatternSkillId;
    private Long scheduleBidPatternId;
    private Long skillId;
    private Long createdBy;
    private LocalDate createdOn;
    private Long lastUpdatedBy;
    private LocalDate lastUpdateDate;

    public ScheduleBidPatternSkill() {
    }

    public ScheduleBidPatternSkill(Long scheduleBidPatternSkillId, Long scheduleBidPatternId,
                                  Long skillId, Long createdBy, LocalDate createdOn,
                                  Long lastUpdatedBy, LocalDate lastUpdateDate) {
        this.scheduleBidPatternSkillId = scheduleBidPatternSkillId;
        this.scheduleBidPatternId = scheduleBidPatternId;
        this.skillId = skillId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getScheduleBidPatternSkillId() {
        return scheduleBidPatternSkillId;
    }

    public void setScheduleBidPatternSkillId(Long scheduleBidPatternSkillId) {
        this.scheduleBidPatternSkillId = scheduleBidPatternSkillId;
    }

    public Long getScheduleBidPatternId() {
        return scheduleBidPatternId;
    }

    public void setScheduleBidPatternId(Long scheduleBidPatternId) {
        this.scheduleBidPatternId = scheduleBidPatternId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
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
        return "ScheduleBidPatternSkill{" +
                "scheduleBidPatternSkillId=" + scheduleBidPatternSkillId +
                ", scheduleBidPatternId=" + scheduleBidPatternId +
                ", skillId=" + skillId +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
