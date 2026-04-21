package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.setup.entity.PersonPreferredPattern;
import com.ewsv3.ews.setup.entity.PersonPreferredPatternDto;
import com.ewsv3.ews.setup.repos.PersonPreferredPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonPreferredPatternService {

    @Autowired
    private PersonPreferredPatternRepository personPreferredPatternRepository;

    private final JdbcClient jdbcClient;

    public PersonPreferredPatternService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String FIND_BY_PERSON_SQL = """
            SELECT
                p.person_preferred_pattern_id,
                p.person_id,
                p.work_rotation_id,
                w.work_rotation_name,
                p.created_by,
                p.created_on,
                p.last_updated_by,
                p.last_update_date
            FROM sc_person_preferred_patterns p
            JOIN sc_work_rotations_h w ON p.work_rotation_id = w.work_rotation_id
            WHERE p.person_id = :personId
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT
                p.person_preferred_pattern_id,
                p.person_id,
                p.work_rotation_id,
                w.work_rotation_name,
                p.created_by,
                p.created_on,
                p.last_updated_by,
                p.last_update_date
            FROM sc_person_preferred_patterns p
            JOIN sc_work_rotations_h w ON p.work_rotation_id = w.work_rotation_id
            WHERE p.person_preferred_pattern_id = :id
            """;

    public List<PersonPreferredPatternDto> findByPersonId(Long personId) {
        return jdbcClient.sql(FIND_BY_PERSON_SQL)
                .param("personId", personId)
                .query(PersonPreferredPatternDto.class)
                .list();
    }

    public Optional<PersonPreferredPatternDto> findByIdDto(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
                .param("id", id)
                .query(PersonPreferredPatternDto.class)
                .optional();
    }

    public Optional<PersonPreferredPattern> findById(Long id) {
        return personPreferredPatternRepository.findById(id);
    }

    public PersonPreferredPattern save(PersonPreferredPattern personPreferredPattern) {
        return personPreferredPatternRepository.save(personPreferredPattern);
    }

    public void deleteById(Long id) {
        personPreferredPatternRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return personPreferredPatternRepository.existsById(id);
    }
}
