package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.AcuityRuleDto;
import com.ewsv3.ews.rules.dto.req.AcuityRuleRequest;
import com.ewsv3.ews.rules.service.AcuityRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acuity-rule")
public class AcuityRuleController {
    private static final Logger logger = LoggerFactory.getLogger(AcuityRuleController.class);

    private final AcuityRuleService acuityRuleService;

    public AcuityRuleController(AcuityRuleService acuityRuleService) {
        this.acuityRuleService = acuityRuleService;
    }

    @PostMapping
    public ResponseEntity<AcuityRuleDto> create(@RequestBody AcuityRuleRequest request) {
        logger.info("Creating new AcuityRule");
        try {
            AcuityRuleDto result = acuityRuleService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating AcuityRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{acuityRuleId}")
    public ResponseEntity<AcuityRuleDto> getById(@PathVariable Long acuityRuleId) {
        logger.info("Fetching AcuityRule with id: {}", acuityRuleId);
        try {
            AcuityRuleDto result = acuityRuleService.getById(acuityRuleId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching AcuityRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AcuityRuleDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all AcuityRules for profileId: {}", profileId);
        try {
            List<AcuityRuleDto> results = acuityRuleService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityRules for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-profile/{profileId}")
    public ResponseEntity<List<AcuityRuleDto>> getByProfileId(@PathVariable Long profileId) {
        logger.info("Fetching AcuityRules for profileId: {}", profileId);
        try {
            List<AcuityRuleDto> results = acuityRuleService.getByProfileId(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityRules by profileId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-acuity-level/{acuityLevelId}")
    public ResponseEntity<List<AcuityRuleDto>> getByAcuityLevelId(@PathVariable Long acuityLevelId) {
        logger.info("Fetching AcuityRules for acuityLevelId: {}", acuityLevelId);
        try {
            List<AcuityRuleDto> results = acuityRuleService.getByAcuityLevelId(acuityLevelId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityRules by acuityLevelId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-ratio/{ratioId}")
    public ResponseEntity<List<AcuityRuleDto>> getByRatioId(@PathVariable Long ratioId) {
        logger.info("Fetching AcuityRules for ratioId: {}", ratioId);
        try {
            List<AcuityRuleDto> results = acuityRuleService.getByRatioId(ratioId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityRules by ratioId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{acuityRuleId}")
    public ResponseEntity<AcuityRuleDto> update(@PathVariable Long acuityRuleId, @RequestBody AcuityRuleRequest request) {
        logger.info("Updating AcuityRule with id: {}", acuityRuleId);
        try {
            AcuityRuleDto result = acuityRuleService.update(acuityRuleId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating AcuityRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{acuityRuleId}")
    public ResponseEntity<Void> delete(@PathVariable Long acuityRuleId) {
        logger.info("Deleting AcuityRule with id: {}", acuityRuleId);
        try {
            boolean deleted = acuityRuleService.delete(acuityRuleId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting AcuityRule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
