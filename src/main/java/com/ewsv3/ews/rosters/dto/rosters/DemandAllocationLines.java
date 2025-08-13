package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;

public record DemandAllocationLines(
        String employeeNumber,
        String fullName,
        Long person_id,
        Long demandTemplateLineId,
        LocalDate effectiveDate,
        Double rate,
        String allocFlag,
        String allocTime,
        Double schHrs
) {
}
