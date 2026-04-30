package com.ewsv3.ews.setup.repos;

import com.ewsv3.ews.setup.entity.LabEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabEquipmentRepository extends JpaRepository<LabEquipment, Long> {

    boolean existsByEqCode(String eqCode);

    boolean existsByEqName(String eqName);
}
