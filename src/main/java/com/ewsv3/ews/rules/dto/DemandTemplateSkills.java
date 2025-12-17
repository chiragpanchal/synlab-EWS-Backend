package com.ewsv3.ews.rules.dto;

import java.time.LocalDateTime;

public record DemandTemplateSkills(
        Long demandTemplateSkillId,
        Long demandTemplateLineId,
        Long skillId,
        Long rating,
        String mustHave,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate) {

}
