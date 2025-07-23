package com.ewsv3.ews.request.dto;

public record DestinationRosterResponseBody(
        Long personId,
        String employeeNumber,
        String fullName,
        String timeStart,
        String timeEnd,
        Long personRosterId
) {
}
