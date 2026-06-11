package com.ewsv3.ews.rotaDemands.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.rotaDemands.dto.JobTitleLookupDto;
import com.ewsv3.ews.rotaDemands.dto.RotaDemandLDto;
import com.ewsv3.ews.rotaDemands.entity.RotaDemandL;
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
public class RotaDemandLService {

    @Autowired
    private RotaDemandLRepository rotaDemandLRepository;

    private final JdbcClient jdbcClient;

    public RotaDemandLService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String COUNT_BY_DEMAND_SQL = """
            SELECT COUNT(*) FROM sc_rota_demands_l WHERE rota_demand_id = :rotaDemandId
            """;

    private static final String FIND_ALL_BY_DEMAND_PAGED_SQL = """
            SELECT
                rota_damand_line_id rota_demand_line_id,
                rota_demand_id,
                line_name,
                work_rotation_line_id,
                seq,
                department_id,
                job_title_id,
                fte_req,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_rota_demands_l
            WHERE rota_demand_id = :rotaDemandId
            ORDER BY seq
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_JOB_TITLES_BY_PROFILE_SQL = """
            SELECT DISTINCT job_title_id, job_title
            FROM sc_timekeeper_person_v
            WHERE profile_id = :profileId
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT
                rota_damand_line_id,
                rota_demand_id,
                line_name,
                work_rotation_line_id,
                seq,
                department_id,
                job_title_id,
                fte_req,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            FROM sc_rota_demands_l
            WHERE rota_damand_line_id = :id
            """;

    public PagedDataDto<RotaDemandLDto> findAllByRotaDemandId(Long rotaDemandId, int page, int size) {
        long total = jdbcClient.sql(COUNT_BY_DEMAND_SQL)
                .param("rotaDemandId", rotaDemandId)
                .query(Long.class)
                .single();

        List<RotaDemandLDto> content = jdbcClient.sql(FIND_ALL_BY_DEMAND_PAGED_SQL)
                .param("rotaDemandId", rotaDemandId)
                .param("offset", page * size)
                .param("size", size)
                .query(RotaDemandLDto.class)
                .list();

        return new PagedDataDto<>(content, page, size, total);
    }

    public Optional<RotaDemandLDto> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(RotaDemandLDto.class)
                .optional();
    }

    public List<JobTitleLookupDto> findJobTitlesByProfileId(Long profileId) {
        return jdbcClient.sql(FIND_JOB_TITLES_BY_PROFILE_SQL)
                .param("profileId", profileId)
                .query(JobTitleLookupDto.class)
                .list();
    }

    public RotaDemandLDto create(RotaDemandLDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        RotaDemandL entity = toEntity(dto);
        entity.setCreatedBy(currentUserId);
        entity.setCreatedOn(now);
        entity.setLastUpdatedBy(currentUserId);
        entity.setLastUpdateDate(now);

        return toDto(rotaDemandLRepository.save(entity));
    }

    public Optional<RotaDemandLDto> update(Long id, RotaDemandLDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        return rotaDemandLRepository.findById(id).map(existing -> {
            existing.setLineName(dto.getLineName());
            existing.setWorkRotationLineId(dto.getWorkRotationLineId());
            existing.setSeq(dto.getSeq());
            existing.setDepartmentId(dto.getDepartmentId());
            existing.setJobTitleId(dto.getJobTitleId());
            existing.setFteReq(dto.getFteReq());
            existing.setLastUpdatedBy(currentUserId);
            existing.setLastUpdateDate(now);
            return toDto(rotaDemandLRepository.save(existing));
        });
    }

    public void deleteById(Long id) {
        rotaDemandLRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return rotaDemandLRepository.existsById(id);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    private RotaDemandL toEntity(RotaDemandLDto dto) {
        RotaDemandL entity = new RotaDemandL();
        entity.setRotaDemandId(dto.getRotaDemandId());
        entity.setLineName(dto.getLineName());
        entity.setWorkRotationLineId(dto.getWorkRotationLineId());
        entity.setSeq(dto.getSeq());
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setJobTitleId(dto.getJobTitleId());
        entity.setFteReq(dto.getFteReq());
        return entity;
    }

    private RotaDemandLDto toDto(RotaDemandL entity) {
        RotaDemandLDto dto = new RotaDemandLDto();
        dto.setRotaDemandLineId(entity.getRotaDemandLineId());
        dto.setRotaDemandId(entity.getRotaDemandId());
        dto.setLineName(entity.getLineName());
        dto.setWorkRotationLineId(entity.getWorkRotationLineId());
        dto.setSeq(entity.getSeq());
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setJobTitleId(entity.getJobTitleId());
        dto.setFteReq(entity.getFteReq());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setLastUpdatedBy(entity.getLastUpdatedBy());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        return dto;
    }
}
