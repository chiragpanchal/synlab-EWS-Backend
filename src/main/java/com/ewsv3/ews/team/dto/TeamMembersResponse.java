package com.ewsv3.ews.team.dto;

import java.util.List;

public record TeamMembersResponse(
        List<TeamMembers> teamMembers,
        TeamTimecardKpi teamTimecardKpi
) {
}
