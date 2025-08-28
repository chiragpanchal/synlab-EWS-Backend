package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
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
    public ResponseEntity<List<PayCodeDto>> getPayCodes(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto){

        try{
            List<PayCodeDto> payCodes = this.timesheetFormService.getPayCodes(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(payCodes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error pay-codes : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-departments")
    public ResponseEntity<List<TsDepartmentDto>> getDepartments(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto){

        try{
            List<TsDepartmentDto> departments = this.timesheetFormService.getDepartments(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-departments : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-jobs")
    public ResponseEntity<List<TsJobDto>> getJobs(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto){

        try{
            List<TsJobDto> jobs = this.timesheetFormService.getJobs(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-jobs : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-project-tasks")
    public ResponseEntity<List<ProjectTaskDto>> getProjectTasks(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto){

        try{
            List<ProjectTaskDto> projectTasks = this.timesheetFormService.getProjectTasks(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(projectTasks, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-project-tasks : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("ts-expenditure-types")
    public ResponseEntity<List<ExpTypeDto>> getExpenditureTypes(@RequestHeader Map<String, String> headers, @RequestBody TimesheetFormReqDto reqDto){

        try{
            List<ExpTypeDto> expenditureTypes = this.timesheetFormService.getExpenditureTypes(getCurrentUserId(), reqDto, this.jdbcClient);
            return new ResponseEntity<>(expenditureTypes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error ts-expenditure-types : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
