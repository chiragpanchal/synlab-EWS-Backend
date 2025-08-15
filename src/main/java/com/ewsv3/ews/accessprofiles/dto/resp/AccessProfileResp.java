package com.ewsv3.ews.accessprofiles.dto.resp;

import com.ewsv3.ews.accessprofiles.dto.AccessProfiles;

import java.time.LocalDate;
import java.util.List;

public record AccessProfileResp(
        Long profileId,
        String profileName,
        LocalDate startDate,
        LocalDate endDate,
        String timekeepers

) {
}
