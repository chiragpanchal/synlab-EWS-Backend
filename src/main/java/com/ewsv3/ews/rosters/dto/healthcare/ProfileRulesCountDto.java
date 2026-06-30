package com.ewsv3.ews.rosters.dto.healthcare;

public class ProfileRulesCountDto {
    private Long ACUITY_RULES;
    private Long OCCUPY_RULES;
    private Long APPOINT_RULES;

    public ProfileRulesCountDto() {}

    public ProfileRulesCountDto(Long acuityRules, Long occupyRules, Long appointRules) {
        this.ACUITY_RULES = acuityRules;
        this.OCCUPY_RULES = occupyRules;
        this.APPOINT_RULES = appointRules;
    }

    public Long getACUITY_RULES() {
        return ACUITY_RULES;
    }

    public void setACUITY_RULES(Long acuityRules) {
        this.ACUITY_RULES = acuityRules;
    }

    public Long getOCCUPY_RULES() {
        return OCCUPY_RULES;
    }

    public void setOCCUPY_RULES(Long occupyRules) {
        this.OCCUPY_RULES = occupyRules;
    }

    public Long getAPPOINT_RULES() {
        return APPOINT_RULES;
    }

    public void setAPPOINT_RULES(Long appointRules) {
        this.APPOINT_RULES = appointRules;
    }
}
