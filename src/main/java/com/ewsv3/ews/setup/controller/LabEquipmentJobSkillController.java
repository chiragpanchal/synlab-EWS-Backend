package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.commons.dto.MessageDto;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.commons.dto.PagedResponseDto;
import com.ewsv3.ews.setup.entity.LabEquipmentJobSkillDto;
import com.ewsv3.ews.setup.service.LabEquipmentJobSkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/setup/lab-equipment-job-skills")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LabEquipmentJobSkillController {

    private static final Logger logger = LoggerFactory.getLogger(LabEquipmentJobSkillController.class);

    @Autowired
    private LabEquipmentJobSkillService labEquipmentJobSkillService;

    @GetMapping
    public ResponseEntity<PagedResponseDto<LabEquipmentJobSkillDto>> getAll(
            @RequestParam(required = false) Long labEquipmentsJobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET_LAB_EQUIPMENT_JOB_SKILLS - Entry - Time: {}, LabEquipmentsJobId: {}, Page: {}, Size: {}",
                LocalDateTime.now(), labEquipmentsJobId, page, size);
        try {
            PagedDataDto<LabEquipmentJobSkillDto> pagedData = labEquipmentsJobId != null
                    ? labEquipmentJobSkillService.findByLabEquipmentsJobId(labEquipmentsJobId, page, size)
                    : labEquipmentJobSkillService.findAll(page, size);

            PagedResponseDto<LabEquipmentJobSkillDto> response = new PagedResponseDto<>(
                    pagedData, HttpStatus.OK, new MessageDto("S", "Fetched successfully."));
            logger.info("GET_LAB_EQUIPMENT_JOB_SKILLS - Exit - Time: {}, TotalElements: {}",
                    LocalDateTime.now(), pagedData.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENT_JOB_SKILLS - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabEquipmentJobSkillDto> getById(@PathVariable Long id) {
        logger.info("GET_LAB_EQUIPMENT_JOB_SKILL_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentJobSkillDto> skill = labEquipmentJobSkillService.findById(id);
            if (skill.isPresent()) {
                logger.info("GET_LAB_EQUIPMENT_JOB_SKILL_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(skill.get());
            } else {
                logger.info("GET_LAB_EQUIPMENT_JOB_SKILL_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_LAB_EQUIPMENT_JOB_SKILL_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<LabEquipmentJobSkillDto> create(@RequestBody LabEquipmentJobSkillDto dto) {
        logger.info("CREATE_LAB_EQUIPMENT_JOB_SKILL - Entry - Time: {}", LocalDateTime.now());
        try {
            LabEquipmentJobSkillDto saved = labEquipmentJobSkillService.create(dto);
            logger.info("CREATE_LAB_EQUIPMENT_JOB_SKILL - Exit - Time: {}, ID: {}",
                    LocalDateTime.now(), saved.getLabEquipmentsJobSkillId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_LAB_EQUIPMENT_JOB_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabEquipmentJobSkillDto> update(@PathVariable Long id,
            @RequestBody LabEquipmentJobSkillDto dto) {
        logger.info("UPDATE_LAB_EQUIPMENT_JOB_SKILL - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<LabEquipmentJobSkillDto> updated = labEquipmentJobSkillService.update(id, dto);
            if (updated.isPresent()) {
                logger.info("UPDATE_LAB_EQUIPMENT_JOB_SKILL - Exit - Time: {}, Updated: true", LocalDateTime.now());
                return ResponseEntity.ok(updated.get());
            } else {
                logger.info("UPDATE_LAB_EQUIPMENT_JOB_SKILL - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("UPDATE_LAB_EQUIPMENT_JOB_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_LAB_EQUIPMENT_JOB_SKILL - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!labEquipmentJobSkillService.existsById(id)) {
                logger.info("DELETE_LAB_EQUIPMENT_JOB_SKILL - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            labEquipmentJobSkillService.deleteById(id);
            logger.info("DELETE_LAB_EQUIPMENT_JOB_SKILL - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_LAB_EQUIPMENT_JOB_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
