package com.ewsv3.ews.reports.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.reports.dto.UserProfileDateReqDto;
import com.ewsv3.ews.reports.dto.productivity.TimecardRespDto;
import com.ewsv3.ews.reports.dto.productivity.TimesheetRespDto;
import com.ewsv3.ews.reports.service.productivity.ProductivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productivity")
public class ProductivityController {

    private final JdbcClient jdbcClient;
    private final ProductivityService productivityService;

    public ProductivityController(JdbcClient jdbcClient, ProductivityService productivityService) {
        this.jdbcClient = jdbcClient;
        this.productivityService = productivityService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("timecard-info")
    public ResponseEntity<List<TimecardRespDto>> getProductivityTimecardInfo(@RequestHeader Map<String, String> header, @RequestBody UserProfileDateReqDto reqDto) {

        try {
            List<TimecardRespDto> productivityTimecardInfo = this.productivityService.getProductivityTimecardInfo(getCurrentUserId(), reqDto, jdbcClient);
            return new ResponseEntity<>(productivityTimecardInfo, HttpStatus.OK);
        } catch (Error error) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("timesheet-info")
    public ResponseEntity<List<TimesheetRespDto>> getProductivityTimesheetInfo(@RequestHeader Map<String, String> header, @RequestBody UserProfileDateReqDto reqDto) {

        try {
            List<TimesheetRespDto> productivityTimesheetInfo = this.productivityService.getProductivityTimesheetInfo(getCurrentUserId(), reqDto, jdbcClient);
            return new ResponseEntity<>(productivityTimesheetInfo, HttpStatus.OK);
        } catch (Error error) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
