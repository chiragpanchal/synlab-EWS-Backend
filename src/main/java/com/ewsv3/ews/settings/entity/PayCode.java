package com.ewsv3.ews.settings.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SC_PAY_CODES", uniqueConstraints = {
    @UniqueConstraint(name = "SC_PAY_CODES_UK_1", columnNames = {"ENTERPRISE_ID", "PAY_CODE"}),
    @UniqueConstraint(name = "SC_PAY_CODES_UK_2", columnNames = {"ENTERPRISE_ID", "PAY_CODE_NAME"})
})
public class PayCode {

    @Id
    @Column(name = "PAY_CODE_ID")
    private Long payCodeId;

    @Column(name = "PAY_CODE", nullable = false, length = 1000)
    private String payCode;

    @Column(name = "PAY_CODE_NAME", nullable = false, length = 1000)
    private String payCodeName;

    @Column(name = "PAY_MULTIPLIER", nullable = false)
    private BigDecimal payMultiplier;

    @Column(name = "ELEMENT_TYPE_ID")
    private Long elementTypeId;

    @Column(name = "INPUT_VALUE_ID")
    private Long inputValueId;

    @Column(name = "ENTERPRISE_ID", nullable = false)
    private Long enterpriseId;

    @Column(name = "ENABLED", length = 1000)
    private String enabled;

    @Column(name = "PAYROLL_AUDIT", length = 1000)
    private String payrollAudit;

    @Column(name = "ELEMENT_NAME", length = 1000)
    private String elementName;

    @Column(name = "ALLW_HOUR_CODE", length = 1)
    private String allwHourCode;

    @Column(name = "REMARK_INPUT_VALUE_ID")
    private Long remarkInputValueId;

    @Column(name = "ELEMENT_LINK_ID")
    private Long elementLinkId;

    @Column(name = "CONSIDER_IN_TOTAL", length = 1)
    private String considerInTotal;

    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "LAST_UPDATED_BY", nullable = false)
    private Long lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    // Default constructor
    public PayCode() {}

    // Constructor with all fields
    public PayCode(Long payCodeId, String payCode, String payCodeName, BigDecimal payMultiplier,
                   Long elementTypeId, Long inputValueId, Long enterpriseId, String enabled,
                   String payrollAudit, String elementName, String allwHourCode,
                   Long remarkInputValueId, Long elementLinkId, String considerInTotal,
                   Long createdBy, Date createdOn, Long lastUpdatedBy, Date lastUpdateDate) {
        this.payCodeId = payCodeId;
        this.payCode = payCode;
        this.payCodeName = payCodeName;
        this.payMultiplier = payMultiplier;
        this.elementTypeId = elementTypeId;
        this.inputValueId = inputValueId;
        this.enterpriseId = enterpriseId;
        this.enabled = enabled;
        this.payrollAudit = payrollAudit;
        this.elementName = elementName;
        this.allwHourCode = allwHourCode;
        this.remarkInputValueId = remarkInputValueId;
        this.elementLinkId = elementLinkId;
        this.considerInTotal = considerInTotal;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    // Getters and Setters
    public Long getPayCodeId() {
        return payCodeId;
    }

    public void setPayCodeId(Long payCodeId) {
        this.payCodeId = payCodeId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayCodeName() {
        return payCodeName;
    }

    public void setPayCodeName(String payCodeName) {
        this.payCodeName = payCodeName;
    }

    public BigDecimal getPayMultiplier() {
        return payMultiplier;
    }

    public void setPayMultiplier(BigDecimal payMultiplier) {
        this.payMultiplier = payMultiplier;
    }

    public Long getElementTypeId() {
        return elementTypeId;
    }

    public void setElementTypeId(Long elementTypeId) {
        this.elementTypeId = elementTypeId;
    }

    public Long getInputValueId() {
        return inputValueId;
    }

    public void setInputValueId(Long inputValueId) {
        this.inputValueId = inputValueId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getPayrollAudit() {
        return payrollAudit;
    }

    public void setPayrollAudit(String payrollAudit) {
        this.payrollAudit = payrollAudit;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getAllwHourCode() {
        return allwHourCode;
    }

    public void setAllwHourCode(String allwHourCode) {
        this.allwHourCode = allwHourCode;
    }

    public Long getRemarkInputValueId() {
        return remarkInputValueId;
    }

    public void setRemarkInputValueId(Long remarkInputValueId) {
        this.remarkInputValueId = remarkInputValueId;
    }

    public Long getElementLinkId() {
        return elementLinkId;
    }

    public void setElementLinkId(Long elementLinkId) {
        this.elementLinkId = elementLinkId;
    }

    public String getConsiderInTotal() {
        return considerInTotal;
    }

    public void setConsiderInTotal(String considerInTotal) {
        this.considerInTotal = considerInTotal;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
