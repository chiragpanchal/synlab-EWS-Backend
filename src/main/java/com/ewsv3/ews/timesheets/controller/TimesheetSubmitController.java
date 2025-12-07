package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.TimesheetKpi;
import com.ewsv3.ews.timesheets.dto.TimesheetPageRequestBody;
import com.ewsv3.ews.timesheets.dto.TimesheetPageResponseBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetActionReqBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalDates;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalReqBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalStatus;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetPeriodTypeReqBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetSubmitReqBody;
import com.ewsv3.ews.timesheets.service.approval.TimesheetServiceApproval;
import com.ewsv3.ews.timesheets.service.submission.TimesheetSubmissionService;
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
@RequestMapping("/api/timesheet-approval/")
public class TimesheetSubmitController {

    private static final Logger logger = LoggerFactory.getLogger(TimesheetSubmitController.class);

    private final TimesheetSubmissionService timesheetSubmissionService;
    private final TimesheetServiceApproval timesheetServiceApproval;
    private final JdbcClient jdbcClient;

    public TimesheetSubmitController(TimesheetSubmissionService timesheetSubmissionService,
            TimesheetServiceApproval timesheetServiceApproval, JdbcClient jdbcClient) {
        this.timesheetSubmissionService = timesheetSubmissionService;
        this.timesheetServiceApproval = timesheetServiceApproval;
        this.jdbcClient = jdbcClient;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("submit")
    public ResponseEntity<DMLResponseDto> submitTimesheets(@RequestHeader Map<String, String> headers,
            @RequestBody TimesheetSubmitReqBody reqBody) {

        logger.info("submitTimesheets - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        try {
            DMLResponseDto dmlResponseDto = this.timesheetSubmissionService.submitTimesheets(getCurrentUserId(),
                    reqBody);
            logger.info("submitTimesheets - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("submitTimesheets - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("action")
    public ResponseEntity<DMLResponseDto> actionTimesheets(@RequestHeader Map<String, String> headers,
            @RequestBody TimesheetActionReqBody reqBody) {

        logger.info("actionTimesheets - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        try {
            //System.out.println("action reqBody:" + reqBody);
            DMLResponseDto dmlResponseDto = this.timesheetServiceApproval.actionTimesheets(getCurrentUserId(), reqBody);
            logger.info("actionTimesheets - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("actionTimesheets - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("period-dates")
    public ResponseEntity<List<TimesheetApprovalDates>> getApprovalPeriodDates(
            @RequestBody TimesheetPeriodTypeReqBody requestBody) {

        logger.info("getApprovalPeriodDates - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {
            //System.out.println("period-dates requestBody: " + requestBody);
            List<TimesheetApprovalDates> approvalDates = this.timesheetServiceApproval
                    .getApprovalDates(getCurrentUserId(), requestBody, jdbcClient);
            logger.info("getApprovalPeriodDates - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), approvalDates.size());
            return new ResponseEntity<>(approvalDates, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("period-dates error: " + e.getMessage());
            logger.error("getApprovalPeriodDates - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), requestBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("get-approvals")
    public ResponseEntity<List<TimesheetApprovalStatus>> getTimesheetApprovalHistory(
            @RequestHeader Map<String, String> headers, @RequestBody TimesheetApprovalReqBody reqBody) {

        logger.info("getTimesheetApprovalHistory - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        try {
            List<TimesheetApprovalStatus> timesheetApprovals = this.timesheetSubmissionService
                    .getTimesheetApprovals(reqBody.itemKey(), jdbcClient);
            logger.info("getTimesheetApprovalHistory - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timesheetApprovals.size());
            return new ResponseEntity<>(timesheetApprovals, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getTimesheetApprovalHistory - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("get-timesheet-approvals-page")
    public ResponseEntity<List<TimesheetPageResponseBody>> getTimesheetApprovals(
            @RequestHeader Map<String, String> headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") String text,
            @RequestParam(defaultValue = "") String filterFlag,
            @RequestParam(defaultValue = "") String payCodeName,
            @RequestBody TimesheetPageRequestBody requestBody) {

        logger.info("getTimesheetApprovals - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {

            //System.out.println("get-timesheet-approvals-page payCodeName" + payCodeName);
            //System.out.println("get-timesheet-approvals-page requestBody" + requestBody);

            List<TimesheetPageResponseBody> timesheetApprovals = this.timesheetServiceApproval
                    .getTimesheetApprovalData(getCurrentUserId(),
                            page,
                            size,
                            payCodeName,
                            requestBody,
                            this.jdbcClient);
            logger.info("getTimesheetApprovals - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timesheetApprovals.size());
            return new ResponseEntity<>(timesheetApprovals, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getTimesheetApprovals - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), requestBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("get-timesheet-approvals-page-kpi")
    public ResponseEntity<TimesheetKpi> getTimesheetApprovalsKpi(
            @RequestHeader Map<String, String> headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") String text,
            @RequestParam(defaultValue = "") String filterFlag,
            @RequestParam(defaultValue = "") String payCodeName,
            @RequestBody TimesheetPageRequestBody requestBody) {

        logger.info("getTimesheetApprovalsKpi - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {
            TimesheetKpi timesheetKpis = this.timesheetServiceApproval
                    .getTimesheetApprovalKpi(getCurrentUserId(),
                            payCodeName,
                            requestBody,
                            this.jdbcClient);
            logger.info("getTimesheetApprovalsKpi - Exit - Time: {}, Response: {}", LocalDateTime.now(), timesheetKpis);
            return new ResponseEntity<>(timesheetKpis, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getTimesheetApprovalsKpi - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), requestBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
