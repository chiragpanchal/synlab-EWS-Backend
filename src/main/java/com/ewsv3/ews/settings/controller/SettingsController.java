package com.ewsv3.ews.settings.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.settings.entity.PayCode;
import com.ewsv3.ews.settings.service.PayCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private final JdbcClient jdbcClient;

    @Autowired
    private PayCodeService payCodeService;

    public SettingsController(JdbcClient jdbcClient) {
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

    /**
     * Get all pay codes ordered by pay code name
     */
    @GetMapping("/pay-codes")
    public ResponseEntity<List<PayCode>> getAllPayCodes() {
        logger.info("GET_ALL_PAY_CODES - Entry - Time: {}", LocalDateTime.now());
        try {
            List<PayCode> payCodes = payCodeService.findAllOrderedByPayCodeName();
            logger.info("GET_ALL_PAY_CODES - Exit - Time: {}, TotalElements: {}",
                    LocalDateTime.now(), payCodes.size());
            return ResponseEntity.ok(payCodes);
        } catch (Exception e) {
            logger.error("GET_ALL_PAY_CODES - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get pay code by ID
     */
    @GetMapping("/pay-codes/{id}")
    public ResponseEntity<PayCode> getPayCodeById(@PathVariable Long id) {
        logger.info("GET_PAY_CODE_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<PayCode> payCode = payCodeService.findById(id);
            if (payCode.isPresent()) {
                logger.info("GET_PAY_CODE_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(payCode.get());
            } else {
                logger.info("GET_PAY_CODE_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_PAY_CODE_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get pay codes list for dropdown
     */
    @GetMapping("/pay-codes-list")
    public ResponseEntity<List<PayCode>> getPayCodesList(@RequestHeader Map<String, String> headers) {
        logger.info("GET_PAY_CODES_LIST - Entry - Time: {}", LocalDateTime.now());
        try {
            List<PayCode> payCodesList = payCodeService.getPayCodesList(jdbcClient);
            logger.info("GET_PAY_CODES_LIST - Exit - Time: {}, Response Count: {}", LocalDateTime.now(),
                    payCodesList.size());
            return ResponseEntity.ok(payCodesList);
        } catch (Exception exception) {
            logger.error("GET_PAY_CODES_LIST - Exception - Time: {}, Error: {}",
                    LocalDateTime.now(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new pay code
     */
    @PostMapping("/pay-codes")
    public ResponseEntity<PayCode> createPayCode(@RequestBody PayCode payCode) {
        logger.info("CREATE_PAY_CODE - Entry - Time: {}", LocalDateTime.now());
        try {
            // Check for duplicates
            if (payCodeService.existsByEnterpriseIdAndPayCode(payCode.getEnterpriseId(), payCode.getPayCode())) {
                logger.warn("CREATE_PAY_CODE - Duplicate pay code - EnterpriseId: {}, PayCode: {}",
                        payCode.getEnterpriseId(), payCode.getPayCode());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            if (payCodeService.existsByEnterpriseIdAndPayCodeName(payCode.getEnterpriseId(), payCode.getPayCodeName())) {
                logger.warn("CREATE_PAY_CODE - Duplicate pay code name - EnterpriseId: {}, PayCodeName: {}",
                        payCode.getEnterpriseId(), payCode.getPayCodeName());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Generate ID from sequence
            Long generatedPayCodeId = jdbcClient
                    .sql("SELECT PAY_CODE_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            payCode.setPayCodeId(generatedPayCodeId);
            payCode.setCreatedBy(currentUserId);
            payCode.setCreatedOn(currentDate);
            payCode.setLastUpdatedBy(currentUserId);
            payCode.setLastUpdateDate(currentDate);

            PayCode savedPayCode = payCodeService.save(payCode);
            logger.info("CREATE_PAY_CODE - Exit - Time: {}, ID: {}", LocalDateTime.now(),
                    savedPayCode.getPayCodeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPayCode);
        } catch (Exception e) {
            logger.error("CREATE_PAY_CODE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing pay code
     */
    @PutMapping("/pay-codes/{id}")
    public ResponseEntity<PayCode> updatePayCode(@PathVariable Long id, @RequestBody PayCode payCode) {
        logger.info("UPDATE_PAY_CODE - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            // Fetch existing record to preserve creation audit fields
            PayCode existingPayCode = payCodeService.findById(id).orElse(null);
            if (existingPayCode == null) {
                logger.info("UPDATE_PAY_CODE - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            // Set ID and update audit fields
            payCode.setPayCodeId(id);

            // Preserve original creation audit fields
            payCode.setCreatedBy(existingPayCode.getCreatedBy());
            payCode.setCreatedOn(existingPayCode.getCreatedOn());

            // Update modification audit fields
            payCode.setLastUpdatedBy(currentUserId);
            payCode.setLastUpdateDate(currentDate);

            PayCode updatedPayCode = payCodeService.save(payCode);
            logger.info("UPDATE_PAY_CODE - Exit - Time: {}, Updated: true", LocalDateTime.now());
            return ResponseEntity.ok(updatedPayCode);
        } catch (Exception e) {
            logger.error("UPDATE_PAY_CODE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete pay code by ID
     */
    @DeleteMapping("/pay-codes/{id}")
    public ResponseEntity<Void> deletePayCode(@PathVariable Long id) {
        logger.info("DELETE_PAY_CODE - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!payCodeService.existsById(id)) {
                logger.info("DELETE_PAY_CODE - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            payCodeService.deleteById(id);
            logger.info("DELETE_PAY_CODE - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_PAY_CODE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
