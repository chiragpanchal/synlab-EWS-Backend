package com.ewsv3.ews.rotaDemands.controller;

import com.ewsv3.ews.commons.dto.MessageDto;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.commons.dto.PagedResponseDto;
import com.ewsv3.ews.rotaDemands.dto.RotaDemandHDto;
import com.ewsv3.ews.rotaDemands.service.RotaDemandHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/rota-demands")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RotaDemandHController {

    private static final Logger logger = LoggerFactory.getLogger(RotaDemandHController.class);

    @Autowired
    private RotaDemandHService rotaDemandHService;

    @GetMapping
    public ResponseEntity<PagedResponseDto<RotaDemandHDto>> getAll(
            @RequestParam Long profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET_ROTA_DEMANDS - Entry - Time: {}, ProfileId: {}, Page: {}, Size: {}", LocalDateTime.now(), profileId, page, size);
        try {
            PagedDataDto<RotaDemandHDto> pagedData = rotaDemandHService.findAll(profileId, page, size);
            PagedResponseDto<RotaDemandHDto> response = new PagedResponseDto<>(
                    pagedData,
                    HttpStatus.OK,
                    new MessageDto("S", "Fetched successfully.")
            );
            logger.info("GET_ROTA_DEMANDS - Exit - Time: {}, TotalElements: {}", LocalDateTime.now(), pagedData.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET_ROTA_DEMANDS - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaDemandHDto> getById(@PathVariable Long id) {
        logger.info("GET_ROTA_DEMAND_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<RotaDemandHDto> demand = rotaDemandHService.findById(id);
            if (demand.isPresent()) {
                logger.info("GET_ROTA_DEMAND_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(demand.get());
            } else {
                logger.info("GET_ROTA_DEMAND_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_ROTA_DEMAND_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RotaDemandHDto> create(@RequestBody RotaDemandHDto dto) {
        logger.info("CREATE_ROTA_DEMAND - Entry - Time: {}", LocalDateTime.now());
        try {
            RotaDemandHDto saved = rotaDemandHService.create(dto);
            logger.info("CREATE_ROTA_DEMAND - Exit - Time: {}, ID: {}", LocalDateTime.now(), saved.getRotaDemandId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_ROTA_DEMAND - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotaDemandHDto> update(@PathVariable Long id, @RequestBody RotaDemandHDto dto) {
        logger.info("UPDATE_ROTA_DEMAND - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<RotaDemandHDto> updated = rotaDemandHService.update(id, dto);
            if (updated.isPresent()) {
                logger.info("UPDATE_ROTA_DEMAND - Exit - Time: {}, Updated: true", LocalDateTime.now());
                return ResponseEntity.ok(updated.get());
            } else {
                logger.info("UPDATE_ROTA_DEMAND - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("UPDATE_ROTA_DEMAND - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_ROTA_DEMAND - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!rotaDemandHService.existsById(id)) {
                logger.info("DELETE_ROTA_DEMAND - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            rotaDemandHService.deleteById(id);
            logger.info("DELETE_ROTA_DEMAND - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_ROTA_DEMAND - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
