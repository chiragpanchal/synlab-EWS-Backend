package com.ewsv3.ews.rosters.dto.rosters.validate;

import java.util.List;

public class ValidateRosterResponse {

    List<DemandLineResponse> demandLineResponseList;
    List<ScheduleLineResponse> getDemandLineResponseList;

    public ValidateRosterResponse() {
    }

    public ValidateRosterResponse(List<DemandLineResponse> demandLineResponseList, List<ScheduleLineResponse> getDemandLineResponseList) {
        this.demandLineResponseList = demandLineResponseList;
        this.getDemandLineResponseList = getDemandLineResponseList;
    }

    public List<DemandLineResponse> getDemandLineResponseList() {
        return demandLineResponseList;
    }

    public void setDemandLineResponseList(List<DemandLineResponse> demandLineResponseList) {
        this.demandLineResponseList = demandLineResponseList;
    }

    public List<ScheduleLineResponse> getGetDemandLineResponseList() {
        return getDemandLineResponseList;
    }

    public void setGetDemandLineResponseList(List<ScheduleLineResponse> getDemandLineResponseList) {
        this.getDemandLineResponseList = getDemandLineResponseList;
    }
}
