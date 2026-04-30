package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.setup.entity.LabEquipment;
import com.ewsv3.ews.setup.entity.LabEquipmentDto;
import com.ewsv3.ews.setup.repos.LabEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LabEquipmentService {

    @Autowired
    private LabEquipmentRepository labEquipmentRepository;

    private final JdbcClient jdbcClient;

    public LabEquipmentService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM sc_lab_equipments_h
            """;

    private static final String FIND_ALL_PAGED_SQL = """
            SELECT
                lab_equipment_id,
                eq_code,
                eq_name,
                is_active,
                cleanup_time,
                prep_time,
                utilize_time,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_lab_equipments_h
            ORDER BY lab_equipment_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT
                lab_equipment_id,
                eq_code,
                eq_name,
                is_active,
                cleanup_time,
                prep_time,
                utilize_time,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_lab_equipments_h
            WHERE lab_equipment_id = :id
            """;

    public PagedDataDto<LabEquipmentDto> findAll(int page, int size) {
        long total = jdbcClient.sql(COUNT_SQL)
                .query(Long.class)
                .single();

        List<LabEquipmentDto> content = jdbcClient.sql(FIND_ALL_PAGED_SQL)
                .param("offset", page * size)
                .param("size", size)
                .query(LabEquipmentDto.class)
                .list();

        return new PagedDataDto<>(content, page, size, total);
    }

    public Optional<LabEquipmentDto> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(LabEquipmentDto.class)
                .optional();
    }

    public LabEquipmentDto create(LabEquipmentDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        LabEquipment entity = toEntity(dto);
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : "Y");
        entity.setCreatedBy(currentUserId);
        entity.setCreatedOn(now);
        entity.setLastUpdatedBy(currentUserId);
        entity.setLastUpdateDate(now);

        return toDto(labEquipmentRepository.save(entity));
    }

    public Optional<LabEquipmentDto> update(Long id, LabEquipmentDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        return labEquipmentRepository.findById(id).map(existing -> {
            existing.setEqCode(dto.getEqCode());
            existing.setEqName(dto.getEqName());
            existing.setIsActive(dto.getIsActive());
            existing.setCleanupTime(dto.getCleanupTime());
            existing.setPrepTime(dto.getPrepTime());
            existing.setUtilizeTime(dto.getUtilizeTime());
            existing.setLastUpdatedBy(currentUserId);
            existing.setLastUpdateDate(now);
            return toDto(labEquipmentRepository.save(existing));
        });
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    public void deleteById(Long id) {
        labEquipmentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return labEquipmentRepository.existsById(id);
    }

    private LabEquipment toEntity(LabEquipmentDto dto) {
        LabEquipment entity = new LabEquipment();
        // ID intentionally omitted — must be null so JPA generates it on INSERT
        entity.setEqCode(dto.getEqCode());
        entity.setEqName(dto.getEqName());
        entity.setIsActive(dto.getIsActive());
        entity.setCleanupTime(dto.getCleanupTime());
        entity.setPrepTime(dto.getPrepTime());
        entity.setUtilizeTime(dto.getUtilizeTime());
        return entity;
    }

    private LabEquipmentDto toDto(LabEquipment entity) {
        LabEquipmentDto dto = new LabEquipmentDto();
        dto.setLabEquipmentId(entity.getLabEquipmentId());
        dto.setEqCode(entity.getEqCode());
        dto.setEqName(entity.getEqName());
        dto.setIsActive(entity.getIsActive());
        dto.setCleanupTime(entity.getCleanupTime());
        dto.setPrepTime(entity.getPrepTime());
        dto.setUtilizeTime(entity.getUtilizeTime());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setLastUpdatedBy(entity.getLastUpdatedBy());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        return dto;
    }
}
