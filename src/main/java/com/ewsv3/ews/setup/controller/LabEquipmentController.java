package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.commons.dto.MessageDto;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.commons.dto.PagedResponseDto;
import com.ewsv3.ews.setup.entity.LabEquipmentDto;
import com.ewsv3.ews.setup.service.LabEquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/setup/lab-equipments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LabEquipmentController {

    private static final Logger logger = LoggerFactory.getLogger(LabEquipmentController.class);

    @Autowired
    private LabEquipmentService labEquipmentService;

    @GetMapping
    public ResponseEntity<PagedResponseDto<LabEquipmentDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET_LAB_EQUIPMENTS - Entry - Time: {}, Page: {}, Size: {}", LocalDateTime.now(), page, size);
        try {
            PagedDataDto<LabEquipmentDto> pagedData = labEquipmentService.findAll(page, size);
            PagedResponseDto<LabEquipmentDto> response = new PagedResponseDto<>(
                    pagedData,
                    HttpStatus.OK,
                    new MessageDto("S", "Fetched successfully.")
            );
            logger.info("GET_LAB_EQUIPMENTS - Exit - Time: {}, TotalElements: {}", LocalDateTime.now(), pagedData.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENTS - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabEquipmentDto> getById(@PathVariable Long id) {
        logger.info("GET_LAB_EQUIPMENT_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentDto> equipment = labEquipmentService.findById(id);
            if (equipment.isPresent()) {
                logger.info("GET_LAB_EQUIPMENT_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(equipment.get());
            } else {
                logger.info("GET_LAB_EQUIPMENT_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENT_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<LabEquipmentDto> create(@RequestBody LabEquipmentDto dto) {
        logger.info("CREATE_LAB_EQUIPMENT - Entry - Time: {}", LocalDateTime.now());
        try {
            LabEquipmentDto saved = labEquipmentService.create(dto);
            logger.info("CREATE_LAB_EQUIPMENT - Exit - Time: {}, ID: {}", LocalDateTime.now(), saved.getLabEquipmentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_LAB_EQUIPMENT - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabEquipmentDto> update(@PathVariable Long id, @RequestBody LabEquipmentDto dto) {
        logger.info("UPDATE_LAB_EQUIPMENT - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentDto> updated = labEquipmentService.update(id, dto);
            if (updated.isPresent()) {
                logger.info("UPDATE_LAB_EQUIPMENT - Exit - Time: {}, Updated: true", LocalDateTime.now());
                return ResponseEntity.ok(updated.get());
            } else {
                logger.info("UPDATE_LAB_EQUIPMENT - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("UPDATE_LAB_EQUIPMENT - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_LAB_EQUIPMENT - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!labEquipmentService.existsById(id)) {
                logger.info("DELETE_LAB_EQUIPMENT - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            labEquipmentService.deleteById(id);
            logger.info("DELETE_LAB_EQUIPMENT - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_LAB_EQUIPMENT - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
