package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.TimesheetKpi;
import com.ewsv3.ews.timesheets.dto.TimesheetPageRequestBody;
import com.ewsv3.ews.timesheets.dto.TimesheetPageResponseBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalReqBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalStatus;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetSubmitReqBody;
import com.ewsv3.ews.timesheets.service.approval.TimesheetServiceApproval;
import com.ewsv3.ews.timesheets.service.submission.TimesheetSubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timesheet-approval/")
public class TimesheetSubmitController {

    private final TimesheetSubmissionService timesheetSubmissionService;
    private final TimesheetServiceApproval timesheetServiceApproval;
    private final JdbcClient jdbcClient;

    public TimesheetSubmitController(TimesheetSubmissionService timesheetSubmissionService, JdbcClient jdbcClient) {
        this.timesheetSubmissionService = timesheetSubmissionService;
        this.timesheetServiceApproval = new TimesheetServiceApproval();
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

        try {
            DMLResponseDto dmlResponseDto = this.timesheetSubmissionService.submitTimesheets(getCurrentUserId(),
                    reqBody);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("get-approvals")
    public ResponseEntity<List<TimesheetApprovalStatus>> getTimesheetApprovalHistory(
            @RequestHeader Map<String, String> headers, @RequestBody TimesheetApprovalReqBody reqBody) {

        try {
            List<TimesheetApprovalStatus> timesheetApprovals = this.timesheetSubmissionService
                    .getTimesheetApprovals(reqBody.itemKey(), jdbcClient);
            return new ResponseEntity<>(timesheetApprovals, HttpStatus.OK);
        } catch (Exception e) {
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

        try {
            List<TimesheetPageResponseBody> timesheetApprovals = this.timesheetServiceApproval
                    .getTimesheetApprovalData(getCurrentUserId(),
                            page,
                            size,
                            payCodeName,
                            requestBody,
                            this.jdbcClient);
            return new ResponseEntity<>(timesheetApprovals, HttpStatus.OK);
        } catch (Exception e) {
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

        try {
            TimesheetKpi timesheetKpis = this.timesheetServiceApproval
                    .getTimesheetApprovalKpi(getCurrentUserId(),
                            payCodeName,
                            requestBody,
                            this.jdbcClient);
            return new ResponseEntity<>(timesheetKpis, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
