package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.AcuityLevelDto;
import com.ewsv3.ews.rules.dto.req.AcuityLevelRequest;
import com.ewsv3.ews.rules.service.AcuityLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acuity-level")
public class AcuityLevelController {
    private static final Logger logger = LoggerFactory.getLogger(AcuityLevelController.class);

    private final AcuityLevelService acuityLevelService;

    public AcuityLevelController(AcuityLevelService acuityLevelService) {
        this.acuityLevelService = acuityLevelService;
    }

    @PostMapping
    public ResponseEntity<AcuityLevelDto> create(@RequestBody AcuityLevelRequest request) {
        logger.info("Creating new AcuityLevel");
        try {
            AcuityLevelDto result = acuityLevelService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating AcuityLevel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{acuityLevelId}")
    public ResponseEntity<AcuityLevelDto> getById(@PathVariable Long acuityLevelId) {
        logger.info("Fetching AcuityLevel with id: {}", acuityLevelId);
        try {
            AcuityLevelDto result = acuityLevelService.getById(acuityLevelId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching AcuityLevel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AcuityLevelDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all AcuityLevels for profileId: {}", profileId);
        try {
            List<AcuityLevelDto> results = acuityLevelService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityLevels for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{acuityLevelId}")
    public ResponseEntity<AcuityLevelDto> update(@PathVariable Long acuityLevelId, @RequestBody AcuityLevelRequest request) {
        logger.info("Updating AcuityLevel with id: {}", acuityLevelId);
        try {
            AcuityLevelDto result = acuityLevelService.update(acuityLevelId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating AcuityLevel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{acuityLevelId}")
    public ResponseEntity<Void> delete(@PathVariable Long acuityLevelId) {
        logger.info("Deleting AcuityLevel with id: {}", acuityLevelId);
        try {
            boolean deleted = acuityLevelService.delete(acuityLevelId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting AcuityLevel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
