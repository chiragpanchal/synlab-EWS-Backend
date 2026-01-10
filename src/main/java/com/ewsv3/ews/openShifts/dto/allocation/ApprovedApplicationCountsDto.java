package com.ewsv3.ews.openShifts.dto.allocation;

public record ApprovedApplicationCountsDto(
        Long openShiftLineId,
        Integer sunApprCnt,
        Integer monApprCnt,
        Integer tueApprCnt,
        Integer wedApprCnt,
        Integer thuApprCnt,
        Integer friApprCnt,
        Integer satApprCnt
) {
}
