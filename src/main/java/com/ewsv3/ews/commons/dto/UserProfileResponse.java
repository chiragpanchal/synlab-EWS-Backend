package com.ewsv3.ews.commons.dto;

public record UserProfileResponse(
        Long userId,
        String userName,
        String userPersonName,
        String userEmployeeNumber,
        String jobTitle,
        String departmentName
) {
}
