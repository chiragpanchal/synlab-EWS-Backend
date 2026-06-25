package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.AcuityRuleDto;
import com.ewsv3.ews.rules.dto.req.AcuityRuleRequest;
import com.ewsv3.ews.rules.entity.AcuityRule;
import com.ewsv3.ews.rules.repository.AcuityRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcuityRuleService {
    private static final Logger logger = LoggerFactory.getLogger(AcuityRuleService.class);

    private final AcuityRuleRepository acuityRuleRepository;

    public AcuityRuleService(AcuityRuleRepository acuityRuleRepository) {
        this.acuityRuleRepository = acuityRuleRepository;
    }

    @Transactional
    public AcuityRuleDto create(AcuityRuleRequest request) {
        logger.info("Creating AcuityRule for profileId: {}", request.getProfileId());
        AcuityRule rule = new AcuityRule();
        rule.setProfileId(request.getProfileId());
        rule.setAcuityLevelId(request.getAcuityLevelId());
        rule.setRatioId(request.getRatioId());
        AcuityRule saved = acuityRuleRepository.save(rule);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public AcuityRuleDto getById(Long acuityRuleId) {
        logger.info("Fetching AcuityRule with acuityRuleId: {}", acuityRuleId);
        return acuityRuleRepository.findById(acuityRuleId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AcuityRuleDto> getAll(Long profileId) {
        logger.info("Fetching all AcuityRules for profileId: {}", profileId);
        return acuityRuleRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcuityRuleDto> getByProfileId(Long profileId) {
        logger.info("Fetching AcuityRules for profileId: {}", profileId);
        return acuityRuleRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcuityRuleDto> getByAcuityLevelId(Long acuityLevelId) {
        logger.info("Fetching AcuityRules for acuityLevelId: {}", acuityLevelId);
        return acuityRuleRepository.findByAcuityLevelId(acuityLevelId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcuityRuleDto> getByRatioId(Long ratioId) {
        logger.info("Fetching AcuityRules for ratioId: {}", ratioId);
        return acuityRuleRepository.findByRatioId(ratioId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AcuityRuleDto update(Long acuityRuleId, AcuityRuleRequest request) {
        logger.info("Updating AcuityRule with acuityRuleId: {}", acuityRuleId);
        Optional<AcuityRule> existing = acuityRuleRepository.findById(acuityRuleId);
        if (existing.isPresent()) {
            AcuityRule rule = existing.get();
            rule.setProfileId(request.getProfileId());
            rule.setAcuityLevelId(request.getAcuityLevelId());
            rule.setRatioId(request.getRatioId());
            AcuityRule updated = acuityRuleRepository.save(rule);
            return mapToDto(updated);
        }
        logger.warn("AcuityRule not found with acuityRuleId: {}", acuityRuleId);
        return null;
    }

    @Transactional
    public boolean delete(Long acuityRuleId) {
        logger.info("Deleting AcuityRule with acuityRuleId: {}", acuityRuleId);
        if (acuityRuleRepository.existsById(acuityRuleId)) {
            acuityRuleRepository.deleteById(acuityRuleId);
            return true;
        }
        logger.warn("AcuityRule not found with acuityRuleId: {}", acuityRuleId);
        return false;
    }

    private AcuityRuleDto mapToDto(AcuityRule rule) {
        return new AcuityRuleDto(
                rule.getAcuityRuleId(),
                rule.getProfileId(),
                rule.getAcuityLevelId(),
                rule.getRatioId(),
                rule.getAcuityLevel() != null ? rule.getAcuityLevel().getLevelName() : null,
                rule.getAcuityRatio() != null ? rule.getAcuityRatio().getRatioName() : null,
                rule.getAcuityRatio() != null ? rule.getAcuityRatio().getBeds() : null,
                rule.getAcuityRatio() != null ? rule.getAcuityRatio().getReqFte() : null
        );
    }
}
