package com.ewsv3.ews.schedulerules.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SC_SCHEDULE_RULES")
public class ScheduleRule {
    
    @Id
    @Column(name = "SCHEDULE_RULE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_rule_seq")
    @SequenceGenerator(name = "schedule_rule_seq", sequenceName = "SCHEDULE_RULE_ID_SQ", allocationSize = 1)
    private Long scheduleRuleId;
    
    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;
    
    @Column(name = "VALID_FROM")
    private LocalDate validFrom;
    
    @Column(name = "VALID_TO")
    private LocalDate validTo;
    
    @Column(name = "MAX_HRS_PER_DAY")
    private Double maxHrsPerDay;
    
    @Column(name = "MIN_HRS_PER_WEEK")
    private Double minHrsPerWeek;
    
    @Column(name = "MAX_HRS_PER_WEEK")
    private Double maxHrsPerWeek;
    
    @Column(name = "MIN_HRS_PER_MONTH")
    private Double minHrsPerMonth;
    
    @Column(name = "MAX_HRS_PER_MONTH")
    private Double maxHrsPerMonth;
    
    @Column(name = "SHIFT_GAP")
    private Double shiftGap;
    
    @Column(name = "MIN_REST_DAYS_PER_WEEK")
    private Integer minRestDaysPerWeek;
    
    @Column(name = "MAX_CONT_SHIFT_DAYS")
    private Integer maxContShiftDays;
    
    @Column(name = "MAX_CONT_REST_DAYS")
    private Integer maxContRestDays;
    
    @Column(name = "CREATED_BY")
    private Long createdBy;
    
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    
    @Column(name = "LAST_UPDATED_BY")
    private Long lastUpdatedBy;
    
    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;
    
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdOn = now;
        this.lastUpdateDate = now;
        // System.out.println("@PrePersist - createdBy: " + this.createdBy + ", lastUpdatedBy: " + this.lastUpdatedBy);
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateDate = LocalDateTime.now();
        // System.out.println("@PreUpdate - createdBy: " + this.createdBy + ", lastUpdatedBy: " + this.lastUpdatedBy);
    }

    public ScheduleRule() {
    }

    public ScheduleRule(Long profileId, LocalDate validFrom, LocalDate validTo, Double maxHrsPerDay,
                        Double minHrsPerWeek, Double maxHrsPerWeek, Double minHrsPerMonth,
                        Double maxHrsPerMonth, Double shiftGap, Integer minRestDaysPerWeek,
                       Integer maxContShiftDays, Integer maxContRestDays, Long createdBy, Long lastUpdatedBy) {
        this.profileId = profileId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.maxHrsPerDay = maxHrsPerDay;
        this.minHrsPerWeek = minHrsPerWeek;
        this.maxHrsPerWeek = maxHrsPerWeek;
        this.minHrsPerMonth = minHrsPerMonth;
        this.maxHrsPerMonth = maxHrsPerMonth;
        this.shiftGap = shiftGap;
        this.minRestDaysPerWeek = minRestDaysPerWeek;
        this.maxContShiftDays = maxContShiftDays;
        this.maxContRestDays = maxContRestDays;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getScheduleRuleId() {
        return scheduleRuleId;
    }

    public void setScheduleRuleId(Long scheduleRuleId) {
        this.scheduleRuleId = scheduleRuleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Double getMaxHrsPerDay() {
        return maxHrsPerDay;
    }

    public void setMaxHrsPerDay(Double maxHrsPerDay) {
        this.maxHrsPerDay = maxHrsPerDay;
    }

    public Double getMinHrsPerWeek() {
        return minHrsPerWeek;
    }

    public void setMinHrsPerWeek(Double minHrsPerWeek) {
        this.minHrsPerWeek = minHrsPerWeek;
    }

    public Double getMaxHrsPerWeek() {
        return maxHrsPerWeek;
    }

    public void setMaxHrsPerWeek(Double maxHrsPerWeek) {
        this.maxHrsPerWeek = maxHrsPerWeek;
    }

    public Double getMinHrsPerMonth() {
        return minHrsPerMonth;
    }

    public void setMinHrsPerMonth(Double minHrsPerMonth) {
        this.minHrsPerMonth = minHrsPerMonth;
    }

    public Double getMaxHrsPerMonth() {
        return maxHrsPerMonth;
    }

    public void setMaxHrsPerMonth(Double maxHrsPerMonth) {
        this.maxHrsPerMonth = maxHrsPerMonth;
    }

    public Double getShiftGap() {
        return shiftGap;
    }

    public void setShiftGap(Double shiftGap) {
        this.shiftGap = shiftGap;
    }

    public Integer getMinRestDaysPerWeek() {
        return minRestDaysPerWeek;
    }

    public void setMinRestDaysPerWeek(Integer minRestDaysPerWeek) {
        this.minRestDaysPerWeek = minRestDaysPerWeek;
    }

    public Integer getMaxContShiftDays() {
        return maxContShiftDays;
    }

    public void setMaxContShiftDays(Integer maxContShiftDays) {
        this.maxContShiftDays = maxContShiftDays;
    }

    public Integer getMaxContRestDays() {
        return maxContRestDays;
    }

    public void setMaxContRestDays(Integer maxContRestDays) {
        this.maxContRestDays = maxContRestDays;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}