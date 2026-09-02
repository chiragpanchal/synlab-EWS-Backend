package com.ewsv3.ews.workrotations.dto;

public record ShiftColorConfigRequestBody(
        Double startTime,
        Double endTime,
        String color
) {
}
