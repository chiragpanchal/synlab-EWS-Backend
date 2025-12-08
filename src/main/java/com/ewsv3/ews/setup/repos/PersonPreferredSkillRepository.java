package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.PersonPreferredSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonPreferredSkillRepository extends JpaRepository<PersonPreferredSkill, Long> {

    // Find all skills by person preferred job ID
    @Query("SELECT ps FROM PersonPreferredSkill ps WHERE ps.personPreferredJob.personPreferredJobId = :personPreferredJobId")
    List<PersonPreferredSkill> findByPersonPreferredJobId(@Param("personPreferredJobId") Long personPreferredJobId);

    // findById, save, and deleteById are inherited from JpaRepository
}
