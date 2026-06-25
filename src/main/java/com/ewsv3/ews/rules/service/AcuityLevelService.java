package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.AcuityLevelDto;
import com.ewsv3.ews.rules.dto.req.AcuityLevelRequest;
import com.ewsv3.ews.rules.entity.AcuityLevel;
import com.ewsv3.ews.rules.repository.AcuityLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcuityLevelService {
    private static final Logger logger = LoggerFactory.getLogger(AcuityLevelService.class);

    private final AcuityLevelRepository acuityLevelRepository;

    public AcuityLevelService(AcuityLevelRepository acuityLevelRepository) {
        this.acuityLevelRepository = acuityLevelRepository;
    }

    @Transactional
    public AcuityLevelDto create(AcuityLevelRequest request) {
        logger.info("Creating AcuityLevel");
        AcuityLevel level = new AcuityLevel();
        level.setLevelName(request.getLevelName());
        AcuityLevel saved = acuityLevelRepository.save(level);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public AcuityLevelDto getById(Long acuityLevelId) {
        logger.info("Fetching AcuityLevel with acuityLevelId: {}", acuityLevelId);
        return acuityLevelRepository.findById(acuityLevelId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AcuityLevelDto> getAll(Long profileId) {
        logger.info("Fetching all AcuityLevels for profileId: {}", profileId);
        return acuityLevelRepository.findAllByOrderByLevelNameAsc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AcuityLevelDto update(Long acuityLevelId, AcuityLevelRequest request) {
        logger.info("Updating AcuityLevel with acuityLevelId: {}", acuityLevelId);
        Optional<AcuityLevel> existing = acuityLevelRepository.findById(acuityLevelId);
        if (existing.isPresent()) {
            AcuityLevel level = existing.get();
            level.setLevelName(request.getLevelName());
            AcuityLevel updated = acuityLevelRepository.save(level);
            return mapToDto(updated);
        }
        logger.warn("AcuityLevel not found with acuityLevelId: {}", acuityLevelId);
        return null;
    }

    @Transactional
    public boolean delete(Long acuityLevelId) {
        logger.info("Deleting AcuityLevel with acuityLevelId: {}", acuityLevelId);
        if (acuityLevelRepository.existsById(acuityLevelId)) {
            acuityLevelRepository.deleteById(acuityLevelId);
            return true;
        }
        logger.warn("AcuityLevel not found with acuityLevelId: {}", acuityLevelId);
        return false;
    }

    private AcuityLevelDto mapToDto(AcuityLevel level) {
        return new AcuityLevelDto(
                level.getAcuityLevelId(),
                level.getLevelName()
        );
    }
}
