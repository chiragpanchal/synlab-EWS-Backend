package com.ewsv3.ews.reports.dto.reportMasters;

import org.springframework.cglib.core.Local;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDate;

public record ReportPersonReqDto(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        String text,
        Long departmentId,
        Long jobTitleId
) {
}
