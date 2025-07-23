package com.ewsv3.ews.rosters.dto.rosters.payload;

public record RotaCreationReqBody(
        Long workRotationId,
        String rotaStartDate,
        String rotaEndDate,
        Integer lineSeq,
        Long departmentId,
        Long jobTitleId,
        Long workLocationId,
        String mode,
        String persons) {

}
