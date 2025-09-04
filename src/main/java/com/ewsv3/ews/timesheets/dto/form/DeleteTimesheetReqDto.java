package com.ewsv3.ews.timesheets.dto.form;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public record DeleteTimesheetReqDto(
        Long personId,
        Long ttsTimesheetId,
        LocalDate startDate,
        Local endDate,
        String deleteComments
) {
}
