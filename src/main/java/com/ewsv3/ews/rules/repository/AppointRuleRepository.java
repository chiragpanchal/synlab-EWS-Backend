package com.ewsv3.ews.rules.repository;

import com.ewsv3.ews.rules.entity.AppointRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointRuleRepository extends JpaRepository<AppointRule, Long> {
    List<AppointRule> findByProfileId(Long profileId);
    List<AppointRule> findByJobTitleId(Long jobTitleId);
    List<AppointRule> findByProfileIdAndJobTitleId(Long profileId, Long jobTitleId);
}
