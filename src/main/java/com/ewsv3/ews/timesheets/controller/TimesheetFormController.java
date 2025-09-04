package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.form.*;
import com.ewsv3.ews.timesheets.service.form.TimesheetFormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timesheet-form/")
public class TimesheetFormController {


    private final TimesheetFormService timesheetFormService;
    private final JdbcClient jdbcClient;

    public TimesheetFormController(TimesheetFormService timesheetFormService, JdbcClient jdbcClient) {
        this.timesheetFormService = timesheetFormService;
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

    @PostMapping("pay-codes")
    public ResponseEntity<List<PayCodeDto>> getPayCodes(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            List<PayCodeDto> payCodes = this.timesheetFormService.getPayCodes(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(payCodes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error pay-codes : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-departments")
    public ResponseEntity<List<TsDepartmentDto>> getDepartments(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            List<TsDepartmentDto> departments = this.timesheetFormService.getDepartments(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-departments : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-jobs")
    public ResponseEntity<List<TsJobDto>> getJobs(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            List<TsJobDto> jobs = this.timesheetFormService.getJobs(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-jobs : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-project-tasks")
    public ResponseEntity<List<ProjectTaskDto>> getProjectTasks(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            List<ProjectTaskDto> projectTasks = this.timesheetFormService.getProjectTasks(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(projectTasks, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-project-tasks : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-expenditure-types")
    public ResponseEntity<List<ExpTypeDto>> getExpenditureTypes(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            List<ExpTypeDto> expenditureTypes = this.timesheetFormService.getExpenditureTypes(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(expenditureTypes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-expenditure-types : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-masters")
    public ResponseEntity<TsMasters> getTimesheetMasters(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        try {
            TsMasters timesheetMasters = this.timesheetFormService.getTimesheetMasters(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(timesheetMasters, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-masters : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-details")
    public ResponseEntity<List<TimesheetDetails>> getTimesheetDetails(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        try {
            System.out.println("ts-details reqDto:" + reqDto);
            List<TimesheetDetails> timesheetdetails = this.timesheetFormService.getTimesheetdetails(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(timesheetdetails, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-details : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-timecard-details")
    public ResponseEntity<List<TsTimecardData>> getTimecardData(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        try {
            System.out.println("ts-timecard-details reqDto:" + reqDto);
            List<TsTimecardData> timecardData = this.timesheetFormService.getTimecardData(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(timecardData, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-timecard-details : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("save-timesheets")
    public ResponseEntity<DMLResponseDto> saveTimecardData(@RequestHeader Map<String, String> headers, @RequestBody List<TimesheetDetails> detailsList) {

        try {
            System.out.println("save-timesheets detailsList.size():" + detailsList.size());
            DMLResponseDto dmlResponseDto = this.timesheetFormService.saveTimesheets(getCurrentUserId(), detailsList, this.jdbcClient);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error save-timesheets : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("delete-timesheets")
    public ResponseEntity<DMLResponseDto> deleteTimecardData(@RequestHeader Map<String, String> headers, @RequestBody List<TimesheetDetails> detailsList) {

        try {
            System.out.println("delete-timesheets detailsList.size():" + detailsList.size());
            DMLResponseDto dmlResponseDto = this.timesheetFormService.deleteTimesheets(getCurrentUserId(), detailsList, this.jdbcClient);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error delete-timesheets : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timesheet-audit")
    public ResponseEntity<List<TimesheetAuditDto>> getTimesheetAudit(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        try {
            System.out.println("timesheet-auditreqDto:" + reqDto);
            List<TimesheetAuditDto> timesheetAudit = this.timesheetFormService.getTimesheetAudit(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(timesheetAudit, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error timesheet-audit : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
