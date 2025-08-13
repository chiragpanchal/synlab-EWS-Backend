package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;

public record RosterActionsReqBody(
        Long profileId,
        String comments,
        String persons,
        LocalDate startDate,
        LocalDate endDate,
        String action, // APPROVED,RMI,SUBMIT,WITHD,INIT
        String dmlFlag
) {
}
