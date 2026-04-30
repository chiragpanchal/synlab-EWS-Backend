package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.setup.entity.LabEquipmentJob;
import com.ewsv3.ews.setup.entity.LabEquipmentJobDto;
import com.ewsv3.ews.setup.repos.LabEquipmentJobRepository;
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
public class LabEquipmentJobService {

    @Autowired
    private LabEquipmentJobRepository labEquipmentJobRepository;

    private final JdbcClient jdbcClient;

    public LabEquipmentJobService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String SELECT_COLUMNS = """
            SELECT
                j.lab_equipments_job_id,
                j.lab_equipment_id,
                j.job_title_id,
                e.eq_name,
                s.job_title,
                j.created_by,
                j.created_on,
                j.last_updated_by,
                j.last_update_date
            FROM sc_lab_equipments_jobs j
            JOIN sc_lab_equipments_h e ON j.lab_equipment_id = e.lab_equipment_id
            JOIN sc_jobs s ON j.job_title_id = s.job_title_id
            """;

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM sc_lab_equipments_jobs
            """;

    private static final String COUNT_BY_EQUIPMENT_SQL = """
            SELECT COUNT(*) FROM sc_lab_equipments_jobs WHERE lab_equipment_id = :labEquipmentId
            """;

    private static final String FIND_ALL_PAGED_SQL = SELECT_COLUMNS + """
            ORDER BY j.lab_equipments_job_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_EQUIPMENT_PAGED_SQL = SELECT_COLUMNS + """
            WHERE j.lab_equipment_id = :labEquipmentId
            ORDER BY j.lab_equipments_job_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_ID_SQL = SELECT_COLUMNS + """
            WHERE j.lab_equipments_job_id = :id
            """;

    public PagedDataDto<LabEquipmentJobDto> findAll(int page, int size) {
        long total = jdbcClient.sql(COUNT_SQL).query(Long.class).single();
        List<LabEquipmentJobDto> content = jdbcClient.sql(FIND_ALL_PAGED_SQL)
                .param("offset", page * size)
                .param("size", size)
                .query(LabEquipmentJobDto.class)
                .list();
        return new PagedDataDto<>(content, page, size, total);
    }

    public PagedDataDto<LabEquipmentJobDto> findByLabEquipmentId(Long labEquipmentId, int page, int size) {
        long total = jdbcClient.sql(COUNT_BY_EQUIPMENT_SQL)
                .param("labEquipmentId", labEquipmentId)
                .query(Long.class)
                .single();
        List<LabEquipmentJobDto> content = jdbcClient.sql(FIND_BY_EQUIPMENT_PAGED_SQL)
                .param("labEquipmentId", labEquipmentId)
                .param("offset", page * size)
                .param("size", size)
                .query(LabEquipmentJobDto.class)
                .list();
        return new PagedDataDto<>(content, page, size, total);
    }

    public Optional<LabEquipmentJobDto> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(LabEquipmentJobDto.class)
                .optional();
    }

    public LabEquipmentJobDto create(LabEquipmentJobDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        LabEquipmentJob entity = toEntity(dto);
        entity.setCreatedBy(currentUserId);
        entity.setCreatedOn(now);
        entity.setLastUpdatedBy(currentUserId);
        entity.setLastUpdateDate(now);

        LabEquipmentJob saved = labEquipmentJobRepository.save(entity);
        return findById(saved.getLabEquipmentsJobId()).orElseThrow();
    }

    public Optional<LabEquipmentJobDto> update(Long id, LabEquipmentJobDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        return labEquipmentJobRepository.findById(id).map(existing -> {
            existing.setLabEquipmentId(dto.getLabEquipmentId());
            existing.setJobTitleId(dto.getJobTitleId());
            existing.setLastUpdatedBy(currentUserId);
            existing.setLastUpdateDate(now);
            labEquipmentJobRepository.save(existing);
            return findById(id).orElseThrow();
        });
    }

    public void deleteById(Long id) {
        labEquipmentJobRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return labEquipmentJobRepository.existsById(id);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    private LabEquipmentJob toEntity(LabEquipmentJobDto dto) {
        LabEquipmentJob entity = new LabEquipmentJob();
        // ID intentionally omitted — must be null so JPA generates it on INSERT
        entity.setLabEquipmentId(dto.getLabEquipmentId());
        entity.setJobTitleId(dto.getJobTitleId());
        return entity;
    }
}
