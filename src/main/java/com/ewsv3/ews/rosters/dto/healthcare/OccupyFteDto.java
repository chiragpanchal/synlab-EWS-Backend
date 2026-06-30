package com.ewsv3.ews.rosters.dto.healthcare;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OccupyFteDto {
    private Long profileId;
    private LocalDate effectiveDate;
    private Integer occupiedBeds;
    private Long jobTitleId;
    private Long workDurationId;
    private String shift;
    private String jobTitle;
    private BigDecimal reqFte;

    public OccupyFteDto() {}

    public OccupyFteDto(Long profileId, LocalDate effectiveDate, Integer occupiedBeds,
                        Long jobTitleId, Long workDurationId, String shift,
                        String jobTitle, BigDecimal reqFte) {
        this.profileId = profileId;
        this.effectiveDate = effectiveDate;
        this.occupiedBeds = occupiedBeds;
        this.jobTitleId = jobTitleId;
        this.workDurationId = workDurationId;
        this.shift = shift;
        this.jobTitle = jobTitle;
        this.reqFte = reqFte;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(Integer occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
