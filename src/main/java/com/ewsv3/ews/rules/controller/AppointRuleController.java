package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.AppointRuleDto;
import com.ewsv3.ews.rules.dto.req.AppointRuleRequest;
import com.ewsv3.ews.rules.service.AppointRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appoint-rule")
public class AppointRuleController {
    private static final Logger logger = LoggerFactory.getLogger(AppointRuleController.class);

    private final AppointRuleService appointRuleService;

    public AppointRuleController(AppointRuleService appointRuleService) {
        this.appointRuleService = appointRuleService;
    }

    @PostMapping
    public ResponseEntity<AppointRuleDto> create(@RequestBody AppointRuleRequest request) {
        logger.info("Creating new AppointRule");
        try {
            AppointRuleDto result = appointRuleService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating AppointRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{appointRuleId}")
    public ResponseEntity<AppointRuleDto> getById(@PathVariable Long appointRuleId) {
        logger.info("Fetching AppointRule with id: {}", appointRuleId);
        try {
            AppointRuleDto result = appointRuleService.getById(appointRuleId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching AppointRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointRuleDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all AppointRules for profileId: {}", profileId);
        try {
            List<AppointRuleDto> results = appointRuleService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AppointRules for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-job-title/{jobTitleId}")
    public ResponseEntity<List<AppointRuleDto>> getByJobTitleId(
            @RequestParam Long profileId,
            @PathVariable Long jobTitleId) {
        logger.info("Fetching AppointRules for profileId: {}, jobTitleId: {}", profileId, jobTitleId);
        try {
            List<AppointRuleDto> results = appointRuleService.getByJobTitleId(profileId, jobTitleId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AppointRules by jobTitleId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{appointRuleId}")
    public ResponseEntity<AppointRuleDto> update(@PathVariable Long appointRuleId, @RequestBody AppointRuleRequest request) {
        logger.info("Updating AppointRule with id: {}", appointRuleId);
        try {
            AppointRuleDto result = appointRuleService.update(appointRuleId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating AppointRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{appointRuleId}")
    public ResponseEntity<Void> delete(@PathVariable Long appointRuleId) {
        logger.info("Deleting AppointRule with id: {}", appointRuleId);
        try {
            boolean deleted = appointRuleService.delete(appointRuleId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting AppointRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
