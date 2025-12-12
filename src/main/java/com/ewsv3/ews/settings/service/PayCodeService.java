package com.ewsv3.ews.settings.service;

import com.ewsv3.ews.settings.entity.PayCode;
import com.ewsv3.ews.settings.repository.PayCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PayCodeService {

    @Autowired
    private PayCodeRepository payCodeRepository;

    /**
     * Get all pay codes ordered by pay code name
     */
    public List<PayCode> findAllOrderedByPayCodeName() {
        return payCodeRepository.findAllByOrderByPayCodeNameAsc();
    }

    /**
     * Get pay code by ID
     */
    public Optional<PayCode> findById(Long id) {
        return payCodeRepository.findById(id);
    }

    /**
     * Get pay code by enterprise ID and pay code
     */
    public Optional<PayCode> findByEnterpriseIdAndPayCode(Long enterpriseId, String payCode) {
        return payCodeRepository.findByEnterpriseIdAndPayCode(enterpriseId, payCode);
    }

    /**
     * Get pay code by enterprise ID and pay code name
     */
    public Optional<PayCode> findByEnterpriseIdAndPayCodeName(Long enterpriseId, String payCodeName) {
        return payCodeRepository.findByEnterpriseIdAndPayCodeName(enterpriseId, payCodeName);
    }

    /**
     * Save or update pay code
     */
    public PayCode save(PayCode payCode) {
        return payCodeRepository.save(payCode);
    }

    /**
     * Delete pay code by ID
     */
    public void deleteById(Long id) {
        payCodeRepository.deleteById(id);
    }

    /**
     * Check if pay code exists
     */
    public boolean existsById(Long id) {
        return payCodeRepository.existsById(id);
    }

    /**
     * Check if pay code exists by enterprise ID and pay code
     */
    public boolean existsByEnterpriseIdAndPayCode(Long enterpriseId, String payCode) {
        return payCodeRepository.existsByEnterpriseIdAndPayCode(enterpriseId, payCode);
    }

    /**
     * Check if pay code exists by enterprise ID and pay code name
     */
    public boolean existsByEnterpriseIdAndPayCodeName(Long enterpriseId, String payCodeName) {
        return payCodeRepository.existsByEnterpriseIdAndPayCodeName(enterpriseId, payCodeName);
    }

    /**
     * Get all pay codes
     */
    public List<PayCode> findAll() {
        return payCodeRepository.findAll();
    }

    /**
     * Get pay codes list for dropdown using JdbcClient (similar to rules pattern)
     */
    public List<PayCode> getPayCodesList(JdbcClient jdbcClient) {
        String sql = """
            SELECT
                PAY_CODE_ID as payCodeId,
                PAY_CODE as payCode,
                PAY_CODE_NAME as payCodeName,
                PAY_MULTIPLIER as payMultiplier,
                ELEMENT_TYPE_ID as elementTypeId,
                INPUT_VALUE_ID as inputValueId,
                ENTERPRISE_ID as enterpriseId,
                ENABLED as enabled,
                PAYROLL_AUDIT as payrollAudit,
                ELEMENT_NAME as elementName,
                ALLW_HOUR_CODE as allwHourCode,
                REMARK_INPUT_VALUE_ID as remarkInputValueId,
                ELEMENT_LINK_ID as elementLinkId,
                CONSIDER_IN_TOTAL as considerInTotal,
                CREATED_BY as createdBy,
                CREATED_ON as createdOn,
                LAST_UPDATED_BY as lastUpdatedBy,
                LAST_UPDATE_DATE as lastUpdateDate
            FROM SC_PAY_CODES
            ORDER BY PAY_CODE_NAME
            """;

        List<PayCode> payCodesList = jdbcClient.sql(sql)
                .query(PayCode.class)
                .list();

        return payCodesList;
    }
}
