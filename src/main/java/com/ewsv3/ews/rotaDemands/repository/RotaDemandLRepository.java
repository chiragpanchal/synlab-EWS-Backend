package com.ewsv3.ews.rotaDemands.repository;

import com.ewsv3.ews.rotaDemands.entity.RotaDemandL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaDemandLRepository extends JpaRepository<RotaDemandL, Long> {

    void deleteAllByRotaDemandId(Long rotaDemandId);
}
