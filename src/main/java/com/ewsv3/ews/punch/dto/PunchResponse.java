package com.ewsv3.ews.punch.dto;

import java.time.LocalDateTime;

public record PunchResponse(
        Long personPunchId,
        LocalDateTime punchTime,
        String punchType,
        String departmentName,
        String jobTitle,
        String timeType) {

}
