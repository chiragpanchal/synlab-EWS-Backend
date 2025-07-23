package com.ewsv3.ews.rosters.dto.rosters.payload;


import com.ewsv3.ews.rosters.dto.rosters.PersonDtoSelected;
import com.ewsv3.ews.rosters.dto.rosters.WorkDurationDtoAssignment;

import java.util.Date;
import java.util.List;


public record SpotRequestBody(
        Date startDate,
        Date endDate,
        Long personRosterId,
        List<PersonDtoSelected> personDtoSelected,
        List<WorkDurationDtoAssignment> workDurationDtoAssignment) {
}
