package com.ewsv3.ews.rosters.dto.rosters.payload;

public record PersonRosterPivotReq(
        long userId,
        String startDate,
        String endDate,
        int profileId,
        String personIds,
        String departmentIds,
        String jobTitleTds,
        String workLocationIds,
        String businessUnitIds,
        String legalEntityIds,
        String dutyManagerIds,
        String apprStatus,
        String filterBy,
        String kpiString
) {
}
