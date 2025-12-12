package com.ewsv3.ews.settings.repository;

import com.ewsv3.ews.settings.entity.PayCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayCodeRepository extends JpaRepository<PayCode, Long> {

    // Find all pay codes ordered by pay code name
    List<PayCode> findAllByOrderByPayCodeNameAsc();

    // Find pay code by enterprise ID and pay code
    Optional<PayCode> findByEnterpriseIdAndPayCode(Long enterpriseId, String payCode);

    // Find pay code by enterprise ID and pay code name
    Optional<PayCode> findByEnterpriseIdAndPayCodeName(Long enterpriseId, String payCodeName);

    // Check if pay code exists by enterprise ID and pay code
    boolean existsByEnterpriseIdAndPayCode(Long enterpriseId, String payCode);

    // Check if pay code exists by enterprise ID and pay code name
    boolean existsByEnterpriseIdAndPayCodeName(Long enterpriseId, String payCodeName);

    // findById, save, and deleteById are inherited from JpaRepository
}
