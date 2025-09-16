package com.ewsv3.ews.timesheets.dto.bulk;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BulkTimesheetDetails(
        Long ttsTimesheetId,
        Long personId,
        LocalDate effectiveDate,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Long payCodeId,
        Long departmentId,
        Long jobTitleId,
        Long projectId,
        Long taskId,
        Long expTypeId,
        Double regHrs,
        Long itemKey,
        String timeHour,
        Double allwValue,
        String comments,
        Long personRosterId,
        Long personUserId
) {
}
