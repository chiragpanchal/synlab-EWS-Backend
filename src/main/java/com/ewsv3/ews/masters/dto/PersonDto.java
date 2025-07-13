package com.ewsv3.ews.masters.dto;

import java.time.LocalDate;

public record PersonDto(

        Long personId,
        Long userId,
        Long assignmentId,
        String employeeNumber,
        String assignmentNumber,
        String personName,
        String departmentName,
        Long departmentId,
        String jobTitle,
        Long jobTitleId,
        String locationName,
        Long workLocationId,
        String shiftType,
        String band,
        String emailAddress,
        Long positionId,
        String positionName,
        LocalDate hireDate,
        LocalDate terminationDate) {

}
