package com.ewsv3.ews.timesheets.dto;

public record TimesheetPerson(
        Long personId,
        Long assignmentId,
        String employeeNumber,
        String assignmentNumber,
        String personName,
        String departmentName,
        String jobTitle,
        String gradeName,
        Long personUserId
) {
}
