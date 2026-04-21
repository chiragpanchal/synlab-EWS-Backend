package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.PersonPreferredPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonPreferredPatternRepository extends JpaRepository<PersonPreferredPattern, Long> {

    List<PersonPreferredPattern> findByPersonId(Long personId);

    // findById, save, and deleteById are inherited from JpaRepository
}
