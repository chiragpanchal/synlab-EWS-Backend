package com.ewsv3.ews.request.dto;

public record RequestMaster(
        Long requestMasterId,
        String requestName,
        String enabled,
        String punchException,
        String timeType,
        String reqCode,
        String multipleDayAllowed,
        Long requestReasonId,
        String reason,
        Long requestCriteriaId,
        Long jobId,
        Long departmentId,
        Long positionId,
        Long workLocationId,
        Long employeeTypeId,
        Long businessUnitId,
        Long payrollId,
        String jobFamily,
        Long legalEntityId,
        String gender,
        String nationality,
        String religion,
        Long gradeId
) {
}
