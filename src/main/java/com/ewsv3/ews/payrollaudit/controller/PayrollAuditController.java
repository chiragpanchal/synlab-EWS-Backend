package com.ewsv3.ews.payrollaudit.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.payrollaudit.dto.*;
import com.ewsv3.ews.payrollaudit.service.PayrollAuditService;
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

@RestController
@RequestMapping("/api/payroll-audit")
public class PayrollAuditController {

    private final PayrollAuditService payrollAuditService;
    private final JdbcClient jdbcClient;
    private static final Logger logger = LoggerFactory.getLogger(PayrollAuditController.class);

    public PayrollAuditController(PayrollAuditService payrollAuditService, JdbcClient jdbcClient) {
        this.payrollAuditService = payrollAuditService;
        this.jdbcClient = jdbcClient;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        return null;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getPayrollAuditSummary(
            @RequestParam Long payPeriodId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long jobTitleId,
            @RequestParam(required = false) String payCodes,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long gradeId,
            @RequestParam(required = false) String assignmentNumber,
            @RequestParam(required = false) String status) {

        logger.info("getPayrollAuditSummary - Entry - Time: {}, payPeriodId: {}, status: {}",
                   LocalDateTime.now(), payPeriodId, status);


        logger.info("getPayrollAuditSummary - Entry - Time: {}, departmentId: {}, jobTitleId: {} , payCodes: {}, locationId: {}, gradeId: {}, assignmentNumber: {}  ",
                LocalDateTime.now(), departmentId, jobTitleId, payCodes, locationId,gradeId,assignmentNumber   );

        try {
            Long userId = getCurrentUserId();
            List<PayrollAuditSummaryDto> summaryList = payrollAuditService.getPayrollAuditSummary(
                    userId,
                    payPeriodId,
                    departmentId,
                    jobTitleId,
                    payCodes,
                    locationId,
                    gradeId,
                    assignmentNumber,
                    status,
                    jdbcClient);

            logger.info("getPayrollAuditSummary - Exit - Time: {}, recordCount: {}",
                       LocalDateTime.now(), summaryList.size());
            return ResponseEntity.ok(summaryList);
        } catch (Exception e) {
            logger.error("getPayrollAuditSummary - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching payroll audit summary: " + e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getPayrollAuditDetails(
            @RequestParam Long payPeriodId,
            @RequestParam String assignmentNumber,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long jobTitleId,
            @RequestParam(required = false) String payCodes,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long gradeId,
            @RequestParam String status) {

        logger.info("getPayrollAuditDetails - Entry - Time: {}, payPeriodId: {}, assignmentNumber: {}, status: {}",
                   LocalDateTime.now(), payPeriodId, assignmentNumber, status);

        try {
            Long userId = getCurrentUserId();
            List<PayrollAuditDetailsDto> detailsList = payrollAuditService.getPayrollAuditDetails(
                    userId,
                    payPeriodId,
                    departmentId,
                    jobTitleId,
                    payCodes,
                    locationId,
                    gradeId,
                    assignmentNumber,
                    status);

            logger.info("getPayrollAuditDetails - Exit - Time: {}, recordCount: {}",
                       LocalDateTime.now(), detailsList.size());
            return ResponseEntity.ok(detailsList);
        } catch (Exception e) {
            logger.error("getPayrollAuditDetails - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching payroll audit details: " + e.getMessage());
        }
    }

    @GetMapping("/periods")
    public ResponseEntity<?> getPayPeriods() {

        logger.info("getPayPeriods - Entry - Time: {}", LocalDateTime.now());

        try {
            List<PayPeriodDto> periodsList = payrollAuditService.getPayPeriods(jdbcClient);

            logger.info("getPayPeriods - Exit - Time: {}, recordCount: {}",
                       LocalDateTime.now(), periodsList.size());
            return ResponseEntity.ok(periodsList);
        } catch (Exception e) {
            logger.error("getPayPeriods - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching pay periods: " + e.getMessage());
        }
    }

    @GetMapping("/masters")
    public ResponseEntity<?> getPayrollAuditMasters() {

        logger.info("getPayrollAuditMasters - Entry - Time: {}", LocalDateTime.now());

        try {
            PayrollAuditMastersDto masters = payrollAuditService.getPayrollAuditMasters(jdbcClient);

            logger.info("getPayrollAuditMasters - Exit - Time: {}, payCodes: {}, departments: {}, jobTitles: {}, locations: {}, grades: {}",
                       LocalDateTime.now(),
                       masters.payCodes().size(),
                       masters.departments().size(),
                       masters.jobTitles().size(),
                       masters.locations().size(),
                       masters.grades().size());
            return ResponseEntity.ok(masters);
        } catch (Exception e) {
            logger.error("getPayrollAuditMasters - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching payroll audit masters: " + e.getMessage());
        }
    }

    @PostMapping("/populate")
    public ResponseEntity<?> populatePayrollAuditData(@RequestBody PopulatePayrollAuditReqDto reqDto) {

        logger.info("populatePayrollAuditData - Entry - Time: {}, payPeriodId: {}",
                   LocalDateTime.now(), reqDto.payPeriodId());

        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not authenticated");
            }

            String result = payrollAuditService.populatePayrollAuditData(userId, reqDto.payPeriodId());

            logger.info("populatePayrollAuditData - Exit - Time: {}, result: {}",
                       LocalDateTime.now(), result);
            return ResponseEntity.ok(new DMLResponseDto("S", result));
        } catch (Exception e) {
            logger.error("populatePayrollAuditData - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DMLResponseDto("E", "Error populating payroll audit data: " + e.getMessage()));
        }
    }

    @PostMapping("/line/update")
    public ResponseEntity<?> updatePayrollAuditLine(@RequestBody UpdatePayrollAuditLineReqDto reqDto) {

        logger.info("updatePayrollAuditLine - Entry - Time: {}, payrollAuditLineId: {}, nPayCodeId: {}, nHours: {}",
                   LocalDateTime.now(), reqDto.payrollAuditLineId(), reqDto.nPayCodeId(), reqDto.nHours());

        try {
            int rowsAffected = payrollAuditService.updatePayrollAuditLine(reqDto, jdbcClient);

            if (rowsAffected > 0) {
                logger.info("updatePayrollAuditLine - Exit - Time: {}, rowsAffected: {}",
                           LocalDateTime.now(), rowsAffected);
                return ResponseEntity.ok(new DMLResponseDto("S", "Payroll audit line updated successfully"));
            } else {
                logger.warn("updatePayrollAuditLine - No records updated for payrollAuditLineId: {}",
                           reqDto.payrollAuditLineId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DMLResponseDto("E", "Payroll audit line not found"));
            }
        } catch (Exception e) {
            logger.error("updatePayrollAuditLine - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DMLResponseDto("E", "Error updating payroll audit line: " + e.getMessage()));
        }
    }

    @PostMapping("/action")
    public ResponseEntity<?> performPayrollAuditAction(@RequestBody PayrollAuditActionReqDto reqDto) {

        logger.info("performPayrollAuditAction - Entry - Time: {}, payPeriodId: {}, status: {}, action: {}",
                   LocalDateTime.now(), reqDto.payPeriodId(), reqDto.status(), reqDto.action());

        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not authenticated");
            }

            String result = payrollAuditService.performPayrollAuditAction(userId, reqDto);

            logger.info("performPayrollAuditAction - Exit - Time: {}, result: {}",
                       LocalDateTime.now(), result);
            return ResponseEntity.ok(new DMLResponseDto("S", result));
        } catch (Exception e) {
            logger.error("performPayrollAuditAction - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DMLResponseDto("E", "Error performing payroll audit action: " + e.getMessage()));
        }
    }

    @GetMapping("/logs/{payrollAuditId}")
    public ResponseEntity<?> getPayrollAuditLogs(@PathVariable Long payrollAuditId) {

        logger.info("getPayrollAuditLogs - Entry - Time: {}, payrollAuditId: {}",
                   LocalDateTime.now(), payrollAuditId);

        try {
            List<PayrollAuditLogDto> logsList = payrollAuditService.getPayrollAuditLogs(payrollAuditId, jdbcClient);

            logger.info("getPayrollAuditLogs - Exit - Time: {}, recordCount: {}",
                       LocalDateTime.now(), logsList.size());
            return ResponseEntity.ok(logsList);
        } catch (Exception e) {
            logger.error("getPayrollAuditLogs - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching payroll audit logs: " + e.getMessage());
        }
    }
}
