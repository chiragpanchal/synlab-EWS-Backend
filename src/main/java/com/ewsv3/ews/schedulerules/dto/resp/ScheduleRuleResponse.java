package com.ewsv3.ews.schedulerules.dto.resp;

import com.ewsv3.ews.schedulerules.dto.ScheduleRuleDto;

import java.util.List;

public class ScheduleRuleResponse {
    private List<ScheduleRuleDto> scheduleRules;
    private String message;

    public ScheduleRuleResponse() {
    }

    public ScheduleRuleResponse(List<ScheduleRuleDto> scheduleRules, String message) {
        this.scheduleRules = scheduleRules;
        this.message = message;
    }

    public List<ScheduleRuleDto> getScheduleRules() {
        return scheduleRules;
    }

    public void setScheduleRules(List<ScheduleRuleDto> scheduleRules) {
        this.scheduleRules = scheduleRules;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}