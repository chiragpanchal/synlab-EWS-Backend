package com.ewsv3.ews.timesheets.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.TimesheetPageRequestBody;
import com.ewsv3.ews.timesheets.dto.bulk.BulkTimesheetDetails;
import com.ewsv3.ews.timesheets.dto.bulk.BulkTimesheetMasterDto;
import com.ewsv3.ews.timesheets.dto.form.TimesheetDetails;
import com.ewsv3.ews.timesheets.service.bulk.TimesheetBulkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bulk-timesheet/")
public class TimesheetBulkController {

    private final JdbcClient jdbcClient;
    private final TimesheetBulkService timesheetBulkService;

    public TimesheetBulkController(JdbcClient jdbcClient, TimesheetBulkService timesheetBulkService) {
        this.jdbcClient = jdbcClient;
        this.timesheetBulkService = timesheetBulkService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("bulk-timesheet-masters")
    public ResponseEntity<BulkTimesheetMasterDto> getBulkTimesheetMasters(@RequestHeader Map<String, String> headers, @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int size, @RequestBody TimesheetPageRequestBody requestBody) {

        try {
            System.out.println("bulk-timesheet-masters page:" + page);
            System.out.println("bulk-timesheet-masters size:" + size);
            System.out.println("bulk-timesheet-masters requestBody:" + requestBody);
            BulkTimesheetMasterDto bulkMasters = this.timesheetBulkService.getBulkMasters(getCurrentUserId(), page, size, requestBody, this.jdbcClient);

            return new ResponseEntity<>(bulkMasters, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Error bulk-timesheet-data : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("bulk-timesheet-data")
    public ResponseEntity<List<BulkTimesheetDetails>> getBulkTimesheets(@RequestHeader Map<String, String> headers, @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "20") int size, @RequestBody TimesheetPageRequestBody requestBody) {

        try {
            System.out.println("bulk-timesheet-data page:" + page);
            System.out.println("bulk-timesheet-data size:" + size);
            System.out.println("bulk-timesheet-data requestBody:" + requestBody);
            List<BulkTimesheetDetails> bulkTimesheets = this.timesheetBulkService.getBulkTimesheets(getCurrentUserId(), requestBody, page, size, this.jdbcClient);

            return new ResponseEntity<>(bulkTimesheets, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Error bulk-timesheet-data : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("save-bulk-timesheet")
    public ResponseEntity<DMLResponseDto> getBulkTimesheets(@RequestHeader Map<String, String> headers, @RequestBody List<TimesheetDetails> requestBody) {

        try {
            DMLResponseDto dmlResponseDto = this.timesheetBulkService.saveBulkTimesheets(getCurrentUserId(), requestBody, this.jdbcClient);

            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Error save-bulk-timesheet : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
