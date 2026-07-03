package com.ewsv3.ews.payrollaudit.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
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
            @RequestParam String status) {

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
                    status);

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
}
