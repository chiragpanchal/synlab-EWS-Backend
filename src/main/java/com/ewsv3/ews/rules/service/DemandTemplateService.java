package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.rules.dto.DemandTemplate;
import com.ewsv3.ews.rules.dto.DemandTemplateLine;
import com.ewsv3.ews.rules.dto.DemandTemplateSaveReqBody;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static com.ewsv3.ews.rules.service.DemandTemplateUtils.*;

@Service
public class DemandTemplateService {

    public List<DemandTemplate> getDemandTemplates(Long userId, JdbcClient jdbcClient, DemandTemplate template) {

        // System.out.println("getDemandTemplates template:" + template);

        List<DemandTemplate> demandTemplates = jdbcClient.sql(DemandTemplateHSql)
                .param("userId", userId)
                .param("templateName", template.templateName())
                .param("profileId", template.profileId())
                .query(DemandTemplate.class)
                .list();

        return demandTemplates;

    }

    public List<DemandTemplateLine> getDemandTemplatesLines(Long demandTemplateId, JdbcClient jdbcClient) {

        try {
            List<DemandTemplateLine> demandTemplateLines = jdbcClient.sql(DemandTemplateLineSql)
                    .param("demandTemplateId", demandTemplateId)
                    .query(DemandTemplateLine.class)
                    .list();

            return demandTemplateLines;

        } catch (Exception exception) {
            return new ArrayList<>();
        }

    }

    public DMLResponseDto saveDemandTempalte(DemandTemplateSaveReqBody reqBody, Long userId, JdbcClient jdbcClient) {

        // System.out.println("saveDemandTemplate: reqBody:" + reqBody);
        // System.out.println("saveDemandTemplate: reqBody.getDemandTemplate:" + reqBody.getDemandTemplate());
        Long generatedDemandTemplateId;

        // DemandTemplateSaveReqBody templateSaveReqBody= new
        // DemandTemplateSaveReqBody();

        if (reqBody.getDemandTemplate().demandTemplateId() == 0) {

            generatedDemandTemplateId = jdbcClient
                    .sql("SELECT demand_template_id_sq.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            jdbcClient.sql(insertDemandTemplateHeader)
                    .param("createdBy", userId)
                    .param("lastUpdatedBy", userId)
                    .param("demandTemplateId", generatedDemandTemplateId)
                    .param("profileId", reqBody.getDemandTemplate().profileId())
                    .param("validTo", reqBody.getDemandTemplate().validTo())
                    .param("templateName", reqBody.getDemandTemplate().templateName())
                    .update();

        } else {
            generatedDemandTemplateId = reqBody.getDemandTemplate().demandTemplateId();
            jdbcClient.sql(updateDemandTemplateHeader)
                    .param("lastUpdatedBy", userId)
                    .param("profileId", reqBody.getDemandTemplate().profileId())
                    .param("validTo", reqBody.getDemandTemplate().validTo())
                    .param("templateName", reqBody.getDemandTemplate().templateName())
                    .param("demandTemplateId", reqBody.getDemandTemplate().demandTemplateId())
                    .update();

        }

        // DemandTemplate demandTemplate = getDemandTemplates(userId, jdbcClient,
        // reqBody.getDemandTemplate()).getFirst();

        // templateSaveReqBody.setDemandTemplate(demandTemplate);

        // System.out.println("saveDemandTempalte: saved
        // demandTemplate:"+demandTemplate);

        for (DemandTemplateLine demandTemplateLine : reqBody.getDemandTemplateLineList()) {

            // System.out.println("saveDemandTemplate: demandTemplateLine:" + demandTemplateLine);

            if (demandTemplateLine.demandTemplateLineId() == 0) {

                Long generatedDemandTemplateLineId = jdbcClient
                        .sql("SELECT demand_line_id_sq.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                jdbcClient.sql(insertDemandTemplateLine)
                        .param("demandTemplateLineId", generatedDemandTemplateLineId)
                        .param("demandTemplateId", generatedDemandTemplateId)
                        .param("departmentId", demandTemplateLine.departmentId())
                        .param("jobTitleId", demandTemplateLine.jobTitleId())
                        .param("locationId", demandTemplateLine.locationId())
                        .param("workDurationId", demandTemplateLine.workDurationId())
                        // .param("timeStart", demandTemplateLine.timeStart())
                        // .param("timeEnd", demandTemplateLine.timeEnd())
                        .param("sun", demandTemplateLine.sun())
                        .param("mon", demandTemplateLine.mon())
                        .param("tue", demandTemplateLine.tue())
                        .param("wed", demandTemplateLine.wed())
                        .param("thu", demandTemplateLine.thu())
                        .param("fri", demandTemplateLine.fri())
                        .param("sat", demandTemplateLine.sat())
                        .param("createdBy", userId)
                        .param("lastUpdatedBy", userId)
                        .update();

            } else {

                jdbcClient.sql(updateDemandTemplateLine)
                        .param("demandTemplateLineId", demandTemplateLine.demandTemplateLineId())
                        .param("departmentId", demandTemplateLine.departmentId())
                        .param("jobTitleId", demandTemplateLine.jobTitleId())
                        .param("locationId", demandTemplateLine.locationId())
                        .param("workDurationId", demandTemplateLine.workDurationId())
                        // .param("timeStart", demandTemplateLine.timeStart())
                        // .param("timeEnd", demandTemplateLine.timeEnd())
                        .param("sun", demandTemplateLine.sun())
                        .param("mon", demandTemplateLine.mon())
                        .param("tue", demandTemplateLine.tue())
                        .param("wed", demandTemplateLine.wed())
                        .param("thu", demandTemplateLine.thu())
                        .param("fri", demandTemplateLine.fri())
                        .param("sat", demandTemplateLine.sat())
                        .param("lastUpdatedBy", userId)
                        .update();

            }

        }

        return new DMLResponseDto("S", "Demand Template saved successfully");

    }

    public DMLResponseDto deleteDemandTemplateLine(Long userId, @RequestBody DemandTemplateLine reqBody,
            JdbcClient jdbcClient) {

        int deleted = jdbcClient.sql(deleteDemandTemplateLine)
                .param("demandTemplateLineId", reqBody.demandTemplateLineId())
                .update();

        if (deleted > 0) {
            return new DMLResponseDto("S", deleted + " demand lines deleted.");
        } else {
            return new DMLResponseDto("S", "No demand lines deleted.");
        }

    }

    public DMLResponseDto deleteDemandTemplate(Long userId, @RequestBody DemandTemplate reqBody,
            JdbcClient jdbcClient) {

        for (DemandTemplateLine line : getDemandTemplatesLines(reqBody.demandTemplateId(), jdbcClient)) {
            // System.out.println("deleting demand line :" + line);
            deleteDemandTemplateLine(userId, line, jdbcClient);
        }

        int deleted = jdbcClient.sql(deleteDemandTemplateHeader)
                .param("demandTemplateId", reqBody.demandTemplateId())
                .update();

        if (deleted > 0) {
            return new DMLResponseDto("S", deleted + " demand template deleted.");
        } else {
            return new DMLResponseDto("S", "No demand template deleted.");
        }

    }

}
