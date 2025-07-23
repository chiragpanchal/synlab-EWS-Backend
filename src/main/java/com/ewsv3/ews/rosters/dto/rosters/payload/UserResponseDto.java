package com.ewsv3.ews.rosters.dto.rosters.payload;


public record UserResponseDto(

        long userId,
        String userName,
        long personId,
        String fullName,
        String employeeNumber,
        String jobTitle,
        String departmentName,
        long assignmentId) {



}
