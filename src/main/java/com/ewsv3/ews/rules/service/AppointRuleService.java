package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.AppointRuleDto;
import com.ewsv3.ews.rules.dto.req.AppointRuleRequest;
import com.ewsv3.ews.rules.entity.AppointRule;
import com.ewsv3.ews.rules.repository.AppointRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointRuleService {
    private static final Logger logger = LoggerFactory.getLogger(AppointRuleService.class);

    private final AppointRuleRepository appointRuleRepository;

    public AppointRuleService(AppointRuleRepository appointRuleRepository) {
        this.appointRuleRepository = appointRuleRepository;
    }

    @Transactional
    public AppointRuleDto create(AppointRuleRequest request) {
        logger.info("Creating AppointRule for profileId: {}", request.getProfileId());
        AppointRule rule = new AppointRule();
        rule.setProfileId(request.getProfileId());
        rule.setReqFte(request.getReqFte());
        rule.setJobTitleId(request.getJobTitleId());
        rule.setWorkDurationId(request.getWorkDurationId());
        rule.setNosAppointments(request.getNosAppointments());
        AppointRule saved = appointRuleRepository.save(rule);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public AppointRuleDto getById(Long appointRuleId) {
        logger.info("Fetching AppointRule with appointRuleId: {}", appointRuleId);
        return appointRuleRepository.findById(appointRuleId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AppointRuleDto> getAll(Long profileId) {
        logger.info("Fetching all AppointRules for profileId: {}", profileId);
        return appointRuleRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointRuleDto> getByJobTitleId(Long profileId, Long jobTitleId) {
        logger.info("Fetching AppointRules for profileId: {}, jobTitleId: {}", profileId, jobTitleId);
        return appointRuleRepository.findByProfileIdAndJobTitleId(profileId, jobTitleId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointRuleDto update(Long appointRuleId, AppointRuleRequest request) {
        logger.info("Updating AppointRule with appointRuleId: {}", appointRuleId);
        Optional<AppointRule> existing = appointRuleRepository.findById(appointRuleId);
        if (existing.isPresent()) {
            AppointRule rule = existing.get();
            rule.setProfileId(request.getProfileId());
            rule.setReqFte(request.getReqFte());
            rule.setJobTitleId(request.getJobTitleId());
            rule.setWorkDurationId(request.getWorkDurationId());
            rule.setNosAppointments(request.getNosAppointments());
            AppointRule updated = appointRuleRepository.save(rule);
            return mapToDto(updated);
        }
        logger.warn("AppointRule not found with appointRuleId: {}", appointRuleId);
        return null;
    }

    @Transactional
    public boolean delete(Long appointRuleId) {
        logger.info("Deleting AppointRule with appointRuleId: {}", appointRuleId);
        if (appointRuleRepository.existsById(appointRuleId)) {
            appointRuleRepository.deleteById(appointRuleId);
            return true;
        }
        logger.warn("AppointRule not found with appointRuleId: {}", appointRuleId);
        return false;
    }

    private AppointRuleDto mapToDto(AppointRule rule) {
        return new AppointRuleDto(
                rule.getAppointRuleId(),
                rule.getProfileId(),
                rule.getReqFte(),
                rule.getJobTitleId(),
                rule.getWorkDurationId(),
                rule.getNosAppointments()
        );
    }
}
