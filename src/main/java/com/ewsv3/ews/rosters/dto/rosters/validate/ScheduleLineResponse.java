package com.ewsv3.ews.rosters.dto.rosters.validate;

import java.time.LocalDateTime;

public record ScheduleLineResponse(
        Long departmentId,
        Long jobTitleId,
        Long locationId,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Double scheduledFte
) {
}
