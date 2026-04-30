package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.commons.dto.MessageDto;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.commons.dto.PagedResponseDto;
import com.ewsv3.ews.setup.entity.LabEquipmentJobDto;
import com.ewsv3.ews.setup.service.LabEquipmentJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/setup/lab-equipment-jobs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LabEquipmentJobController {

    private static final Logger logger = LoggerFactory.getLogger(LabEquipmentJobController.class);

    @Autowired
    private LabEquipmentJobService labEquipmentJobService;

    @GetMapping
    public ResponseEntity<PagedResponseDto<LabEquipmentJobDto>> getAll(
            @RequestParam(required = false) Long labEquipmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET_LAB_EQUIPMENT_JOBS - Entry - Time: {}, LabEquipmentId: {}, Page: {}, Size: {}",
                LocalDateTime.now(), labEquipmentId, page, size);
        try {
            PagedDataDto<LabEquipmentJobDto> pagedData = labEquipmentId != null
                    ? labEquipmentJobService.findByLabEquipmentId(labEquipmentId, page, size)
                    : labEquipmentJobService.findAll(page, size);

            PagedResponseDto<LabEquipmentJobDto> response = new PagedResponseDto<>(
                    pagedData, HttpStatus.OK, new MessageDto("S", "Fetched successfully."));
            logger.info("GET_LAB_EQUIPMENT_JOBS - Exit - Time: {}, TotalElements: {}",
                    LocalDateTime.now(), pagedData.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENT_JOBS - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabEquipmentJobDto> getById(@PathVariable Long id) {
        logger.info("GET_LAB_EQUIPMENT_JOB_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentJobDto> job = labEquipmentJobService.findById(id);
            if (job.isPresent()) {
                logger.info("GET_LAB_EQUIPMENT_JOB_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(job.get());
            } else {
                logger.info("GET_LAB_EQUIPMENT_JOB_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENT_JOB_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<LabEquipmentJobDto> create(@RequestBody LabEquipmentJobDto dto) {
        logger.info("CREATE_LAB_EQUIPMENT_JOB - Entry - Time: {}", LocalDateTime.now());
        try {
            LabEquipmentJobDto saved = labEquipmentJobService.create(dto);
            logger.info("CREATE_LAB_EQUIPMENT_JOB - Exit - Time: {}, ID: {}",
                    LocalDateTime.now(), saved.getLabEquipmentsJobId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_LAB_EQUIPMENT_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabEquipmentJobDto> update(@PathVariable Long id, @RequestBody LabEquipmentJobDto dto) {
        logger.info("UPDATE_LAB_EQUIPMENT_JOB - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentJobDto> updated = labEquipmentJobService.update(id, dto);
            if (updated.isPresent()) {
                logger.info("UPDATE_LAB_EQUIPMENT_JOB - Exit - Time: {}, Updated: true", LocalDateTime.now());
                return ResponseEntity.ok(updated.get());
            } else {
                logger.info("UPDATE_LAB_EQUIPMENT_JOB - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("UPDATE_LAB_EQUIPMENT_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_LAB_EQUIPMENT_JOB - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!labEquipmentJobService.existsById(id)) {
                logger.info("DELETE_LAB_EQUIPMENT_JOB - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            labEquipmentJobService.deleteById(id);
            logger.info("DELETE_LAB_EQUIPMENT_JOB - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_LAB_EQUIPMENT_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
