package com.ewsv3.ews.rules.service;

import com.ewsv3.ews.rules.dto.AcuityRatioDto;
import com.ewsv3.ews.rules.dto.req.AcuityRatioRequest;
import com.ewsv3.ews.rules.entity.AcuityRatio;
import com.ewsv3.ews.rules.repository.AcuityRatioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcuityRatioService {
    private static final Logger logger = LoggerFactory.getLogger(AcuityRatioService.class);

    private final AcuityRatioRepository acuityRatioRepository;

    public AcuityRatioService(AcuityRatioRepository acuityRatioRepository) {
        this.acuityRatioRepository = acuityRatioRepository;
    }

    @Transactional
    public AcuityRatioDto create(AcuityRatioRequest request) {
        logger.info("Creating AcuityRatio");
        AcuityRatio ratio = new AcuityRatio();
        ratio.setRatioName(request.getRatioName());
        ratio.setBeds(request.getBeds());
        ratio.setReqFte(request.getReqFte());
        AcuityRatio saved = acuityRatioRepository.save(ratio);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public AcuityRatioDto getById(Long ratioId) {
        logger.info("Fetching AcuityRatio with ratioId: {}", ratioId);
        return acuityRatioRepository.findById(ratioId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AcuityRatioDto> getAll(Long profileId) {
        logger.info("Fetching all AcuityRatios for profileId: {}", profileId);
        return acuityRatioRepository.findAllByOrderByRatioNameAsc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AcuityRatioDto update(Long ratioId, AcuityRatioRequest request) {
        logger.info("Updating AcuityRatio with ratioId: {}", ratioId);
        Optional<AcuityRatio> existing = acuityRatioRepository.findById(ratioId);
        if (existing.isPresent()) {
            AcuityRatio ratio = existing.get();
            ratio.setRatioName(request.getRatioName());
            ratio.setBeds(request.getBeds());
            ratio.setReqFte(request.getReqFte());
            AcuityRatio updated = acuityRatioRepository.save(ratio);
            return mapToDto(updated);
        }
        logger.warn("AcuityRatio not found with ratioId: {}", ratioId);
        return null;
    }

    @Transactional
    public boolean delete(Long ratioId) {
        logger.info("Deleting AcuityRatio with ratioId: {}", ratioId);
        if (acuityRatioRepository.existsById(ratioId)) {
            acuityRatioRepository.deleteById(ratioId);
            return true;
        }
        logger.warn("AcuityRatio not found with ratioId: {}", ratioId);
        return false;
    }

    private AcuityRatioDto mapToDto(AcuityRatio ratio) {
        return new AcuityRatioDto(
                ratio.getRatioId(),
                ratio.getRatioName(),
                ratio.getBeds(),
                ratio.getReqFte()
        );
    }
}
