package com.ewsv3.ews.rules.repository;

import com.ewsv3.ews.rules.entity.AcuityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcuityRuleRepository extends JpaRepository<AcuityRule, Long> {
    List<AcuityRule> findByProfileId(Long profileId);
    List<AcuityRule> findByAcuityLevelId(Long acuityLevelId);
    List<AcuityRule> findByRatioId(Long ratioId);
    List<AcuityRule> findByProfileIdAndAcuityLevelId(Long profileId, Long acuityLevelId);
    Optional<AcuityRule> findByProfileIdAndAcuityLevelIdAndRatioId(Long profileId, Long acuityLevelId, Long ratioId);
}
