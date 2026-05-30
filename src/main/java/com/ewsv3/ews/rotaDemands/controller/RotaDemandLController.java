package com.ewsv3.ews.rotaDemands.controller;

import com.ewsv3.ews.commons.dto.MessageDto;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.commons.dto.PagedResponseDto;
import com.ewsv3.ews.rotaDemands.dto.JobTitleLookupDto;
import com.ewsv3.ews.rotaDemands.dto.RotaDemandLDto;
import com.ewsv3.ews.rotaDemands.service.RotaDemandLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rota-demand-lines")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RotaDemandLController {

    private static final Logger logger = LoggerFactory.getLogger(RotaDemandLController.class);

    @Autowired
    private RotaDemandLService rotaDemandLService;

    @GetMapping("/job-titles")
    public ResponseEntity<List<JobTitleLookupDto>> getJobTitlesByProfileId(@RequestParam Long profileId) {
        logger.info("GET_JOB_TITLES_BY_PROFILE - Entry - Time: {}, ProfileId: {}", LocalDateTime.now(), profileId);
        try {
            List<JobTitleLookupDto> result = rotaDemandLService.findJobTitlesByProfileId(profileId);
            logger.info("GET_JOB_TITLES_BY_PROFILE - Exit - Time: {}, Count: {}", LocalDateTime.now(), result.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("GET_JOB_TITLES_BY_PROFILE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<RotaDemandLDto>> getAllByDemandId(
            @RequestParam Long rotaDemandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET_ROTA_DEMAND_LINES - Entry - Time: {}, RotaDemandId: {}, Page: {}, Size: {}", LocalDateTime.now(), rotaDemandId, page, size);
        try {
            PagedDataDto<RotaDemandLDto> pagedData = rotaDemandLService.findAllByRotaDemandId(rotaDemandId, page, size);
            PagedResponseDto<RotaDemandLDto> response = new PagedResponseDto<>(
                    pagedData,
                    HttpStatus.OK,
                    new MessageDto("S", "Fetched successfully.")
            );
            logger.info("GET_ROTA_DEMAND_LINES - Exit - Time: {}, TotalElements: {}", LocalDateTime.now(), pagedData.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_ROTA_DEMAND_LINES - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaDemandLDto> getById(@PathVariable Long id) {
        logger.info("GET_ROTA_DEMAND_LINE_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<RotaDemandLDto> line = rotaDemandLService.findById(id);
            if (line.isPresent()) {
                logger.info("GET_ROTA_DEMAND_LINE_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(line.get());
            } else {
                logger.info("GET_ROTA_DEMAND_LINE_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_ROTA_DEMAND_LINE_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RotaDemandLDto> create(@RequestBody RotaDemandLDto dto) {
        logger.info("CREATE_ROTA_DEMAND_LINE - Entry - Time: {} req :{}", LocalDateTime.now(), dto.getDepartmentId() );
        dto.setDepartmentId(null);
        logger.info("CREATE_ROTA_DEMAND_LINE - Entry - DTOL: getDepartmentId :{} , dto.getJobTitleId :{} , getRotaDemandLineId: {} ",  dto.getDepartmentId() , dto.getJobTitleId(), dto.getRotaDemandLineId());
        try {
            RotaDemandLDto saved = rotaDemandLService.create(dto);
            logger.info("CREATE_ROTA_DEMAND_LINE - Exit - Time: {}, ID: {}", LocalDateTime.now(), saved.getRotaDemandLineId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_ROTA_DEMAND_LINE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotaDemandLDto> update(@PathVariable Long id, @RequestBody RotaDemandLDto dto) {
        logger.info("UPDATE_ROTA_DEMAND_LINE - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<RotaDemandLDto> updated = rotaDemandLService.update(id, dto);
            if (updated.isPresent()) {
                logger.info("UPDATE_ROTA_DEMAND_LINE - Exit - Time: {}, Updated: true", LocalDateTime.now());
                return ResponseEntity.ok(updated.get());
            } else {
                logger.info("UPDATE_ROTA_DEMAND_LINE - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("UPDATE_ROTA_DEMAND_LINE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_ROTA_DEMAND_LINE - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!rotaDemandLService.existsById(id)) {
                logger.info("DELETE_ROTA_DEMAND_LINE - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            rotaDemandLService.deleteById(id);
            logger.info("DELETE_ROTA_DEMAND_LINE - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_ROTA_DEMAND_LINE - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
