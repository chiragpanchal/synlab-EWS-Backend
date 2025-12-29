package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OpenShiftsHeader {
    Long openShiftId;
    LocalDate startDate;
    LocalDate endDate;
    Long demandTemplateId;
    Long profileId;
    String recalled;
    Long createdBy;
    LocalDateTime createdOn;
    Long lastUpdatedBy;
    LocalDateTime lastUpdateDate;
    List<OpenShiftLines> openShiftLines;

    public OpenShiftsHeader() {
    }

    public OpenShiftsHeader(Long openShiftId, LocalDate startDaate, LocalDate endDate, Long demandTemplateId, Long profileId, String recalled, Long createdBy, LocalDateTime createdOn, Long lastUpdatedBy, LocalDateTime lastUpdateDate) {
        this.openShiftId = openShiftId;
        this.startDate = startDaate;
        this.endDate = endDate;
        this.demandTemplateId = demandTemplateId;
        this.profileId = profileId;
        this.recalled = recalled;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getOpenShiftId() {
        return openShiftId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getDemandTemplateId() {
        return demandTemplateId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getRecalled() {
        return recalled;
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

    public List<OpenShiftLines> getOpenShiftLines() {
        return openShiftLines;
    }

    public void setOpenShiftLines(List<OpenShiftLines> openShiftLines) {
        this.openShiftLines = openShiftLines;
    }
}
