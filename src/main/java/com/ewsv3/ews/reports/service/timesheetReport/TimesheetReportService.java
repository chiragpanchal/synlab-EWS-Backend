package com.ewsv3.ews.reports.service.timesheetReport;

import com.ewsv3.ews.openShifts.dto.allocation.SuggestionPersonDto;
import com.ewsv3.ews.reports.controller.ReportController;
import com.ewsv3.ews.reports.dto.ReportPersonDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportReqDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportRespDto;
import com.ewsv3.ews.reports.dto.timesheetReport.xx_TimesheetReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ewsv3.ews.reports.service.timesheetReport.TimesheetReportUtils.*;
import static java.awt.SystemColor.text;

@Service
public class TimesheetReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    public List<TimesheetReportRespDto> getTimesheetReport(Long userId, int page,
                                                           int size, TimesheetReportReqDto reqDto, JdbcClient jdbcClient) {

        //System.out.println("timesheet-report getTimesheetReport userId:" + userId);
        //System.out.println("timesheet-report getTimesheetReport reqDto:" + reqDto);

        String pendingWithParam = "%" + (reqDto.pendingWith() == null ? "" : reqDto.pendingWith()) + "%";
        String employeeTextParam = "%" + (reqDto.employeeText() == null ? "" : reqDto.employeeText()) + "%";

        List<ReportPersonDto> personList = new ArrayList<>();
        if (reqDto.profileId() == 0) {
            // line manager
            personList = jdbcClient.sql(sqlLineManagerRecordsReportSQL)
                    .param("userId", userId)
                    .param("text", "%" + text + "%")
                    .param("status", reqDto.status())
                    .param("payCodeName", reqDto.payCodeName())
                    .param("text", employeeTextParam)
//                    .param("pendingWithParam", pendingWithParam)
                    .param("startDate", reqDto.startDate())
                    .param("endDate", reqDto.endDate())
                    .param("departmentId",reqDto.departmentId())
                    .param("jobTitleId",reqDto.jobTitleId())
                    .query(ReportPersonDto.class)
                    .list();

        } else {
            // Timekeeper Profile
            personList = jdbcClient.sql(sqlTimesheetPersonListReportSQL)
                    .param("userId", userId)
                    .param("text", "%" + text + "%")
                    .param("status", reqDto.status())
                    .param("payCodeName", reqDto.payCodeName())
//                    .param("pendingWithParam",pendingWithParam)
                    .param("startDate",reqDto.startDate() )
                    .param("endDate", reqDto.endDate())
                    .param("profileId",reqDto.profileId())
                    .param("departmentId",reqDto.departmentId())
                    .param("jobTitleId",reqDto.jobTitleId())
                    .param("offset", page)
                    .param("pageSize", size)
                    .query(ReportPersonDto.class)
                    .list();

            // System.out.println("getTimesheetData personList.size():" +
            // personList.size());

        }

        logger.info("getTimesheetReport - Entry - Time: {}, Page: {}, Size: {}, personList.size(): {}", LocalDateTime.now(), page, size, personList.size());

        List<TimesheetReportRespDto> reportRespDtos =  new ArrayList<>();
        if (!personList.isEmpty()){

            List<Long> collectedPersonIds = personList.stream().map(ReportPersonDto::personId).toList();


            reportRespDtos = jdbcClient.sql(timesheetReportSql)
                    .param("departmentId", reqDto.departmentId())
                    .param("jobTitleId",reqDto.jobTitleId())
                    .param("pendingWith", pendingWithParam)
                    .param("personIds", collectedPersonIds)
                    .param("startDate",reqDto.startDate() )
                    .param("endDate", reqDto.endDate())
                    .param("payCodeName", reqDto.payCodeName())
                    .param("status", reqDto.status())
                    .query(TimesheetReportRespDto.class)
                    .list();
        }

        return reportRespDtos;


    }

}
