package com.ewsv3.ews.schedulerules.dto.req;

import java.time.LocalDate;

public record ScheduleRuleSearchRequest(
        Long profileId,
        LocalDate validDate
) {
    public ScheduleRuleSearchRequest() {
        this(null, null);
    }
}