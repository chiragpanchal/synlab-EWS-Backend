package com.ewsv3.ews.timesheets.dto.submission;

import java.time.LocalDate;

public record TimesheetActionReqBody(
                Long personId,
                String payCodes,
                LocalDate startDate,
                LocalDate endDate,
                String fromAction,
                String strPerson,
                Long fwdUserId,
                Long rmiUserId,
                String comments) {
}
