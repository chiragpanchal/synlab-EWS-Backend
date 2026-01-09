package com.ewsv3.ews.openShifts.dto.allocation;

public record TotalApplictionCountsDto(
        Long openShiftLineId,
        Integer sun,
        Integer mon,
        Integer tue,
        Integer wed,
        Integer thu,
        Integer fri,
        Integer sat
) {
}
