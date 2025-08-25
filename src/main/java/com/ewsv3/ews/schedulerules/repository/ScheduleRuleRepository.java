package com.ewsv3.ews.schedulerules.repository;

import com.ewsv3.ews.schedulerules.entity.ScheduleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRuleRepository extends JpaRepository<ScheduleRule, Long> {
    
    List<ScheduleRule> findByProfileId(Long profileId);
    
    List<ScheduleRule> findByProfileIdOrderByValidFromDesc(Long profileId);
    
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.profileId = :profileId " +
           "AND (:validDate IS NULL OR :validDate BETWEEN sr.validFrom AND sr.validTo)")
    List<ScheduleRule> findByProfileIdAndValidDate(@Param("profileId") Long profileId, 
                                                   @Param("validDate") LocalDate validDate);
    
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.validFrom <= :date AND sr.validTo >= :date")
    List<ScheduleRule> findActiveScheduleRules(@Param("date") LocalDate date);
    
    boolean existsByProfileIdAndScheduleRuleIdNot(Long profileId, Long scheduleRuleId);
    
    @Query("SELECT COUNT(sr) FROM ScheduleRule sr WHERE sr.profileId = :profileId " +
           "AND sr.scheduleRuleId != :excludeRuleId " +
           "AND ((sr.validFrom <= :validTo AND sr.validTo >= :validFrom))")
    long countOverlappingScheduleRules(@Param("profileId") Long profileId, 
                                      @Param("validFrom") LocalDate validFrom, 
                                      @Param("validTo") LocalDate validTo, 
                                      @Param("excludeRuleId") Long excludeRuleId);
    
    @Query("SELECT COUNT(sr) FROM ScheduleRule sr WHERE sr.profileId = :profileId " +
           "AND ((sr.validFrom <= :validTo AND sr.validTo >= :validFrom))")
    long countOverlappingScheduleRulesForNew(@Param("profileId") Long profileId, 
                                            @Param("validFrom") LocalDate validFrom, 
                                            @Param("validTo") LocalDate validTo);
                                            
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.profileId IN :profileIds")
    List<ScheduleRule> findByProfileIds(@Param("profileIds") List<Long> profileIds);
}