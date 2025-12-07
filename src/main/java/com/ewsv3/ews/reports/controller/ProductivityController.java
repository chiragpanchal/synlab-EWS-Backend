package com.ewsv3.ews.reports.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.reports.dto.UserProfileDateReqDto;
import com.ewsv3.ews.reports.dto.productivity.TimecardRespDto;
import com.ewsv3.ews.reports.dto.productivity.TimesheetRespDto;
import com.ewsv3.ews.reports.service.productivity.ProductivityService;
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
@RequestMapping("/api/productivity")
public class ProductivityController {
    private static final Logger logger = LoggerFactory.getLogger(ProductivityController.class);

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
        logger.info("GET_PRODUCTIVITY_TIMECARD_INFO - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);
        try {
            List<TimecardRespDto> productivityTimecardInfo = this.productivityService.getProductivityTimecardInfo(getCurrentUserId(), reqDto, jdbcClient);
            logger.info("GET_PRODUCTIVITY_TIMECARD_INFO - Exit - Time: {}, Request: {}, Response Count: {}", LocalDateTime.now(), reqDto, productivityTimecardInfo.size());
            return new ResponseEntity<>(productivityTimecardInfo, HttpStatus.OK);
        } catch (Error error) {
            logger.error("GET_PRODUCTIVITY_TIMECARD_INFO - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("timesheet-info")
    public ResponseEntity<List<TimesheetRespDto>> getProductivityTimesheetInfo(@RequestHeader Map<String, String> header, @RequestBody UserProfileDateReqDto reqDto) {
        logger.info("GET_PRODUCTIVITY_TIMESHEET_INFO - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);
        try {
            List<TimesheetRespDto> productivityTimesheetInfo = this.productivityService.getProductivityTimesheetInfo(getCurrentUserId(), reqDto, jdbcClient);
            logger.info("GET_PRODUCTIVITY_TIMESHEET_INFO - Exit - Time: {}, Request: {}, Response Count: {}", LocalDateTime.now(), reqDto, productivityTimesheetInfo.size());
            return new ResponseEntity<>(productivityTimesheetInfo, HttpStatus.OK);
        } catch (Error error) {
            logger.error("GET_PRODUCTIVITY_TIMESHEET_INFO - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqDto, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
