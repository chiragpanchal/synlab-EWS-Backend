package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.PagedDataDto;
import com.ewsv3.ews.setup.entity.LabEquipmentJobSkill;
import com.ewsv3.ews.setup.entity.LabEquipmentJobSkillDto;
import com.ewsv3.ews.setup.repos.LabEquipmentJobSkillRepository;
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
public class LabEquipmentJobSkillService {

    @Autowired
    private LabEquipmentJobSkillRepository labEquipmentJobSkillRepository;

    private final JdbcClient jdbcClient;

    public LabEquipmentJobSkillService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String SELECT_COLUMNS = """
            SELECT
                js.lab_equipments_job_skill_id,
                js.lab_equipments_job_id,
                js.skill_id,
                sk.skill AS skill_name,
                js.created_by,
                js.created_on,
                js.last_updated_by,
                js.last_update_date
            FROM sc_lab_equipments_job_skills js
            JOIN sc_skills sk ON js.skill_id = sk.skill_id
            """;

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM sc_lab_equipments_job_skills
            """;

    private static final String COUNT_BY_JOB_SQL = """
            SELECT COUNT(*) FROM sc_lab_equipments_job_skills WHERE lab_equipments_job_id = :labEquipmentsJobId
            """;

    private static final String FIND_ALL_PAGED_SQL = SELECT_COLUMNS + """
            ORDER BY js.lab_equipments_job_skill_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_JOB_PAGED_SQL = SELECT_COLUMNS + """
            WHERE js.lab_equipments_job_id = :labEquipmentsJobId
            ORDER BY js.lab_equipments_job_skill_id
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
            """;

    private static final String FIND_BY_ID_SQL = SELECT_COLUMNS + """
            WHERE js.lab_equipments_job_skill_id = :id
            """;

    public PagedDataDto<LabEquipmentJobSkillDto> findAll(int page, int size) {
        long total = jdbcClient.sql(COUNT_SQL).query(Long.class).single();
        List<LabEquipmentJobSkillDto> content = jdbcClient.sql(FIND_ALL_PAGED_SQL)
                .param("offset", page * size)
                .param("size", size)
                .query(LabEquipmentJobSkillDto.class)
                .list();
        return new PagedDataDto<>(content, page, size, total);
    }

    public PagedDataDto<LabEquipmentJobSkillDto> findByLabEquipmentsJobId(Long labEquipmentsJobId, int page, int size) {
        long total = jdbcClient.sql(COUNT_BY_JOB_SQL)
                .param("labEquipmentsJobId", labEquipmentsJobId)
                .query(Long.class)
                .single();
        List<LabEquipmentJobSkillDto> content = jdbcClient.sql(FIND_BY_JOB_PAGED_SQL)
                .param("labEquipmentsJobId", labEquipmentsJobId)
                .param("offset", page * size)
                .param("size", size)
                .query(LabEquipmentJobSkillDto.class)
                .list();
        return new PagedDataDto<>(content, page, size, total);
    }

    public Optional<LabEquipmentJobSkillDto> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(LabEquipmentJobSkillDto.class)
                .optional();
    }

    public LabEquipmentJobSkillDto create(LabEquipmentJobSkillDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        LabEquipmentJobSkill entity = toEntity(dto);
        entity.setCreatedBy(currentUserId);
        entity.setCreatedOn(now);
        entity.setLastUpdatedBy(currentUserId);
        entity.setLastUpdateDate(now);

        LabEquipmentJobSkill saved = labEquipmentJobSkillRepository.save(entity);
        return findById(saved.getLabEquipmentsJobSkillId()).orElseThrow();
    }

    public Optional<LabEquipmentJobSkillDto> update(Long id, LabEquipmentJobSkillDto dto) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();

        return labEquipmentJobSkillRepository.findById(id).map(existing -> {
            existing.setLabEquipmentsJobId(dto.getLabEquipmentsJobId());
            existing.setSkillId(dto.getSkillId());
            existing.setLastUpdatedBy(currentUserId);
            existing.setLastUpdateDate(now);
            labEquipmentJobSkillRepository.save(existing);
            return findById(id).orElseThrow();
        });
    }

    public void deleteById(Long id) {
        labEquipmentJobSkillRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return labEquipmentJobSkillRepository.existsById(id);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    private LabEquipmentJobSkill toEntity(LabEquipmentJobSkillDto dto) {
        LabEquipmentJobSkill entity = new LabEquipmentJobSkill();
        // ID intentionally omitted — must be null so JPA generates it on INSERT
        entity.setLabEquipmentsJobId(dto.getLabEquipmentsJobId());
        entity.setSkillId(dto.getSkillId());
        return entity;
    }
}
