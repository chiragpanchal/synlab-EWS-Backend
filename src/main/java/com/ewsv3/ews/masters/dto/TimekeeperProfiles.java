package com.ewsv3.ews.masters.dto;


public record TimekeeperProfiles(
        int profileId,
        String profileName,
        String userType,
        String readOnly,
        String allowOverlapShifts,
        String allowOvertimeShifts,
        String allowOncallShifts,
        Long weekStartsOn,
        String allowOpenShifts,
        Long rosterUpdateNotAllow,
        String approvers) {

}
