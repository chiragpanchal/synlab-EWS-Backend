package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDate;

public record OpenShiftProfileDatesReqDto(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate
) {
}
