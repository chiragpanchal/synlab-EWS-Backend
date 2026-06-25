package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.AcuityJobMappingDto;
import com.ewsv3.ews.rules.dto.req.AcuityJobMappingRequest;
import com.ewsv3.ews.rules.service.AcuityJobMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acuity-job-mapping")
public class AcuityJobMappingController {
    private static final Logger logger = LoggerFactory.getLogger(AcuityJobMappingController.class);

    private final AcuityJobMappingService acuityJobMappingService;

    public AcuityJobMappingController(AcuityJobMappingService acuityJobMappingService) {
        this.acuityJobMappingService = acuityJobMappingService;
    }

    @PostMapping
    public ResponseEntity<AcuityJobMappingDto> create(@RequestBody AcuityJobMappingRequest request) {
        logger.info("Creating new AcuityJobMapping");
        try {
            AcuityJobMappingDto result = acuityJobMappingService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating AcuityJobMapping", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{acuityJobMappingId}")
    public ResponseEntity<AcuityJobMappingDto> getById(@PathVariable Long acuityJobMappingId) {
        logger.info("Fetching AcuityJobMapping with id: {}", acuityJobMappingId);
        try {
            AcuityJobMappingDto result = acuityJobMappingService.getById(acuityJobMappingId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching AcuityJobMapping", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AcuityJobMappingDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all AcuityJobMappings for profileId: {}", profileId);
        try {
            List<AcuityJobMappingDto> results = acuityJobMappingService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityJobMappings for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-for-job-title/{forJobTitleId}")
    public ResponseEntity<List<AcuityJobMappingDto>> getByForJobTitleId(
            @RequestParam Long profileId,
            @PathVariable Long forJobTitleId) {
        logger.info("Fetching AcuityJobMappings for profileId: {}, forJobTitleId: {}", profileId, forJobTitleId);
        try {
            List<AcuityJobMappingDto> results = acuityJobMappingService.getByForJobTitleId(profileId, forJobTitleId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityJobMappings by forJobTitleId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-req-job-title/{reqJobTitleId}")
    public ResponseEntity<List<AcuityJobMappingDto>> getByReqJobTitleId(
            @RequestParam Long profileId,
            @PathVariable Long reqJobTitleId) {
        logger.info("Fetching AcuityJobMappings for profileId: {}, reqJobTitleId: {}", profileId, reqJobTitleId);
        try {
            List<AcuityJobMappingDto> results = acuityJobMappingService.getByReqJobTitleId(profileId, reqJobTitleId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityJobMappings by reqJobTitleId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{acuityJobMappingId}")
    public ResponseEntity<AcuityJobMappingDto> update(@PathVariable Long acuityJobMappingId,
                                                       @RequestBody AcuityJobMappingRequest request) {
        logger.info("Updating AcuityJobMapping with id: {}", acuityJobMappingId);
        try {
            AcuityJobMappingDto result = acuityJobMappingService.update(acuityJobMappingId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating AcuityJobMapping", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{acuityJobMappingId}")
    public ResponseEntity<Void> delete(@PathVariable Long acuityJobMappingId) {
        logger.info("Deleting AcuityJobMapping with id: {}", acuityJobMappingId);
        try {
            boolean deleted = acuityJobMappingService.delete(acuityJobMappingId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting AcuityJobMapping", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
