package com.ewsv3.ews.reports.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportReqDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportRespDto;
import com.ewsv3.ews.reports.service.timesheetReport.TimesheetReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final JdbcClient jdbcClient;
    private final TimesheetReportService timesheetReportService;

    public ReportController(JdbcClient jdbcClient, TimesheetReportService timesheetReportService) {
        this.jdbcClient = jdbcClient;
        this.timesheetReportService = timesheetReportService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("timesheet-report")
    public ResponseEntity<List<TimesheetReportRespDto>> getTimesheetDetails(@RequestHeader Map<String, String> header,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "500") int size,
                                                                            @RequestBody TimesheetReportReqDto reqDto) {
        logger.info("GET_TIMESHEET_REPORT - Entry - Time: {}, Page: {}, Size: {}, Request: {}", LocalDateTime.now(), page, size, reqDto);
        try {
            //System.out.println("timesheet-report reqDto:" + reqDto);
            List<TimesheetReportRespDto> timesheetReport = this.timesheetReportService.getTimesheetReport(getCurrentUserId(), page, size, reqDto, this.jdbcClient);
            logger.info("GET_TIMESHEET_REPORT - Exit - Time: {}, Page: {}, Size: {}, Response Count: {}", LocalDateTime.now(), page, size, timesheetReport.size());
            return new ResponseEntity<>(timesheetReport, HttpStatus.OK);
        } catch (Error error) {
            //System.out.println("timesheet-report error:" + error.getMessage());
            logger.error("GET_TIMESHEET_REPORT - Exception - Time: {}, Page: {}, Size: {}, Request: {}, Error: {}", LocalDateTime.now(), page, size, reqDto, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


}
