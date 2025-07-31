package com.ewsv3.ews.rosters.dto.rosters;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record RosterLinesChild(
        Long personId,
        Long assignmentId,
        Long personRosterId,
        LocalDate effectiveDate,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Double schHrs,
        Long schDepartmentId,
        Long schJobTitleId,
        Long schWorkLocationId,
        String schDepartment,
        String schJobTitle,
        String schLocation,
        String onCall,
        String emergency,
        String published,
        Long workDurationId,
        String workDurationCode,
        String workDurationName,
        Long schCost,
        String currencyCode
) {

    public RosterLinesChild setEffectiveDate(LocalDate effectiveDate) {
        return new RosterLinesChild(
                this.personId,
                this.assignmentId,
                this.personRosterId,
                effectiveDate,
                this.timeStart,
                this.timeEnd,
                this.schHrs,
                this.schDepartmentId,
                this.schJobTitleId,
                this.schWorkLocationId,
                this.schDepartment,
                this.schJobTitle,
                this.schLocation,
                this.onCall,
                this.emergency,
                this.published,
                this.workDurationId,
                this.workDurationCode,
                this.workDurationName,
                this.schCost,
                this.currencyCode
        );
    }

}
