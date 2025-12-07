package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.form.*;
import com.ewsv3.ews.timesheets.service.form.TimesheetFormService;
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
@RequestMapping("/api/timesheet-form/")
public class TimesheetFormController {

    private static final Logger logger = LoggerFactory.getLogger(TimesheetFormController.class);

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

        logger.info("getPayCodes - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            List<PayCodeDto> payCodes = this.timesheetFormService.getPayCodes(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getPayCodes - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), payCodes.size());
            return new ResponseEntity<>(payCodes, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error pay-codes : " + e.getMessage());
            logger.error("getPayCodes - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-departments")
    public ResponseEntity<List<TsDepartmentDto>> getDepartments(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        logger.info("getDepartments - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            List<TsDepartmentDto> departments = this.timesheetFormService.getDepartments(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getDepartments - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), departments.size());
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-departments : " + e.getMessage());
            logger.error("getDepartments - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-jobs")
    public ResponseEntity<List<TsJobDto>> getJobs(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        logger.info("getJobs - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            List<TsJobDto> jobs = this.timesheetFormService.getJobs(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getJobs - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), jobs.size());
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-jobs : " + e.getMessage());
            logger.error("getJobs - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-project-tasks")
    public ResponseEntity<List<ProjectTaskDto>> getProjectTasks(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        logger.info("getProjectTasks - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            List<ProjectTaskDto> projectTasks = this.timesheetFormService.getProjectTasks(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getProjectTasks - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), projectTasks.size());
            return new ResponseEntity<>(projectTasks, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-project-tasks : " + e.getMessage());
            logger.error("getProjectTasks - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-expenditure-types")
    public ResponseEntity<List<ExpTypeDto>> getExpenditureTypes(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        logger.info("getExpenditureTypes - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            List<ExpTypeDto> expenditureTypes = this.timesheetFormService.getExpenditureTypes(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getExpenditureTypes - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), expenditureTypes.size());
            return new ResponseEntity<>(expenditureTypes, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-expenditure-types : " + e.getMessage());
            logger.error("getExpenditureTypes - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-masters")
    public ResponseEntity<TsMasters> getTimesheetMasters(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto) {

        logger.info("getTimesheetMasters - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            TsMasters timesheetMasters = this.timesheetFormService.getTimesheetMasters(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getTimesheetMasters - Exit - Time: {}, Response: {}", LocalDateTime.now(), timesheetMasters);
            return new ResponseEntity<>(timesheetMasters, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-masters : " + e.getMessage());
            logger.error("getTimesheetMasters - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-details")
    public ResponseEntity<List<TimesheetDetails>> getTimesheetDetails(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        logger.info("getTimesheetDetails - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            //System.out.println("ts-details reqDto:" + reqDto);
            List<TimesheetDetails> timesheetdetails = this.timesheetFormService.getTimesheetdetails(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getTimesheetDetails - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timesheetdetails.size());
            return new ResponseEntity<>(timesheetdetails, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-details : " + e.getMessage());
            logger.error("getTimesheetDetails - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-timecard-details")
    public ResponseEntity<List<TsTimecardData>> getTimecardData(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        logger.info("getTimecardData - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            //System.out.println("ts-timecard-details reqDto:" + reqDto);
            List<TsTimecardData> timecardData = this.timesheetFormService.getTimecardData(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getTimecardData - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timecardData.size());
            return new ResponseEntity<>(timecardData, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error ts-timecard-details : " + e.getMessage());
            logger.error("getTimecardData - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("save-timesheets")
    public ResponseEntity<DMLResponseDto> saveTimecardData(@RequestHeader Map<String, String> headers, @RequestBody List<TimesheetDetails> detailsList) {

        logger.info("saveTimecardData - Entry - Time: {}, Request: {}", LocalDateTime.now(), detailsList);

        try {
            //System.out.println("save-timesheets detailsList.size():" + detailsList.size());
            DMLResponseDto dmlResponseDto = this.timesheetFormService.saveTimesheets(getCurrentUserId(), detailsList, this.jdbcClient);
            logger.info("saveTimecardData - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error save-timesheets : " + e.getMessage());
            logger.error("saveTimecardData - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), detailsList, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("delete-timesheets")
    public ResponseEntity<DMLResponseDto> deleteTimecardData(@RequestHeader Map<String, String> headers, @RequestBody List<TimesheetDetails> detailsList) {

        logger.info("deleteTimecardData - Entry - Time: {}, Request: {}", LocalDateTime.now(), detailsList);

        try {
            //System.out.println("delete-timesheets detailsList.size():" + detailsList.size());
            DMLResponseDto dmlResponseDto = this.timesheetFormService.deleteTimesheets(getCurrentUserId(), detailsList, this.jdbcClient);
            logger.info("deleteTimecardData - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error delete-timesheets : " + e.getMessage());
            logger.error("deleteTimecardData - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), detailsList, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timesheet-audit")
    public ResponseEntity<List<TimesheetAuditDto>> getTimesheetAudit(@RequestHeader Map<String, String> headers, @RequestBody TimesheetDetailsReqDto reqDto) {

        logger.info("getTimesheetAudit - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            //System.out.println("timesheet-auditreqDto:" + reqDto);
            List<TimesheetAuditDto> timesheetAudit = this.timesheetFormService.getTimesheetAudit(getCurrentUserId(), reqDto, this.jdbcClient);
            logger.info("getTimesheetAudit - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timesheetAudit.size());
            return new ResponseEntity<>(timesheetAudit, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error timesheet-audit : " + e.getMessage());
            logger.error("getTimesheetAudit - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
