package com.ewsv3.ews.reports.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.UserIdReqDto;
import com.ewsv3.ews.commons.dto.UserProfileResponse;
import com.ewsv3.ews.commons.service.CommonService;
import com.ewsv3.ews.reports.dto.reportMasters.AllReportMasters;
import com.ewsv3.ews.reports.dto.requestStatusReport.RequestStatusReportReqBody;
import com.ewsv3.ews.reports.dto.requestStatusReport.RequestStatusRespDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportReqDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportRespDto;
import com.ewsv3.ews.reports.service.reportMaters.ReportMasterService;
import com.ewsv3.ews.reports.service.requestStatusReport.RequestStatusReportService;
import com.ewsv3.ews.reports.dto.rosterAuditReport.RosterAuditReqDto;
import com.ewsv3.ews.reports.dto.rosterAuditReport.RosterAuditResponseDto;
import com.ewsv3.ews.reports.service.rosterAuditReport.RosterAuditService;
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
    private final CommonService commonService;
    private final TimesheetReportService timesheetReportService;
    private final RosterAuditService rosterAuditService;
    private final RequestStatusReportService requestStatusReportService;
    private final ReportMasterService reportMasterService;

    public ReportController(JdbcClient jdbcClient, CommonService commonService, TimesheetReportService timesheetReportService, RosterAuditService rosterAuditService, RequestStatusReportService requestStatusReportService, ReportMasterService reportMasterService) {
        this.jdbcClient = jdbcClient;
        this.commonService = commonService;
        this.timesheetReportService = timesheetReportService;
        this.rosterAuditService = rosterAuditService;
        this.requestStatusReportService = requestStatusReportService;
        this.reportMasterService = reportMasterService;
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

    @PostMapping("roster-audit-report")
    public ResponseEntity<List<RosterAuditResponseDto>> getRosterAuditReport(@RequestHeader Map<String, String> header,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "500") int size,
                                                                             @RequestBody RosterAuditReqDto reqDto){


        logger.info("roster-audit-report - Entry - Time: {}, Page: {}, Size: {}, Request: {}", LocalDateTime.now(), page, size, reqDto);
        try {
            //System.out.println("timesheet-report reqDto:" + reqDto);
            List<RosterAuditResponseDto> rosterAuditReport = this.rosterAuditService.getRosterAuditReport(getCurrentUserId(), page, size, reqDto, this.jdbcClient);
            logger.info("roster-audit-report - Exit - Time: {}, Page: {}, Size: {}, Response Count: {}", LocalDateTime.now(), page, size, rosterAuditReport.size());
            return new ResponseEntity<>(rosterAuditReport, HttpStatus.OK);
        } catch (Error error) {
            //System.out.println("timesheet-report error:" + error.getMessage());
            logger.error("roster-audit-report - Exception - Time: {}, Page: {}, Size: {}, Request: {}, Error: {}", LocalDateTime.now(), page, size, reqDto, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("request-status-report")
    public ResponseEntity<List<RequestStatusRespDto>> getRequestStatusReport(@RequestHeader Map<String, String> header,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "500") int size,
                                                                           @RequestBody RequestStatusReportReqBody reqDto){


        logger.info("request-status-report - Entry - Time: {}, Page: {}, Size: {}, Request: {}", LocalDateTime.now(), page, size, reqDto);
        try {
            //System.out.println("timesheet-report reqDto:" + reqDto);
            List<RequestStatusRespDto> requestStatusReport = this.requestStatusReportService.getRequestStatusReport(getCurrentUserId(), page, size, reqDto, this.jdbcClient);
            logger.info("request-status-report - Exit - Time: {}, Page: {}, Size: {}, Response Count: {}", LocalDateTime.now(), page, size, requestStatusReport.size());
            return new ResponseEntity<>(requestStatusReport, HttpStatus.OK);
        } catch (Error error) {
            //System.out.println("timesheet-report error:" + error.getMessage());
            logger.error("request-status-report - Exception - Time: {}, Page: {}, Size: {}, Request: {}, Error: {}", LocalDateTime.now(), page, size, reqDto, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("report-masters")
    public ResponseEntity<AllReportMasters> getReportMasters(@RequestHeader Map<String, String> header){
        logger.info("report-masters - Entry - Time: {}", LocalDateTime.now());
        try{
            UserProfileResponse userFromUserId = this.commonService
                    .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
            AllReportMasters allReportMasters = reportMasterService.getAllReportMasters(getCurrentUserId(), userFromUserId.personId(), jdbcClient);
            logger.info("report-masters  - Exit - Time: {}, allReportMasters.getReportDepartmentDtoList().size(): {}", LocalDateTime.now(), allReportMasters.getReportDepartmentDtoList().size() );
            logger.info("report-masters  - Exit - Time: {}, allReportMasters.getReportJobDtoList().size(): {}", LocalDateTime.now(), allReportMasters.getReportJobDtoList().size() );
            return new ResponseEntity<>(allReportMasters, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("report-masters - Exception - Time: {}  Error: {}", LocalDateTime.now(),  e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
