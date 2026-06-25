package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.OccupyRuleDto;
import com.ewsv3.ews.rules.dto.req.OccupyRuleRequest;
import com.ewsv3.ews.rules.service.OccupyRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/occupy-rule")
public class OccupyRuleController {
    private static final Logger logger = LoggerFactory.getLogger(OccupyRuleController.class);

    private final OccupyRuleService occupyRuleService;

    public OccupyRuleController(OccupyRuleService occupyRuleService) {
        this.occupyRuleService = occupyRuleService;
    }

    @PostMapping
    public ResponseEntity<OccupyRuleDto> create(@RequestBody OccupyRuleRequest request) {
        logger.info("Creating new OccupyRule");
        try {
            OccupyRuleDto result = occupyRuleService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating OccupyRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{occupyRuleId}")
    public ResponseEntity<OccupyRuleDto> getById(@PathVariable Long occupyRuleId) {
        logger.info("Fetching OccupyRule with id: {}", occupyRuleId);
        try {
            OccupyRuleDto result = occupyRuleService.getById(occupyRuleId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching OccupyRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OccupyRuleDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all OccupyRules for profileId: {}", profileId);
        try {
            List<OccupyRuleDto> results = occupyRuleService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching OccupyRules for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-job-title/{jobTitleId}")
    public ResponseEntity<List<OccupyRuleDto>> getByJobTitleId(
            @RequestParam Long profileId,
            @PathVariable Long jobTitleId) {
        logger.info("Fetching OccupyRules for profileId: {}, jobTitleId: {}", profileId, jobTitleId);
        try {
            List<OccupyRuleDto> results = occupyRuleService.getByJobTitleId(profileId, jobTitleId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching OccupyRules by jobTitleId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-work-duration/{workDurationId}")
    public ResponseEntity<List<OccupyRuleDto>> getByWorkDurationId(@PathVariable Long workDurationId) {
        logger.info("Fetching OccupyRules for workDurationId: {}", workDurationId);
        try {
            List<OccupyRuleDto> results = occupyRuleService.getByWorkDurationId(workDurationId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching OccupyRules by workDurationId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{occupyRuleId}")
    public ResponseEntity<OccupyRuleDto> update(@PathVariable Long occupyRuleId, @RequestBody OccupyRuleRequest request) {
        logger.info("Updating OccupyRule with id: {}", occupyRuleId);
        try {
            OccupyRuleDto result = occupyRuleService.update(occupyRuleId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating OccupyRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{occupyRuleId}")
    public ResponseEntity<Void> delete(@PathVariable Long occupyRuleId) {
        logger.info("Deleting OccupyRule with id: {}", occupyRuleId);
        try {
            boolean deleted = occupyRuleService.delete(occupyRuleId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting OccupyRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
