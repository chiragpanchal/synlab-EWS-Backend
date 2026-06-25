package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.AcuityJobMappingDto;
import com.ewsv3.ews.rules.dto.req.AcuityJobMappingRequest;
import com.ewsv3.ews.rules.entity.AcuityJobMapping;
import com.ewsv3.ews.rules.repository.AcuityJobMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcuityJobMappingService {
    private static final Logger logger = LoggerFactory.getLogger(AcuityJobMappingService.class);

    private final AcuityJobMappingRepository acuityJobMappingRepository;

    public AcuityJobMappingService(AcuityJobMappingRepository acuityJobMappingRepository) {
        this.acuityJobMappingRepository = acuityJobMappingRepository;
    }

    @Transactional
    public AcuityJobMappingDto create(AcuityJobMappingRequest request) {
        logger.info("Creating AcuityJobMapping for profileId: {}", request.getProfileId());
        AcuityJobMapping mapping = new AcuityJobMapping(
                request.getProfileId(),
                request.getForJobTitleId(),
                request.getForFte(),
                request.getReqJobTitleId(),
                request.getReqFte()
        );
        AcuityJobMapping saved = acuityJobMappingRepository.save(mapping);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public AcuityJobMappingDto getById(Long acuityJobMappingId) {
        logger.info("Fetching AcuityJobMapping with acuityJobMappingId: {}", acuityJobMappingId);
        return acuityJobMappingRepository.findById(acuityJobMappingId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AcuityJobMappingDto> getAll(Long profileId) {
        logger.info("Fetching all AcuityJobMappings for profileId: {}", profileId);
        return acuityJobMappingRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcuityJobMappingDto> getByForJobTitleId(Long profileId, Long forJobTitleId) {
        logger.info("Fetching AcuityJobMappings for profileId: {}, forJobTitleId: {}", profileId, forJobTitleId);
        return acuityJobMappingRepository.findByProfileIdAndForJobTitleId(profileId, forJobTitleId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcuityJobMappingDto> getByReqJobTitleId(Long profileId, Long reqJobTitleId) {
        logger.info("Fetching AcuityJobMappings for profileId: {}, reqJobTitleId: {}", profileId, reqJobTitleId);
        return acuityJobMappingRepository.findByProfileIdAndReqJobTitleId(profileId, reqJobTitleId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AcuityJobMappingDto update(Long acuityJobMappingId, AcuityJobMappingRequest request) {
        logger.info("Updating AcuityJobMapping with acuityJobMappingId: {}", acuityJobMappingId);
        Optional<AcuityJobMapping> existing = acuityJobMappingRepository.findById(acuityJobMappingId);
        if (existing.isPresent()) {
            AcuityJobMapping mapping = existing.get();
            mapping.setProfileId(request.getProfileId());
            mapping.setForJobTitleId(request.getForJobTitleId());
            mapping.setForFte(request.getForFte());
            mapping.setReqJobTitleId(request.getReqJobTitleId());
            mapping.setReqFte(request.getReqFte());
            AcuityJobMapping updated = acuityJobMappingRepository.save(mapping);
            return mapToDto(updated);
        }
        logger.warn("AcuityJobMapping not found with acuityJobMappingId: {}", acuityJobMappingId);
        return null;
    }

    @Transactional
    public boolean delete(Long acuityJobMappingId) {
        logger.info("Deleting AcuityJobMapping with acuityJobMappingId: {}", acuityJobMappingId);
        if (acuityJobMappingRepository.existsById(acuityJobMappingId)) {
            acuityJobMappingRepository.deleteById(acuityJobMappingId);
            return true;
        }
        logger.warn("AcuityJobMapping not found with acuityJobMappingId: {}", acuityJobMappingId);
        return false;
    }

    private AcuityJobMappingDto mapToDto(AcuityJobMapping mapping) {
        return new AcuityJobMappingDto(
                mapping.getAcuityJobMappingId(),
                mapping.getProfileId(),
                mapping.getForJobTitleId(),
                mapping.getForFte(),
                mapping.getReqJobTitleId(),
                mapping.getReqFte()
        );
    }
}
