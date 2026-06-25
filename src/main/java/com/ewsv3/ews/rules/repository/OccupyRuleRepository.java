package com.ewsv3.ews.rules.repository;

import com.ewsv3.ews.rules.entity.OccupyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OccupyRuleRepository extends JpaRepository<OccupyRule, Long> {
    List<OccupyRule> findByProfileId(Long profileId);
    List<OccupyRule> findByJobTitleId(Long jobTitleId);
    List<OccupyRule> findByWorkDurationId(Long workDurationId);
    List<OccupyRule> findByProfileIdAndJobTitleId(Long profileId, Long jobTitleId);
    Optional<OccupyRule> findByProfileIdAndJobTitleIdAndWorkDurationId(Long profileId, Long jobTitleId, Long workDurationId);
}
