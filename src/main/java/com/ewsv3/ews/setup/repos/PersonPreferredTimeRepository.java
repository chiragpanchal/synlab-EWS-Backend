package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.PersonPreferredTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPreferredTimeRepository extends JpaRepository<PersonPreferredTime, Long> {

    // Find all preferred times by person ID with pagination
    Page<PersonPreferredTime> findByPersonId(Long personId, Pageable pageable);

    // findById, save, and deleteById are inherited from JpaRepository
}
