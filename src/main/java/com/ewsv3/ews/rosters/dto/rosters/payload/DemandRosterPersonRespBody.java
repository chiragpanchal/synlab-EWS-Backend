package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DemandRosterPersonRespBody(
        Long personId,
        String personName,
        String employeeNumber,
        String gradeName,
        Double rate,
        LocalDate effectiveDate,
        String workDurationCode,
        LocalDateTime timeStart,
        LocalDateTime timeEnd
) {
}
