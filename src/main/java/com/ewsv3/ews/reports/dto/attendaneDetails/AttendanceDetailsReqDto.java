package com.ewsv3.ews.reports.dto.attendaneDetails;

import java.time.LocalDate;

public record AttendanceDetailsReqDto(
        Long profileId,
        String text,
        LocalDate startDate,
        LocalDate endDate,
        Long departmentId,
        Long jobTitleId
) {
}
