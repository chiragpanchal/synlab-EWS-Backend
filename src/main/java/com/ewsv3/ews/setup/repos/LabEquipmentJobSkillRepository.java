package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.LabEquipmentJobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabEquipmentJobSkillRepository extends JpaRepository<LabEquipmentJobSkill, Long> {
}
