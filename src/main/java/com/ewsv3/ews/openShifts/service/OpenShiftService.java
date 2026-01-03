package com.ewsv3.ews.openShifts.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.openShifts.dto.OpenShiftDetailSkills;
import com.ewsv3.ews.openShifts.dto.OpenShiftDetails;
import com.ewsv3.ews.openShifts.dto.OpenShiftLines;
import com.ewsv3.ews.openShifts.dto.OpenShiftsHeader;
import com.ewsv3.ews.rosters.controller.RosterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ewsv3.ews.openShifts.service.OpenShiftUtils.*;

@Service
public class OpenShiftService {

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    public OpenShiftsHeader getOpenShifts(Long openShiftId, JdbcClient jdbcClient) {
        logger.info("getOpenShifts - Entry - Time: {}, openShiftId: {}", LocalDateTime.now(), openShiftId);
        OpenShiftsHeader shiftsHeader = jdbcClient.sql(GetOpenShiftsByOpenShiftId)
                .param("openShiftId", openShiftId)
                .query(OpenShiftsHeader.class)
                .single();

        List<OpenShiftLines> shiftLinesList = jdbcClient.sql(GetOpenShiftLines)
                .param("openShiftId", openShiftId)
                .query(OpenShiftLines.class)
                .list();

        shiftsHeader.setOpenShiftLines(shiftLinesList);

        return shiftsHeader;


    }

    public DMLResponseDto createOpenShifts(Long userId, OpenShiftsHeader reqBody, JdbcClient jdbcClient) {

        Long generatedOpenShiftId = jdbcClient
                .sql("SELECT OPEN_SHIFT_ID_SQ.NEXTVAL FROM dual")
                .query(Long.class)
                .single();


        int createdCount = jdbcClient.sql(CreateOpenShiftHeader)
                .param("openShiftId", generatedOpenShiftId)
                .param("startDate", reqBody.getStartDate())
                .param("endDate", reqBody.getEndDate())
                .param("demandTemplateId", reqBody.getDemandTemplateId())
                .param("profileId", reqBody.getProfileId())
                .param("recalled", reqBody.getRecalled())
                .param("createdBy", userId)
                .param("createdOn", new Date())
                .param("lastUpdatedBy", userId)
                .param("lastUpdateDate", new Date())
                .update();

        if (createdCount == 1) {

            for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {
                int createdLioneCount = jdbcClient.sql(CreateOpenShiftLine)
                        .param("openShiftId", generatedOpenShiftId)
                        .param("demandTemplateLineId", openShiftLine.getDemandTemplateLineId())
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("createdBy", userId)
                        .param("createdOn", new Date())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();
            }


        } else {
            return new DMLResponseDto("E", "No open-shifts are generated");
        }

        logger.info("createOpenShifts - Entry - Time: {}, generatedOpenShiftId: {}", LocalDateTime.now(), generatedOpenShiftId);
        OpenShiftsHeader shiftsHeader = getOpenShifts(generatedOpenShiftId, jdbcClient);

        populateOpenShiftDetails(userId, shiftsHeader, generatedOpenShiftId, jdbcClient);

        return new

                DMLResponseDto("S", "Open-shifts are generated successfully");

    }

    public DMLResponseDto updateOpenShifts(Long userId, OpenShiftsHeader reqBody, JdbcClient jdbcClient) {


        OpenShiftsHeader openShiftsHeader = jdbcClient.sql(GetOpenShiftsByOpenShiftId)
                .param("openShiftId", reqBody.getOpenShiftId())
                .query(OpenShiftsHeader.class)
                .single();

        int updatedHEaderCount = 0;

        if (!Objects.equals(openShiftsHeader.getRecalled(), reqBody.getRecalled())) {
            updatedHEaderCount = jdbcClient.sql(UpdateOpenShiftHeader)
                    .param("openShiftId", reqBody.getOpenShiftId())
                    .param("recalled", reqBody.getRecalled())
                    .param("lastUpdatedBy", userId)
                    .param("lastUpdateDate", new Date())
                    .update();
        }

        Long generatedOpenShiftLineId = 0L;
        int createdLineCount = 0;
        int updatedLineCount = 0;

        for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {

            if (openShiftLine.getOpenShiftLineId() == null || openShiftLine.getOpenShiftLineId() == 0) {

                generatedOpenShiftLineId = jdbcClient
                        .sql("SELECT OPEN_SHIFT_ID_SQ.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                createdLineCount += jdbcClient.sql(CreateOpenShiftLine)
                        .param("openShiftId", reqBody.getOpenShiftId())
                        .param("demandTemplateLineId", openShiftLine.getDemandTemplateLineId())
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("createdBy", userId)
                        .param("createdOn", new Date())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();
            } else {
                generatedOpenShiftLineId = openShiftLine.getOpenShiftLineId();

                updatedLineCount += jdbcClient.sql(UpdateOpenShiftLine)
                        .param("openShiftLineId", generatedOpenShiftLineId)
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();

            }

        }


        if (updatedHEaderCount == 0 && createdLineCount == 0 && updatedLineCount == 0) {
            return new DMLResponseDto("E", "No open-shifts are updated");
        }

        OpenShiftsHeader shiftsHeader = getOpenShifts(reqBody.getOpenShiftId(), jdbcClient);

        populateOpenShiftDetails(userId, shiftsHeader, reqBody.getOpenShiftId(), jdbcClient);

        return new
                DMLResponseDto("S", "Open-shifts are updated successfully");

    }

    public void populateOpenShiftDetails(Long userId, OpenShiftsHeader reqBody, Long generatedOpenShiftId, JdbcClient jdbcClient) {

        LocalDate startDate = reqBody.getStartDate();
//        LocalDate endDate = reqBody.getEndDate();
        for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {

            if (Objects.nonNull(openShiftLine.getSun()) && openShiftLine.getSun() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate, openShiftLine.getSun(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getMon()) && openShiftLine.getMon() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(1), openShiftLine.getMon(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getTue()) && openShiftLine.getTue() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(2), openShiftLine.getTue(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getWed()) && openShiftLine.getWed() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(3), openShiftLine.getWed(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getThu()) && openShiftLine.getThu() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(4), openShiftLine.getThu(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getFri()) && openShiftLine.getFri() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(5), openShiftLine.getFri(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getSat()) && openShiftLine.getSat() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(6), openShiftLine.getSat(), jdbcClient);
            }
        }

    }

    public void insertOpenShiftDetails(Long userId, OpenShiftLines openShiftLine, Long generatedOpenShiftId, LocalDate effectiveDate, Long requestedFte, JdbcClient jdbcClient) {

        int insertedCounts = jdbcClient.sql(CreateOpenShiftDetail)
                .param("openShiftId", generatedOpenShiftId)
                .param("openShiftLineId", openShiftLine.getOpenShiftLineId())
                .param("departmentId", openShiftLine.getDepartmentId())
                .param("jobTitleId", openShiftLine.getJobTitleId())
                .param("locationId", openShiftLine.getLocationId())
                .param("workDurationId", openShiftLine.getWorkDurationId())
                .param("effectiveDate", effectiveDate)
                .param("fteRequested", requestedFte)
                .param("createdBy", userId)
                .param("createdOn", LocalDateTime.now())
                .param("lastUpdatedBy", userId)
                .param("lastUpdateDate", LocalDateTime.now())
                .update();

    }


}
