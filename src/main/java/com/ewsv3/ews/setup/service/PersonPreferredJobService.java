package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.setup.entity.PersonPreferredJob;
import com.ewsv3.ews.setup.repos.PersonPreferredJobRepository;
import com.ewsv3.ews.team.dto.TeamTimecardSimple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ewsv3.ews.setup.service.PersonPreferredJobSql;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PersonPreferredJobService {

    @Autowired
    private PersonPreferredJobRepository personPreferredJobRepository;

    /**
     * Get all person preferred jobs by person ID with pagination
     */
    public Page<PersonPreferredJob> findByPersonId(Long personId, Pageable pageable) {
        return personPreferredJobRepository.findByPersonId(personId, pageable);
    }

    /**
     * Get person preferred job by ID
     */
    public Optional<PersonPreferredJob> findById(Long id) {
        return personPreferredJobRepository.findById(id);
    }

    /**
     * Save or update person preferred job
     */
    public PersonPreferredJob save(PersonPreferredJob personPreferredJob) {
        return personPreferredJobRepository.save(personPreferredJob);
    }

    /**
     * Delete person preferred job by ID
     */
    public void deleteById(Long id) {
        personPreferredJobRepository.deleteById(id);
    }

    /**
     * Check if person preferred job exists
     */
    public boolean existsById(Long id) {
        return personPreferredJobRepository.existsById(id);
    }

    public List<TeamTimecardSimple> getTeamList(long userId, Long profileId, LocalDate startDate, LocalDate endDate,
            int page, int size, String text, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = Map.of(
                "userId", userId,
                "profileId", profileId,
                "startDate", startDate,
                "endDate", endDate,
                "offset", page,
                "pageSize", size,
                "text", "%" + text + "%");

        // System.out.println("getTeamTimecardsSimpleV2 objectMap:" + objectMap);

        List<TeamTimecardSimple> timecardSimples = jdbcClient
                .sql(PersonPreferredJobSql.TeamSql)
                .params(objectMap)
                .query(TeamTimecardSimple.class)
                .list();

        return timecardSimples;
    }
}
