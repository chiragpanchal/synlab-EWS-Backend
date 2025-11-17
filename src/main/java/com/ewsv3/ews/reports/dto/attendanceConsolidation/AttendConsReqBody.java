package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendConsReqBody(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate
) {
}
