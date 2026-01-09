package com.ewsv3.ews.openShifts.dto.allocation;

import java.time.LocalDateTime;

public record PersonSelfApplicationsDto(
        Long personOpenShiftId,
        Long openShiftLineId,
        Long personId,
        String sun,
        String mon,
        String tue,
        String wed,
        String thu,
        String fri,
        String sat,
        LocalDateTime createdOn,
        String  createdByUserName,
        LocalDateTime lastUpdateDate,
        String  updatedByUserName
) {
}
