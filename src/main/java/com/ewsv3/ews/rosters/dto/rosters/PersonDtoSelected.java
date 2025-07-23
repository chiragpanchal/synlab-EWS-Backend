package com.ewsv3.ews.rosters.dto.rosters;

public record PersonDtoSelected(
        long personId,
        long assignmentId,
        String employeeNumber,
        String assignmentNumber,
        String personName,
        long departmentId,
        long jobTitleId,
        long locationId,
        String emergencyType,
        String onCallType) {
}
