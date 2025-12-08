package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.PersonPreferredJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPreferredJobRepository extends JpaRepository<PersonPreferredJob, Long> {

    // Find all jobs by person ID with pagination
    Page<PersonPreferredJob> findByPersonId(Long personId, Pageable pageable);

    // findById, save, and deleteById are inherited from JpaRepository
}
