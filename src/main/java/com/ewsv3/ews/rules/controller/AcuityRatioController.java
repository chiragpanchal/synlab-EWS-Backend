package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.rules.dto.AcuityRatioDto;
import com.ewsv3.ews.rules.dto.req.AcuityRatioRequest;
import com.ewsv3.ews.rules.service.AcuityRatioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acuity-ratio")
public class AcuityRatioController {
    private static final Logger logger = LoggerFactory.getLogger(AcuityRatioController.class);

    private final AcuityRatioService acuityRatioService;

    public AcuityRatioController(AcuityRatioService acuityRatioService) {
        this.acuityRatioService = acuityRatioService;
    }

    @PostMapping
    public ResponseEntity<AcuityRatioDto> create(@RequestBody AcuityRatioRequest request) {
        logger.info("Creating new AcuityRatio");
        try {
            AcuityRatioDto result = acuityRatioService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating AcuityRatio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{ratioId}")
    public ResponseEntity<AcuityRatioDto> getById(@PathVariable Long ratioId) {
        logger.info("Fetching AcuityRatio with id: {}", ratioId);
        try {
            AcuityRatioDto result = acuityRatioService.getById(ratioId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching AcuityRatio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AcuityRatioDto>> getAll(@RequestParam Long profileId) {
        logger.info("Fetching all AcuityRatios for profileId: {}", profileId);
        try {
            List<AcuityRatioDto> results = acuityRatioService.getAll(profileId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching AcuityRatios for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{ratioId}")
    public ResponseEntity<AcuityRatioDto> update(@PathVariable Long ratioId, @RequestBody AcuityRatioRequest request) {
        logger.info("Updating AcuityRatio with id: {}", ratioId);
        try {
            AcuityRatioDto result = acuityRatioService.update(ratioId, request);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating AcuityRatio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{ratioId}")
    public ResponseEntity<Void> delete(@PathVariable Long ratioId) {
        logger.info("Deleting AcuityRatio with id: {}", ratioId);
        try {
            boolean deleted = acuityRatioService.delete(ratioId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting AcuityRatio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
