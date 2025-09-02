package com.ewsv3.ews.timesheets.dto.form;


import java.time.LocalDate;

public record TimesheetDetailsReqDto(
        Long personId,
        LocalDate startDate,
        LocalDate endDate
) {
}
