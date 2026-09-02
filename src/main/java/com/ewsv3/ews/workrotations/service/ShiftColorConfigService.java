package com.ewsv3.ews.workrotations.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.workrotations.dto.ShiftColorConfigDto;
import com.ewsv3.ews.workrotations.dto.ShiftColorConfigRequestBody;
import com.ewsv3.ews.workrotations.entity.ShiftColorConfig;
import com.ewsv3.ews.workrotations.repository.ShiftColorConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShiftColorConfigService {

    private final ShiftColorConfigRepository shiftColorConfigRepository;

    public ShiftColorConfigService(ShiftColorConfigRepository shiftColorConfigRepository) {
        this.shiftColorConfigRepository = shiftColorConfigRepository;
    }

    public List<ShiftColorConfigDto> getAllShiftColorConfigs() {
        return shiftColorConfigRepository.findAllByOrderByStartTimeAsc()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<ShiftColorConfigDto> getShiftColorConfigById(Long id) {
        return shiftColorConfigRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<ShiftColorConfigDto> getShiftColorConfigsByTime(Double time) {
        return shiftColorConfigRepository.findByTime(time)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public DMLResponseDto createShiftColorConfig(Long userId, ShiftColorConfigRequestBody request) {
        try {
            DMLResponseDto validationResult = validateRequest(request, null);
            if ("E".equals(validationResult.getStatusMessage())) {
                return validationResult;
            }

            ShiftColorConfig shiftColorConfig = new ShiftColorConfig();
            mapRequestToEntity(request, shiftColorConfig, userId);

            ShiftColorConfig savedConfig = shiftColorConfigRepository.save(shiftColorConfig);

            return new DMLResponseDto("S",
                    "Shift color config created successfully with ID: " + savedConfig.getShiftColorConfigId());
        } catch (Exception exception) {
            return new DMLResponseDto("E", "Error creating shift color config: " + exception.getMessage());
        }
    }

    public DMLResponseDto updateShiftColorConfig(Long userId, Long id, ShiftColorConfigRequestBody request) {
        try {
            Optional<ShiftColorConfig> existingConfig = shiftColorConfigRepository.findById(id);

            if (existingConfig.isEmpty()) {
                return new DMLResponseDto("E", "Shift color config not found with ID: " + id);
            }

            DMLResponseDto validationResult = validateRequest(request, id);
            if ("E".equals(validationResult.getStatusMessage())) {
                return validationResult;
            }

            ShiftColorConfig shiftColorConfig = existingConfig.get();
            mapRequestToEntity(request, shiftColorConfig, userId);

            shiftColorConfigRepository.save(shiftColorConfig);

            return new DMLResponseDto("S", "Shift color config updated successfully");
        } catch (Exception exception) {
            return new DMLResponseDto("E", "Error updating shift color config: " + exception.getMessage());
        }
    }

    public DMLResponseDto deleteShiftColorConfig(Long id) {
        try {
            if (!shiftColorConfigRepository.existsById(id)) {
                return new DMLResponseDto("E", "Shift color config not found with ID: " + id);
            }

            shiftColorConfigRepository.deleteById(id);

            return new DMLResponseDto("S", "Shift color config deleted successfully");
        } catch (Exception exception) {
            return new DMLResponseDto("E", "Error deleting shift color config: " + exception.getMessage());
        }
    }

    private void mapRequestToEntity(ShiftColorConfigRequestBody request, ShiftColorConfig entity, Long userId) {
        entity.setStartTime(request.startTime());
        entity.setEndTime(request.endTime());
        entity.setColor(request.color() != null ? request.color().trim() : null);

        // For new entities (create operation) - stamp both audit columns
        if (entity.getShiftColorConfigId() == null) {
            entity.setCreatedBy(userId);
            entity.setLastUpdatedBy(userId);
        } else {
            // For existing entities (update operation) - createdBy and createdOn stay unchanged
            entity.setLastUpdatedBy(userId);
        }
    }

    private ShiftColorConfigDto convertToDto(ShiftColorConfig entity) {
        return new ShiftColorConfigDto(
                entity.getShiftColorConfigId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getColor(),
                entity.getCreatedBy(),
                entity.getCreatedOn(),
                entity.getLastUpdatedBy(),
                entity.getLastUpdateDate());
    }

    private DMLResponseDto validateRequest(ShiftColorConfigRequestBody request, Long excludeId) {
        if (request == null) {
            return new DMLResponseDto("E", "Request body is required");
        }

        if (request.startTime() == null || request.endTime() == null) {
            return new DMLResponseDto("E", "Start Time and End Time are required");
        }

        if (request.color() == null || request.color().isBlank()) {
            return new DMLResponseDto("E", "Color is required");
        }

        if (request.color().trim().length() > 100) {
            return new DMLResponseDto("E", "Color cannot exceed 100 characters");
        }

        if (request.startTime() > request.endTime()) {
            return new DMLResponseDto("E", "Start Time cannot be after End Time");
        }

        long overlappingCount;
        if (excludeId != null) {
            // For update operation - exclude the config being updated
            overlappingCount = shiftColorConfigRepository.countOverlapping(
                    request.startTime(), request.endTime(), excludeId);
        } else {
            // For create operation - check all existing configs
            overlappingCount = shiftColorConfigRepository.countOverlappingForNew(
                    request.startTime(), request.endTime());
        }

        if (overlappingCount > 0) {
            String operation = excludeId != null ? "update" : "create";
            return new DMLResponseDto("E", "Cannot " + operation + " shift color config. Another config already " +
                    "covers the specified time range (" + request.startTime() + " to " + request.endTime() + ").");
        }

        return new DMLResponseDto("S", "Validation passed");
    }
}
