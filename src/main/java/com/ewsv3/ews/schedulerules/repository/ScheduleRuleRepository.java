package com.ewsv3.ews.schedulerules.repository;

import com.ewsv3.ews.schedulerules.entity.ScheduleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    
    List<ScheduleRule> findByParentScheduleRuleId(Long parentScheduleRuleId);
    
    List<ScheduleRule> findByParentScheduleRuleIdOrderByValidFromDesc(Long parentScheduleRuleId);
    
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.parentScheduleRuleId = :parentScheduleRuleId " +
           "AND (:validDate IS NULL OR :validDate BETWEEN sr.validFrom AND sr.validTo)")
    List<ScheduleRule> findByParentScheduleRuleIdAndValidDate(@Param("parentScheduleRuleId") Long parentScheduleRuleId,
                                                              @Param("validDate") LocalDate validDate);
    
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.profileId = :profileId " +
           "AND sr.parentScheduleRuleId = :parentScheduleRuleId " +
           "AND (:validDate IS NULL OR :validDate BETWEEN sr.validFrom AND sr.validTo)")
    List<ScheduleRule> findByProfileIdAndParentScheduleRuleIdAndValidDate(@Param("profileId") Long profileId,
                                                                          @Param("parentScheduleRuleId") Long parentScheduleRuleId,
                                                                          @Param("validDate") LocalDate validDate);
    
    List<ScheduleRule> findByProfileIdAndParentScheduleRuleId(Long profileId, Long parentScheduleRuleId);
    
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
    
    @Query("SELECT sr FROM ScheduleRule sr WHERE sr.parentScheduleRuleId IN :parentScheduleRuleIds")
    List<ScheduleRule> findByParentScheduleRuleIds(@Param("parentScheduleRuleIds") List<Long> parentScheduleRuleIds);

    /**
     * Copies the rule values of the given rule onto every rule that references it through
     * PARENT_SCHEDULE_RULE_ID. Native, because the multi-column subquery assignment has no
     * JPQL equivalent.
     * <p>
     * {@code flushAutomatically} matters here: the parent rule is updated through the entity
     * manager just before this runs, so the pending change has to reach the database before the
     * subquery reads it back. {@code clearAutomatically} drops any child rules already loaded in
     * the persistence context, which this bulk statement would otherwise leave stale.
     *
     * @return the number of child rules updated
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE sc_schedule_rules cr
            SET (
                cr.valid_from,
                cr.valid_to,
                cr.max_hrs_per_day,
                cr.min_hrs_per_week,
                cr.max_hrs_per_week,
                cr.min_hrs_per_month,
                cr.max_hrs_per_month,
                cr.shift_gap,
                cr.min_rest_days_per_week,
                cr.max_cont_shift_days,
                cr.max_cont_rest_days
            ) = (
                SELECT
                    pr.valid_from,
                    pr.valid_to,
                    pr.max_hrs_per_day,
                    pr.min_hrs_per_week,
                    pr.max_hrs_per_week,
                    pr.min_hrs_per_month,
                    pr.max_hrs_per_month,
                    pr.shift_gap,
                    pr.min_rest_days_per_week,
                    pr.max_cont_shift_days,
                    pr.max_cont_rest_days
                FROM sc_schedule_rules pr
                WHERE pr.schedule_rule_id = :scheduleRuleId
            )
            WHERE cr.schedule_rule_id IN (
                SELECT schedule_rule_id
                FROM sc_schedule_rules
                START WITH parent_schedule_rule_id = :scheduleRuleId
                CONNECT BY NOCYCLE
                    PRIOR schedule_rule_id = parent_schedule_rule_id
            )
            """, nativeQuery = true)
    int syncChildRulesWithParent(@Param("scheduleRuleId") Long scheduleRuleId);
}