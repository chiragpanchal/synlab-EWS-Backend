package com.ewsv3.ews.shiftbidding.dto;

import java.time.LocalDate;
import java.util.List;

public class ScheduleBidPattern {

    private Long scheduleBidPatternId;
    private Long scheduleBidCycleId;
    private Long departmentId;
    private Long jobTitleId;
    private Long d1;
    private Long d2;
    private Long d3;
    private Long d4;
    private Long d5;
    private Long d6;
    private Long d7;
    private Long createdBy;
    private LocalDate createdOn;
    private Long lastUpdatedBy;
    private LocalDate lastUpdateDate;
    private List<ScheduleBidPatternSkill> skills;

    public ScheduleBidPattern() {
    }

    public ScheduleBidPattern(Long scheduleBidPatternId, Long scheduleBidCycleId, Long departmentId,
                             Long jobTitleId, Long d1, Long d2, Long d3, Long d4, Long d5, Long d6,
                             Long d7, Long createdBy, LocalDate createdOn, Long lastUpdatedBy,
                             LocalDate lastUpdateDate) {
        this.scheduleBidPatternId = scheduleBidPatternId;
        this.scheduleBidCycleId = scheduleBidCycleId;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.d4 = d4;
        this.d5 = d5;
        this.d6 = d6;
        this.d7 = d7;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getScheduleBidPatternId() {
        return scheduleBidPatternId;
    }

    public void setScheduleBidPatternId(Long scheduleBidPatternId) {
        this.scheduleBidPatternId = scheduleBidPatternId;
    }

    public Long getScheduleBidCycleId() {
        return scheduleBidCycleId;
    }

    public void setScheduleBidCycleId(Long scheduleBidCycleId) {
        this.scheduleBidCycleId = scheduleBidCycleId;
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

    public Long getD1() {
        return d1;
    }

    public void setD1(Long d1) {
        this.d1 = d1;
    }

    public Long getD2() {
        return d2;
    }

    public void setD2(Long d2) {
        this.d2 = d2;
    }

    public Long getD3() {
        return d3;
    }

    public void setD3(Long d3) {
        this.d3 = d3;
    }

    public Long getD4() {
        return d4;
    }

    public void setD4(Long d4) {
        this.d4 = d4;
    }

    public Long getD5() {
        return d5;
    }

    public void setD5(Long d5) {
        this.d5 = d5;
    }

    public Long getD6() {
        return d6;
    }

    public void setD6(Long d6) {
        this.d6 = d6;
    }

    public Long getD7() {
        return d7;
    }

    public void setD7(Long d7) {
        this.d7 = d7;
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

    public List<ScheduleBidPatternSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<ScheduleBidPatternSkill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "ScheduleBidPattern{" +
                "scheduleBidPatternId=" + scheduleBidPatternId +
                ", scheduleBidCycleId=" + scheduleBidCycleId +
                ", departmentId=" + departmentId +
                ", jobTitleId=" + jobTitleId +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", d3=" + d3 +
                ", d4=" + d4 +
                ", d5=" + d5 +
                ", d6=" + d6 +
                ", d7=" + d7 +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdateDate=" + lastUpdateDate +
                ", skills=" + skills +
                '}';
    }
}
