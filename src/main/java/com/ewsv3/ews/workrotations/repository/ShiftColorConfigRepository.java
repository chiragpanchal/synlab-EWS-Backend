package com.ewsv3.ews.workrotations.repository;

import com.ewsv3.ews.workrotations.entity.ShiftColorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftColorConfigRepository extends JpaRepository<ShiftColorConfig, Long> {

    List<ShiftColorConfig> findAllByOrderByStartTimeAsc();

    Optional<ShiftColorConfig> findByColor(String color);

    // Configs whose time range covers the given time
    @Query("SELECT scc FROM ShiftColorConfig scc WHERE :time BETWEEN scc.startTime AND scc.endTime")
    List<ShiftColorConfig> findByTime(@Param("time") Double time);

    // Existing configs overlapping the given range - create case
    @Query("SELECT COUNT(scc) FROM ShiftColorConfig scc WHERE scc.startTime <= :endTime " +
           "AND scc.endTime >= :startTime")
    long countOverlappingForNew(@Param("startTime") Double startTime,
                                @Param("endTime") Double endTime);

    // Existing configs overlapping the given range, excluding the config being updated
    @Query("SELECT COUNT(scc) FROM ShiftColorConfig scc WHERE scc.startTime <= :endTime " +
           "AND scc.endTime >= :startTime " +
           "AND scc.shiftColorConfigId <> :excludeId")
    long countOverlapping(@Param("startTime") Double startTime,
                          @Param("endTime") Double endTime,
                          @Param("excludeId") Long excludeId);

    // findById, findAll, save, deleteById and existsById are inherited from JpaRepository
}
