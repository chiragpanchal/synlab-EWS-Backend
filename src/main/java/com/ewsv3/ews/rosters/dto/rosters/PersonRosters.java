package com.ewsv3.ews.rosters.dto.rosters;


import java.time.LocalDate;
import java.time.LocalDateTime;


public record PersonRosters(

        Long personRosterId,
        LocalDate effectiveDate,
        Long personId,
        Long workDurationId,
        LocalDateTime timeStart,
        LocalDateTime breakStart,
        LocalDateTime breakEnd,
        LocalDateTime timeEnd,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        Long departmentId,
        Long sectionId,
        Double wages,
        Long currencyId,
        String comments,
        Long jobTitleId,
        Long workLocationId,
        String published,
        Long managerId,
        Long workRotationId,
        Long projectId,
        Long taskId,
        String projectAddress,
        Long groupIdentifierKey,
        LocalDateTime personStartTime,
        Long crewId,
        String skill_1,
        String skill_2,
        String skill_3,
        Long movingAssetId,
        String perDiem,
        Long patientId,
        String apprStatus,
        String dmlFlag,
        String onCall,
        String emergency,
        int objectVersionNumber,
        String openShift,
        Long skillId,
        String workDurationCode,
        String workDurationName,
        String colorCode,
        String categoryName,
        String departmentName,
        String jobTitle,
        Double schHrs,
        String timeStartShort,
        String timeEndShort
) {


}
