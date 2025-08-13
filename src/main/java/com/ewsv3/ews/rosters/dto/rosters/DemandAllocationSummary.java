package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;
import java.util.List;

public class DemandAllocationSummary {
    Long profileId;
    Long demandTemplateLineId;
    LocalDate effectiveDate;
    Double reqFte;
    Double allocFte;
    Long workDurationId;
    List<DemandAllocationLines> DemandAllocationLines;

    public DemandAllocationSummary() {
    }


    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getDemandTemplateLineId() {
        return demandTemplateLineId;
    }

    public void setDemandTemplateLineId(Long demandTemplateLineId) {
        this.demandTemplateLineId = demandTemplateLineId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getReqFte() {
        return reqFte;
    }

    public void setReqFte(Double reqFte) {
        this.reqFte = reqFte;
    }

    public Double getAllocFte() {
        return allocFte;
    }

    public void setAllocFte(Double allocFte) {
        this.allocFte = allocFte;
    }

    public Long getWorkDurationId() {
        return workDurationId;
    }

    public void setWorkDurationId(Long workDurationId) {
        this.workDurationId = workDurationId;
    }

    public List<DemandAllocationLines> getDemandAllocationLines() {
        return DemandAllocationLines;
    }

    public void setDemandAllocationLines(List<DemandAllocationLines> demandAllocationLines) {
        DemandAllocationLines = demandAllocationLines;
    }
}
