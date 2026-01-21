package com.ewsv3.ews.commons.dto;

public record PersonSkillsRespDto(
        Long personPreferredSkillId,
        Long personId,
        String jobTitle,
        Long jobTitleId,
        Double rate,
        String currencyCode,
        Long skillId,
        String skill

) {
}
