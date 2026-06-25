package com.ewsv3.ews.rules.repository;

import com.ewsv3.ews.rules.entity.AcuityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcuityLevelRepository extends JpaRepository<AcuityLevel, Long> {
    Optional<AcuityLevel> findByLevelName(String levelName);
    List<AcuityLevel> findAllByOrderByLevelNameAsc();
}
