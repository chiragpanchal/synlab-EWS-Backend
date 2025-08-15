package com.ewsv3.ews.commons.dto.masters;

public record PersonDtoLov(
        Long userId,
        Long personId,
        String fullName,
        String employeeNumber,
        String jobTitle
) {
}
