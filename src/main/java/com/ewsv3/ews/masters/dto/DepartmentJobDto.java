package com.ewsv3.ews.masters.dto;

public record DepartmentJobDto(
        long departmentId,
        String departmentName,
        long jobTitleId,
        String jobTitle,
        long positionId,
        String positionName) {
}
