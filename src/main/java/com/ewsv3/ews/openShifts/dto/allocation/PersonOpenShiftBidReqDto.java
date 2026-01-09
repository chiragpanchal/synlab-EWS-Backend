package com.ewsv3.ews.openShifts.dto.allocation;

public record PersonOpenShiftBidReqDto(
        Long personOpenShiftId,
        Long personId,
        Long openShiftLineId,
        String sun,
        String mon,
        String tue,
        String wed,
        String thu,
        String fri,
        String sat

) {
}
