package com.ewsv3.ews.rosters.dto.rosters.validate;

public record DemandLineResponse(
        Long departmentId,
        Long jobTitleId,
        Long locationId,
        String timeStart,
        String timeEnd,
        Double sun,
        Double mon,
        Double tue,
        Double wed,
        Double thu,
        Double fri,
        Double sat

) {
}
