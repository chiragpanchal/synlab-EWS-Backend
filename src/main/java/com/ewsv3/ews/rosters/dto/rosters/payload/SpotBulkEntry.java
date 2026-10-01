package com.ewsv3.ews.rosters.dto.rosters.payload;

/**
 * One roster entry for {@code POST /api/roster/spot-bulk}. Mirrors the parameters of
 * SC_CREATE_SPOT_ROSTER_P; field names match the JSON read by SC_CREATE_SPOT_ROSTER_BULK_P.
 *
 * @param ref       caller-chosen id, echoed back in the matching {@link SpotBulkResult}
 * @param startDate yyyy-MM-dd
 * @param endDate   yyyy-MM-dd
 */
public record SpotBulkEntry(
        Integer ref,
        String startDate,
        String endDate,
        Long personId,
        Long assignmentId,
        Long jobTitleId,
        Long departmentId,
        Long workLocationId,
        Long workDurationId,
        String onCall,
        String emergency,
        String sun,
        String mon,
        String tue,
        String wed,
        String thu,
        String fri,
        String sat,
        Long personRosterId) {
}
