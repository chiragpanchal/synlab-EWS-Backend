package com.ewsv3.ews.reports.dto.reportMasters;

public record ReportJobDto(
        Long profileId,
        Long jobTitleId,
        String jobTitle
) {
}
