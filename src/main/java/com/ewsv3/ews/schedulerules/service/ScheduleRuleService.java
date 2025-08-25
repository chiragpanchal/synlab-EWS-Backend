package com.ewsv3.ews.schedulerules.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.schedulerules.dto.ScheduleRuleDto;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleRequest;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleSearchRequest;
import com.ewsv3.ews.schedulerules.entity.ScheduleRule;
import com.ewsv3.ews.schedulerules.repository.ScheduleRuleRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ewsv3.ews.masters.service.ServiceUtils.sqlProfiles;

@Service
@Transactional
public class ScheduleRuleService {

    private final ScheduleRuleRepository scheduleRuleRepository;

    public ScheduleRuleService(ScheduleRuleRepository scheduleRuleRepository) {
        this.scheduleRuleRepository = scheduleRuleRepository;
    }

    public List<ScheduleRuleDto> getAllScheduleRules() {
        List<ScheduleRule> scheduleRules = scheduleRuleRepository.findAll();
        return scheduleRules.stream()
                .map(this::convertToDto)
                .toList();
    }
    
    public List<ScheduleRuleDto> getScheduleRulesForUser(Long userId, JdbcClient jdbcClient) {
        try {
            // Get user's accessible profile IDs from timekeeper-profiles logic
            List<Long> accessibleProfileIds = getUserAccessibleProfileIds(userId, jdbcClient);
            
            if (accessibleProfileIds.isEmpty()) {
                System.out.println("getScheduleRulesForUser: No accessible profiles found for userId: " + userId);
                return List.of();
            }
            
            System.out.println("getScheduleRulesForUser: Found " + accessibleProfileIds.size() + " accessible profiles for userId: " + userId);
            
            // Get schedule rules only for accessible profiles
            List<ScheduleRule> scheduleRules = scheduleRuleRepository.findByProfileIds(accessibleProfileIds);
            
            return scheduleRules.stream()
                    .map(this::convertToDto)
                    .toList();
        } catch (Exception exception) {
            System.out.println("getScheduleRulesForUser exception: " + exception.getMessage());
            throw new RuntimeException("Error fetching schedule rules for user: " + exception.getMessage());
        }
    }

