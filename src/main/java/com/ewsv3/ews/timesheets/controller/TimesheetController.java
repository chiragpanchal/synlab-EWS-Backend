package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.timesheets.dto.TimesheetKpi;
import com.ewsv3.ews.timesheets.dto.TimesheetPageRequestBody;
import com.ewsv3.ews.timesheets.dto.TimesheetPageResponseBody;
import com.ewsv3.ews.timesheets.service.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timesheet/")
public class TimesheetController {

    private static final Logger logger = LoggerFactory.getLogger(TimesheetController.class);

    public final TimesheetService timesheetService;
    public final JdbcClient jdbcClient;


    public TimesheetController(TimesheetService timesheetService, JdbcClient jdbcClient) {
        this.timesheetService = timesheetService;
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


    @PostMapping("page-table-data")
    public ResponseEntity<List<TimesheetPageResponseBody>> getTimesheetTableData(@RequestHeader Map<String, String> headers,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "100") int size,
                                                                                 @RequestParam(defaultValue = "") String text,
                                                                                 @RequestParam(defaultValue = "") String filterFlag,
                                                                                 @RequestParam(defaultValue = "") String payCodeName,
                                                                                 @RequestBody TimesheetPageRequestBody requestBody) {

        logger.info("getTimesheetTableData - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {
            //System.out.println("page-table-data > page:" + page);
            //System.out.println("page-table-data > size:" + size);
            //System.out.println("page-table-data > text:" + text);
            //System.out.println("page-table-data > filterFlag:" + filterFlag);
            //System.out.println("page-table-data > payCodeName:" + payCodeName);
            //System.out.println("page-table-data requestBody:" + requestBody);
            List<TimesheetPageResponseBody> timesheetData = this.timesheetService.getTimesheetData(
                    getCurrentUserId(),
                    page,
                    size,
                    text,
                    filterFlag,
                    payCodeName,
                    requestBody, this.jdbcClient);
            logger.info("getTimesheetTableData - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), timesheetData.size());
            return new ResponseEntity<>(timesheetData, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error page-table-data : " + e.getMessage());
            logger.error("getTimesheetTableData - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), requestBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timesheet-kpi")
    public ResponseEntity<TimesheetKpi> getTimesheetKpi(@RequestHeader Map<String, String> headers,
                                                        @RequestParam(defaultValue = "") String text,
                                                        @RequestParam(defaultValue = "") String filterFlag,
                                                        @RequestParam(defaultValue = "") String payCodeName,
                                                        @RequestBody TimesheetPageRequestBody requestBody) {

        logger.info("getTimesheetKpi - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {
            //System.out.println("timesheet-kpi requestBody:" + requestBody);
            //System.out.println("timesheet-kpi filterFlag:" + filterFlag);
            //System.out.println("timesheet-kpi payCodeName:" + payCodeName);
            TimesheetKpi timesheetKpi = this.timesheetService.getTimesheetKpi(
                    getCurrentUserId(),
                    text,
                    filterFlag,
                    payCodeName,
                    requestBody,
                    this.jdbcClient);
            logger.info("getTimesheetKpi - Exit - Time: {}, Response: {}", LocalDateTime.now(), timesheetKpi);
            return new ResponseEntity<>(timesheetKpi, HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("Error timesheet-kpi : " + e.getMessage());
            logger.error("getTimesheetKpi - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), requestBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
