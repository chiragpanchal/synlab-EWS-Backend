package com.ewsv3.ews.rules.dto;

import java.math.BigDecimal;

public class AcuityRatioDto {
    private Long ratioId;
    private String ratioName;
    private BigDecimal beds;
    private BigDecimal reqFte;

    public AcuityRatioDto() {}

    public AcuityRatioDto(Long ratioId, String ratioName, BigDecimal beds, BigDecimal reqFte) {
        this.ratioId = ratioId;
        this.ratioName = ratioName;
        this.beds = beds;
        this.reqFte = reqFte;
    }

    public Long getRatioId() {
        return ratioId;
    }

    public void setRatioId(Long ratioId) {
        this.ratioId = ratioId;
    }

    public String getRatioName() {
        return ratioName;
    }

    public void setRatioName(String ratioName) {
        this.ratioName = ratioName;
    }

    public BigDecimal getBeds() {
        return beds;
    }

    public void setBeds(BigDecimal beds) {
        this.beds = beds;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
