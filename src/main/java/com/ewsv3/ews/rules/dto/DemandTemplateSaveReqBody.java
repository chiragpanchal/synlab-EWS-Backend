package com.ewsv3.ews.rules.dto;

import java.util.List;

public class DemandTemplateSaveReqBody {

    DemandTemplate demandTemplate;
    List<DemandTemplateLine> demandTemplateLineList;

    public DemandTemplateSaveReqBody() {
    }

    public DemandTemplate getDemandTemplate() {
        return demandTemplate;
    }

    public void setDemandTemplate(DemandTemplate demandTemplate) {
        this.demandTemplate = demandTemplate;
    }

    public List<DemandTemplateLine> getDemandTemplateLineList() {
        return demandTemplateLineList;
    }

    public void setDemandTemplateLineList(List<DemandTemplateLine> demandTemplateLineList) {
        this.demandTemplateLineList = demandTemplateLineList;
    }
}
