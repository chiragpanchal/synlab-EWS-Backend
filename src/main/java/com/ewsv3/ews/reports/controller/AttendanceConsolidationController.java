package com.ewsv3.ews.reports.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.reports.dto.attendanceConsolidation.AttendConsReqBody;
import com.ewsv3.ews.reports.dto.attendanceConsolidation.AttendanceConsolidationResp;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportReqDto;
import com.ewsv3.ews.reports.service.attendanceConsolidation.AttendConsService;
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
public class AttendanceConsolidationController {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceConsolidationController.class);

    private final JdbcClient jdbcClient;
    private final AttendConsService attendConsService;

    public AttendanceConsolidationController(JdbcClient jdbcClient, AttendConsService attendConsService) {
        this.jdbcClient = jdbcClient;
        this.attendConsService = attendConsService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("attend-cons")
    public ResponseEntity<List<AttendanceConsolidationResp>> getAttendanceConsolidationReport(@RequestHeader Map<String, String> header,
                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "50") int size,
                                                                                              @RequestBody AttendConsReqBody reqDto) {
        logger.info("GET_ATTENDANCE_CONSOLIDATION_REPORT - Entry - Time: {}, Page: {}, Size: {}, Request: {}", LocalDateTime.now(), page, size, reqDto);
        try {

            //System.out.println("attend-cons page:" + page);
            //System.out.println("attend-cons size:" + size);
            //System.out.println("attend-cons reqDto:" + reqDto);

            List<AttendanceConsolidationResp> attendanceConsData = attendConsService.getAttendanceConsData(getCurrentUserId(), page, size, reqDto, this.jdbcClient);
            logger.info("GET_ATTENDANCE_CONSOLIDATION_REPORT - Exit - Time: {}, Page: {}, Size: {}, Response Count: {}", LocalDateTime.now(), page, size, attendanceConsData.size());
            return new ResponseEntity<>(attendanceConsData, HttpStatus.OK);


        } catch (Exception e) {
            logger.error("GET_ATTENDANCE_CONSOLIDATION_REPORT - Exception - Time: {}, Page: {}, Size: {}, Request: {}, Error: {}", LocalDateTime.now(), page, size, reqDto, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
