package com.ewsv3.ews.timesheets.dto.form;

public record TsJobDto(

        Long jobTitleId,
        String jobTitle,
        Double perHrSal,
        String currencyCode,
        Long payCodeId
) {
}
