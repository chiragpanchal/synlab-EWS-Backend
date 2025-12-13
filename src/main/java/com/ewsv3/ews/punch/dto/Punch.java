package com.ewsv3.ews.punch.dto;

import java.time.LocalDateTime;

public record Punch(
                Long personPunchId,
                Long personId,
                Long departmentId,
                Long jobTitleId,
                LocalDateTime punchTime,
                String punchType,
                String timeType) {

}
