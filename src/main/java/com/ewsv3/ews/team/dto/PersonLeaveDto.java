package com.ewsv3.ews.team.dto;

import java.time.LocalDate;

public record PersonLeaveDto(
                Long personId,
                LocalDate leaveDate,
                String absenceName,
                Double absenceDays,
                Double absenceHrs) {

}
