package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.OccupyRuleDto;
import com.ewsv3.ews.rules.dto.req.OccupyRuleRequest;
import com.ewsv3.ews.rules.entity.OccupyRule;
import com.ewsv3.ews.rules.repository.OccupyRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OccupyRuleService {
    private static final Logger logger = LoggerFactory.getLogger(OccupyRuleService.class);

    private final OccupyRuleRepository occupyRuleRepository;

    public OccupyRuleService(OccupyRuleRepository occupyRuleRepository) {
        this.occupyRuleRepository = occupyRuleRepository;
    }

    @Transactional
    public OccupyRuleDto create(OccupyRuleRequest request) {
        logger.info("Creating OccupyRule for profileId: {}", request.getProfileId());
        OccupyRule rule = new OccupyRule();
        rule.setProfileId(request.getProfileId());
        rule.setOccBeds(request.getOccBeds());
        rule.setWorkDurationId(request.getWorkDurationId());
        rule.setReqFte(request.getReqFte());
        rule.setJobTitleId(request.getJobTitleId());
        OccupyRule saved = occupyRuleRepository.save(rule);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public OccupyRuleDto getById(Long occupyRuleId) {
        logger.info("Fetching OccupyRule with occupyRuleId: {}", occupyRuleId);
        return occupyRuleRepository.findById(occupyRuleId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<OccupyRuleDto> getAll(Long profileId) {
        logger.info("Fetching all OccupyRules for profileId: {}", profileId);
        return occupyRuleRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OccupyRuleDto> getByJobTitleId(Long profileId, Long jobTitleId) {
        logger.info("Fetching OccupyRules for profileId: {}, jobTitleId: {}", profileId, jobTitleId);
        return occupyRuleRepository.findByProfileIdAndJobTitleId(profileId, jobTitleId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OccupyRuleDto> getByWorkDurationId(Long workDurationId) {
        logger.info("Fetching OccupyRules for workDurationId: {}", workDurationId);
        return occupyRuleRepository.findByWorkDurationId(workDurationId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OccupyRuleDto update(Long occupyRuleId, OccupyRuleRequest request) {
        logger.info("Updating OccupyRule with occupyRuleId: {}", occupyRuleId);
        Optional<OccupyRule> existing = occupyRuleRepository.findById(occupyRuleId);
        if (existing.isPresent()) {
            OccupyRule rule = existing.get();
            rule.setProfileId(request.getProfileId());
            rule.setOccBeds(request.getOccBeds());
            rule.setWorkDurationId(request.getWorkDurationId());
            rule.setReqFte(request.getReqFte());
            rule.setJobTitleId(request.getJobTitleId());
            OccupyRule updated = occupyRuleRepository.save(rule);
            return mapToDto(updated);
        }
        logger.warn("OccupyRule not found with occupyRuleId: {}", occupyRuleId);
        return null;
    }

    @Transactional
    public boolean delete(Long occupyRuleId) {
        logger.info("Deleting OccupyRule with occupyRuleId: {}", occupyRuleId);
        if (occupyRuleRepository.existsById(occupyRuleId)) {
            occupyRuleRepository.deleteById(occupyRuleId);
            return true;
        }
        logger.warn("OccupyRule not found with occupyRuleId: {}", occupyRuleId);
        return false;
    }

    private OccupyRuleDto mapToDto(OccupyRule rule) {
        return new OccupyRuleDto(
                rule.getOccupyRuleId(),
                rule.getProfileId(),
                rule.getOccBeds(),
                rule.getWorkDurationId(),
                rule.getReqFte(),
                rule.getJobTitleId()
        );
    }
}