    public Optional<ScheduleRuleDto> getScheduleRuleById(Long id) {
        return scheduleRuleRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<ScheduleRuleDto> getScheduleRulesByProfileId(Long profileId) {
        List<ScheduleRule> scheduleRules = scheduleRuleRepository.findByProfileIdOrderByValidFromDesc(profileId);
        return scheduleRules.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ScheduleRuleDto> searchScheduleRules(ScheduleRuleSearchRequest request) {
        List<ScheduleRule> scheduleRules;
        
        if (request.profileId() != null && request.validDate() != null) {
            scheduleRules = scheduleRuleRepository.findByProfileIdAndValidDate(request.profileId(), request.validDate());
        } else if (request.profileId() != null) {
            scheduleRules = scheduleRuleRepository.findByProfileId(request.profileId());
        } else if (request.validDate() != null) {
            scheduleRules = scheduleRuleRepository.findActiveScheduleRules(request.validDate());
        } else {
            scheduleRules = scheduleRuleRepository.findAll();
        }
        
        return scheduleRules.stream()
                .map(this::convertToDto)
                .toList();
    }

    public DMLResponseDto createScheduleRule(Long userId, ScheduleRuleRequest request) {
        try {
            System.out.println("createScheduleRule userId: " + userId);
            
            // Validate date overlap for new schedule rule
            DMLResponseDto validationResult = validateDateOverlap(request.profileId(), request.validFrom(), request.validTo(), null);
            if ("E".equals(validationResult.getStatusMessage())) {
                return validationResult;
            }
            
            ScheduleRule scheduleRule = new ScheduleRule();
            
            // Set business fields
            scheduleRule.setProfileId(request.profileId());
            scheduleRule.setValidFrom(request.validFrom());
            scheduleRule.setValidTo(request.validTo());
            scheduleRule.setMaxHrsPerDay(request.maxHrsPerDay());
            scheduleRule.setMinHrsPerWeek(request.minHrsPerWeek());
            scheduleRule.setMaxHrsPerWeek(request.maxHrsPerWeek());
            scheduleRule.setMinHrsPerMonth(request.minHrsPerMonth());
            scheduleRule.setMaxHrsPerMonth(request.maxHrsPerMonth());
            scheduleRule.setShiftGap(request.shiftGap());
            scheduleRule.setMinRestDaysPerWeek(request.minRestDaysPerWeek());
            scheduleRule.setMaxContShiftDays(request.maxContShiftDays());
            scheduleRule.setMaxContRestDays(request.maxContRestDays());
            
            // Set audit fields explicitly
            scheduleRule.setCreatedBy(userId);
            System.out.println("DEBUG: After setting createdBy, value is: " + scheduleRule.getCreatedBy());
            
            scheduleRule.setLastUpdatedBy(userId);
            System.out.println("DEBUG: After setting lastUpdatedBy, value is: " + scheduleRule.getLastUpdatedBy());
            System.out.println("DEBUG: userId value is: " + userId + " (type: " + (userId != null ? userId.getClass().getSimpleName() : "null") + ")");
            
            System.out.println("createScheduleRule before save - createdBy: " + scheduleRule.getCreatedBy() + ", lastUpdatedBy: " + scheduleRule.getLastUpdatedBy());
            
            ScheduleRule savedRule = scheduleRuleRepository.save(scheduleRule);
            
            System.out.println("createScheduleRule after save - ID: " + savedRule.getScheduleRuleId() + ", createdBy: " + savedRule.getCreatedBy() + ", lastUpdatedBy: " + savedRule.getLastUpdatedBy());
            return new DMLResponseDto("S", "Schedule rule created successfully with ID: " + savedRule.getScheduleRuleId());
        } catch (Exception exception) {
            System.out.println("createScheduleRule exception: " + exception.getMessage());
            exception.printStackTrace();
            return new DMLResponseDto("E", "Error creating schedule rule: " + exception.getMessage());
        }
    }

    public DMLResponseDto updateScheduleRule(Long userId, Long id, ScheduleRuleRequest request) {
        try {
            Optional<ScheduleRule> existingRule = scheduleRuleRepository.findById(id);
            
            if (existingRule.isEmpty()) {
                return new DMLResponseDto("E", "Schedule rule not found with ID: " + id);
            }
            
            // Validate date overlap for existing schedule rule (exclude current rule from check)
            DMLResponseDto validationResult = validateDateOverlap(request.profileId(), request.validFrom(), request.validTo(), id);
            if ("E".equals(validationResult.getStatusMessage())) {
                return validationResult;
            }
            
            ScheduleRule scheduleRule = existingRule.get();
            mapRequestToEntity(request, scheduleRule, userId);
            // lastUpdatedBy is already set in mapRequestToEntity for update operations
            
            ScheduleRule updatedRule = scheduleRuleRepository.save(scheduleRule);
            
            System.out.println("updateScheduleRule updated schedule rule ID: " + updatedRule.getScheduleRuleId());
            return new DMLResponseDto("S", "Schedule rule updated successfully");
        } catch (Exception exception) {
            System.out.println("updateScheduleRule exception: " + exception.getMessage());
            return new DMLResponseDto("E", "Error updating schedule rule: " + exception.getMessage());
        }
    }

    public DMLResponseDto deleteScheduleRule(Long id) {
        try {
            if (!scheduleRuleRepository.existsById(id)) {
                return new DMLResponseDto("E", "Schedule rule not found with ID: " + id);
            }
            
            scheduleRuleRepository.deleteById(id);
            
            System.out.println("deleteScheduleRule deleted schedule rule ID: " + id);
            return new DMLResponseDto("S", "Schedule rule deleted successfully");
        } catch (Exception exception) {
            System.out.println("deleteScheduleRule exception: " + exception.getMessage());
            return new DMLResponseDto("E", "Error deleting schedule rule: " + exception.getMessage());
        }
    }

    public List<ScheduleRuleDto> getActiveScheduleRules(LocalDate date) {
        List<ScheduleRule> scheduleRules = scheduleRuleRepository.findActiveScheduleRules(date);
        return scheduleRules.stream()
                .map(this::convertToDto)
                .toList();
    }

    private void mapRequestToEntity(ScheduleRuleRequest request, ScheduleRule entity, Long userId) {
        entity.setProfileId(request.profileId());
        entity.setValidFrom(request.validFrom());
        entity.setValidTo(request.validTo());
        entity.setMaxHrsPerDay(request.maxHrsPerDay());
        entity.setMinHrsPerWeek(request.minHrsPerWeek());
        entity.setMaxHrsPerWeek(request.maxHrsPerWeek());
        entity.setMinHrsPerMonth(request.minHrsPerMonth());
        entity.setMaxHrsPerMonth(request.maxHrsPerMonth());
        entity.setShiftGap(request.shiftGap());
        entity.setMinRestDaysPerWeek(request.minRestDaysPerWeek());
        entity.setMaxContShiftDays(request.maxContShiftDays());
        entity.setMaxContRestDays(request.maxContRestDays());
        
        // For new entities (create operation)
        if (entity.getScheduleRuleId() == null) {
            System.out.println("mapRequestToEntity CREATE - setting createdBy and lastUpdatedBy to userId: " + userId);
            entity.setCreatedBy(userId);
            entity.setLastUpdatedBy(userId);
        } else {
            // For existing entities (update operation) - only update lastUpdatedBy
            // Do NOT modify createdBy and createdOn - they should remain unchanged
            System.out.println("mapRequestToEntity UPDATE - setting lastUpdatedBy to userId: " + userId);
            entity.setLastUpdatedBy(userId);
        }
    }

    private ScheduleRuleDto convertToDto(ScheduleRule entity) {
        return new ScheduleRuleDto(
                entity.getScheduleRuleId(),
                entity.getProfileId(),
                entity.getValidFrom(),
                entity.getValidTo(),
                entity.getMaxHrsPerDay(),
                entity.getMinHrsPerWeek(),
                entity.getMaxHrsPerWeek(),
                entity.getMinHrsPerMonth(),
                entity.getMaxHrsPerMonth(),
                entity.getShiftGap(),
                entity.getMinRestDaysPerWeek(),
                entity.getMaxContShiftDays(),
                entity.getMaxContRestDays(),
                entity.getCreatedBy(),
                entity.getCreatedOn(),
                entity.getLastUpdatedBy(),
                entity.getLastUpdateDate()
        );
    }

    private DMLResponseDto validateDateOverlap(Long profileId, LocalDate validFrom, LocalDate validTo, Long excludeRuleId) {
        System.out.println("validateDateOverlap - profileId: " + profileId + ", validFrom: " + validFrom + ", validTo: " + validTo + ", excludeRuleId: " + excludeRuleId);
        
        // Check for null dates
        if (validFrom == null || validTo == null) {
            return new DMLResponseDto("E", "Valid From and Valid To dates are required");
        }
        
        // Check if validFrom is after validTo
        if (validFrom.isAfter(validTo)) {
            return new DMLResponseDto("E", "Valid From date cannot be after Valid To date");
        }
        
        // Check for overlapping schedule rules
        long overlappingCount;
        
        if (excludeRuleId != null) {
            // For update operation - exclude the current rule being updated
            System.out.println("validateDateOverlap UPDATE - calling countOverlappingScheduleRules with excludeRuleId: " + excludeRuleId);
            overlappingCount = scheduleRuleRepository.countOverlappingScheduleRules(profileId, validFrom, validTo, excludeRuleId);
        } else {
            // For create operation - check all existing rules
            System.out.println("validateDateOverlap CREATE - calling countOverlappingScheduleRulesForNew");
            overlappingCount = scheduleRuleRepository.countOverlappingScheduleRulesForNew(profileId, validFrom, validTo);
        }
        
        System.out.println("validateDateOverlap overlappingCount: " + overlappingCount);
        
        if (overlappingCount > 0) {
            String operation = excludeRuleId != null ? "update" : "create";
            System.out.println("validateDateOverlap found " + overlappingCount + " overlapping rules for profileId: " + profileId + 
                             ", validFrom: " + validFrom + ", validTo: " + validTo + ", operation: " + operation);
            
            return new DMLResponseDto("E", "Cannot " + operation + " schedule rule. Profile ID " + profileId + 
                                    " already has an active schedule rule during the specified date range (" + 
                                    validFrom + " to " + validTo + "). Only one schedule rule can be active per profile on any given date.");
        }
        
        return new DMLResponseDto("S", "Date validation passed");
    }
    
    private List<Long> getUserAccessibleProfileIds(Long userId, JdbcClient jdbcClient) {
        Map<String, Object> profileParamMap = new HashMap<>();
        profileParamMap.put("userId", userId);
        
        // Using the same SQL query as timekeeper-profiles endpoint to get accessible profile IDs
        List<Map<String, Object>> profileResults = jdbcClient.sql(sqlProfiles)
                .params(profileParamMap)
                .query()
                .listOfRows();
        
        return profileResults.stream()
                .map(row -> {
                    Object profileIdObj = row.get("PROFILE_ID");
                    if (profileIdObj instanceof Number) {
                        return ((Number) profileIdObj).longValue();
                    }
                    return Long.valueOf(profileIdObj.toString());
                })
                .toList();
    }
}