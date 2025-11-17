package com.ewsv3.ews.reports.dto;

import java.time.LocalDate;

public record UserProfileDateReqDto(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate
) {
}
