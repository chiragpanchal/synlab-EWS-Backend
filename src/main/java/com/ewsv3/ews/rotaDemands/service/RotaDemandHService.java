package com.ewsv3.ews.rotaDemands.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.rotaDemands.dto.RotaDemandHDto;
import com.ewsv3.ews.rotaDemands.entity.RotaDemandH;
import com.ewsv3.ews.rotaDemands.repository.RotaDemandHRepository;
import com.ewsv3.ews.rotaDemands.repository.RotaDemandLRepository;
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
public class RotaDemandHService {

    @Autowired
    private RotaDemandHRepository rotaDemandHRepository;

    @Autowired
    private RotaDemandLRepository rotaDemandLRepository;

    private final JdbcClient jdbcClient;

    public RotaDemandHService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM sc_rota_demands_h WHERE profile_id = :profileId
            """;

    private static final String FIND_ALL_PAGED_SQL = """
            SELECT
                rota_demand_id,
                demand_name,
                is_active,
                work_rotation_id,
                profile_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_rota_demands_h
            WHERE profile_id = :profileId
            ORDER BY rota_demand_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT
                rota_demand_id,
                demand_name,
                is_active,
                work_rotation_id,
                profile_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_rota_demands_h
            WHERE rota_demand_id = :id
            """;

    public PagedDataDto<RotaDemandHDto> findAll(Long profileId, int page, int size) {
        long total = jdbcClient.sql(COUNT_SQL)
                .param("profileId", profileId)
                .query(Long.class)
                .single();

        List<RotaDemandHDto> content = jdbcClient.sql(FIND_ALL_PAGED_SQL)
                .param("profileId", profileId)
                .param("offset", page * size)
                .param("size", size)
                .query(RotaDemandHDto.class)
                .list();

        return new PagedDataDto<>(content, page, size, total);
    }

    public Optional<RotaDemandHDto> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(RotaDemandHDto.class)
                .optional();
    }

    public RotaDemandHDto create(RotaDemandHDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        RotaDemandH entity = toEntity(dto);
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : "Y");
        entity.setCreatedBy(currentUserId);
        entity.setCreatedOn(now);
        entity.setLastUpdatedBy(currentUserId);
        entity.setLastUpdateDate(now);

        return toDto(rotaDemandHRepository.save(entity));
    }

    public Optional<RotaDemandHDto> update(Long id, RotaDemandHDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        return rotaDemandHRepository.findById(id).map(existing -> {
            existing.setDemandName(dto.getDemandName());
            existing.setIsActive(dto.getIsActive());
            existing.setWorkRotationId(dto.getWorkRotationId());
            existing.setProfileId(dto.getProfileId());
            existing.setLastUpdatedBy(currentUserId);
            existing.setLastUpdateDate(now);
            return toDto(rotaDemandHRepository.save(existing));
        });
    }

    public void deleteById(Long id) {
        rotaDemandLRepository.deleteAllByRotaDemandId(id);
        rotaDemandHRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return rotaDemandHRepository.existsById(id);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    private RotaDemandH toEntity(RotaDemandHDto dto) {
        RotaDemandH entity = new RotaDemandH();
        entity.setDemandName(dto.getDemandName());
        entity.setIsActive(dto.getIsActive());
        entity.setWorkRotationId(dto.getWorkRotationId());
        entity.setProfileId(dto.getProfileId());
        return entity;
    }

    private RotaDemandHDto toDto(RotaDemandH entity) {
        RotaDemandHDto dto = new RotaDemandHDto();
        dto.setRotaDemandId(entity.getRotaDemandId());
        dto.setDemandName(entity.getDemandName());
        dto.setIsActive(entity.getIsActive());
        dto.setWorkRotationId(entity.getWorkRotationId());
        dto.setProfileId(entity.getProfileId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setLastUpdatedBy(entity.getLastUpdatedBy());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        return dto;
    }
}
