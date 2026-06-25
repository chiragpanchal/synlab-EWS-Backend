package com.ewsv3.ews.rules.repository;

import com.ewsv3.ews.rules.entity.AcuityJobMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcuityJobMappingRepository extends JpaRepository<AcuityJobMapping, Long> {
    List<AcuityJobMapping> findByProfileId(Long profileId);
    List<AcuityJobMapping> findByForJobTitleId(Long forJobTitleId);
    List<AcuityJobMapping> findByReqJobTitleId(Long reqJobTitleId);
    List<AcuityJobMapping> findByProfileIdAndForJobTitleId(Long profileId, Long forJobTitleId);
    List<AcuityJobMapping> findByProfileIdAndReqJobTitleId(Long profileId, Long reqJobTitleId);
    Optional<AcuityJobMapping> findByProfileIdAndForJobTitleIdAndReqJobTitleId(Long profileId, Long forJobTitleId, Long reqJobTitleId);
}
