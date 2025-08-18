package com.ewsv3.ews.accessprofiles.dto;

import java.time.LocalDateTime;

public record AccessProfileLines(
        Long accessProfileLineId,
        Long profileId,
        Long personId,
        Long jobId,
        Long departmentId,
        Long gradeId,
        String employeeTypeId,
        String includeExcludeFlag,
        Long businessUnitId,
        String jobFamily,
        Long legalEntityId,
        String gender,
        String nationality,
        String religion,
        Long subDepartmentId,
        String employeeCatg,
        String shiftType,
        Long projectId,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate
) {
}
