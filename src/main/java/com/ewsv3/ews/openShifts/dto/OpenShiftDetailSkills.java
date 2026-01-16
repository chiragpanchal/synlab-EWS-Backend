package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDateTime;

public class OpenShiftDetailSkills {
    Long openShiftsSkillId;
    Long openShiftLineId;
    Long skillId;
    Long createdBy;
    LocalDateTime createdOn;
    Long lastUpdatedBy;
    LocalDateTime lastUpdateDate;

    public OpenShiftDetailSkills(Long openShiftsSkillId) {
        this.openShiftsSkillId = openShiftsSkillId;
    }


    public OpenShiftDetailSkills() {
    }

    public OpenShiftDetailSkills(Long openShiftsSkillId, Long openShiftLineId, Long skillId, Long createdBy, LocalDateTime createdOn, Long lastUpdatedBy, LocalDateTime lastUpdateDate) {
        this.openShiftsSkillId = openShiftsSkillId;
        this.openShiftLineId = openShiftLineId;
        this.skillId = skillId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getOpenShiftsSkillId() {
        return openShiftsSkillId;
    }

    public Long getOpenShiftLineId() {
        return openShiftLineId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }
}
