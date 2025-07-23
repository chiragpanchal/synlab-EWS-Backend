package com.ewsv3.ews.rosters.dto.rosters;


import java.time.LocalDate;
import java.time.LocalDateTime;


public record PersonRostersSmall(

        long personRosterId,
        LocalDate effectiveDate,
        long personId,
        long workDurationId,
        LocalDateTime timeStart,
        LocalDateTime breakStart,
        LocalDateTime breakEnd,
        LocalDateTime timeEnd,
        long createdBy,
        LocalDateTime createdOn,
        long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        long departmentId,
        Long sectionId,
        Double wages,
        Long currencyId,
        String comments,
        long jobTitleId,
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
        String skill_1
//        String skill2,
//        String skill3
//        Long movingAssetId,
//        String perDiem,
//        Long patientId,
//        String apprStatus,
//        String dmlFlag,
//        String onCall,
//        String emergency
//        int objectVersionNumber,
//        String openShift,
//        Long skillId,
//        String workDurationCode,
//        String workDurationName,
//        String colorCode,
//        String categoryName,
//        String departmentName,
//        String jobTitle
) {


}
