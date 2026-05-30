package com.ewsv3.ews.rotaDemands.repository;

import com.ewsv3.ews.rotaDemands.entity.RotaDemandH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaDemandHRepository extends JpaRepository<RotaDemandH, Long> {

    boolean existsByDemandName(String demandName);
}
