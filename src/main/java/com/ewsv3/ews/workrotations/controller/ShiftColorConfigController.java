package com.ewsv3.ews.workrotations.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.workrotations.dto.ShiftColorConfigDto;
import com.ewsv3.ews.workrotations.dto.ShiftColorConfigRequestBody;
import com.ewsv3.ews.workrotations.service.ShiftColorConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/shift-color-configs/")
public class ShiftColorConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ShiftColorConfigController.class);

    private final ShiftColorConfigService shiftColorConfigService;

    public ShiftColorConfigController(ShiftColorConfigService shiftColorConfigService) {
        this.shiftColorConfigService = shiftColorConfigService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping("all")
    public ResponseEntity<List<ShiftColorConfigDto>> getAllShiftColorConfigs(@RequestHeader Map<String, String> headers) {
        logger.info("GET_ALL_SHIFT_COLOR_CONFIGS - Entry - Time: {}", LocalDateTime.now());
        try {
            List<ShiftColorConfigDto> shiftColorConfigs = shiftColorConfigService.getAllShiftColorConfigs();
            logger.info("GET_ALL_SHIFT_COLOR_CONFIGS - Exit - Time: {}, Response count: {}", LocalDateTime.now(), shiftColorConfigs.size());
            return new ResponseEntity<>(shiftColorConfigs, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("GET_ALL_SHIFT_COLOR_CONFIGS - Exception - Time: {}, Error: {}",
                    LocalDateTime.now(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ShiftColorConfigDto> getShiftColorConfigById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("GET_SHIFT_COLOR_CONFIG_BY_ID - Entry - Time: {}, Id: {}", LocalDateTime.now(), id);
        try {
            Optional<ShiftColorConfigDto> shiftColorConfig = shiftColorConfigService.getShiftColorConfigById(id);
            if (shiftColorConfig.isPresent()) {
                logger.info("GET_SHIFT_COLOR_CONFIG_BY_ID - Exit - Time: {}, Response: {}", LocalDateTime.now(), shiftColorConfig.get());
                return new ResponseEntity<>(shiftColorConfig.get(), HttpStatus.OK);
            } else {
                logger.info("GET_SHIFT_COLOR_CONFIG_BY_ID - Exit - Time: {}, Id: {}, Status: NOT_FOUND", LocalDateTime.now(), id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("GET_SHIFT_COLOR_CONFIG_BY_ID - Exception - Time: {}, Id: {}, Error: {}",
                    LocalDateTime.now(), id, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("by-time")
    public ResponseEntity<List<ShiftColorConfigDto>> getShiftColorConfigsByTime(@RequestHeader Map<String, String> headers, @RequestParam Double time) {
        logger.info("GET_SHIFT_COLOR_CONFIGS_BY_TIME - Entry - Time: {}, SearchTime: {}", LocalDateTime.now(), time);
        try {
            List<ShiftColorConfigDto> shiftColorConfigs = shiftColorConfigService.getShiftColorConfigsByTime(time);
            logger.info("GET_SHIFT_COLOR_CONFIGS_BY_TIME - Exit - Time: {}, Response count: {}", LocalDateTime.now(), shiftColorConfigs.size());
            return new ResponseEntity<>(shiftColorConfigs, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("GET_SHIFT_COLOR_CONFIGS_BY_TIME - Exception - Time: {}, SearchTime: {}, Error: {}",
                    LocalDateTime.now(), time, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createShiftColorConfig(@RequestHeader Map<String, String> headers, @RequestBody ShiftColorConfigRequestBody request) {
        logger.info("CREATE_SHIFT_COLOR_CONFIG - Entry - Time: {}, Request: {}", LocalDateTime.now(), request);
        try {
            DMLResponseDto response = shiftColorConfigService.createShiftColorConfig(getCurrentUserId(), request);
            logger.info("CREATE_SHIFT_COLOR_CONFIG - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            logger.error("CREATE_SHIFT_COLOR_CONFIG - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), request, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DMLResponseDto> updateShiftColorConfig(@RequestHeader Map<String, String> headers, @PathVariable Long id, @RequestBody ShiftColorConfigRequestBody request) {
        logger.info("UPDATE_SHIFT_COLOR_CONFIG - Entry - Time: {}, Id: {}, Request: {}", LocalDateTime.now(), id, request);
        try {
            DMLResponseDto response = shiftColorConfigService.updateShiftColorConfig(getCurrentUserId(), id, request);
            logger.info("UPDATE_SHIFT_COLOR_CONFIG - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            logger.error("UPDATE_SHIFT_COLOR_CONFIG - Exception - Time: {}, Id: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), id, request, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<DMLResponseDto> deleteShiftColorConfig(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("DELETE_SHIFT_COLOR_CONFIG - Entry - Time: {}, Id: {}", LocalDateTime.now(), id);
        try {
            DMLResponseDto response = shiftColorConfigService.deleteShiftColorConfig(id);
            logger.info("DELETE_SHIFT_COLOR_CONFIG - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            logger.error("DELETE_SHIFT_COLOR_CONFIG - Exception - Time: {}, Id: {}, Error: {}",
                    LocalDateTime.now(), id, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
