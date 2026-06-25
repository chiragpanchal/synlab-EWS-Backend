package com.ewsv3.ews.rosters.dto.healthcare;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AcuityDataDto {
    private LocalDate effectiveDate;
    private String levelName;
    private Integer patientCounts;
    private String derivedRatio;
    private BigDecimal reqFte;

    public AcuityDataDto() {}

    public AcuityDataDto(LocalDate effectiveDate, String levelName, Integer patientCounts,
                         String derivedRatio, BigDecimal reqFte) {
        this.effectiveDate = effectiveDate;
        this.levelName = levelName;
        this.patientCounts = patientCounts;
        this.derivedRatio = derivedRatio;
        this.reqFte = reqFte;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getPatientCounts() {
        return patientCounts;
    }

    public void setPatientCounts(Integer patientCounts) {
        this.patientCounts = patientCounts;
    }

    public String getDerivedRatio() {
        return derivedRatio;
    }

    public void setDerivedRatio(String derivedRatio) {
        this.derivedRatio = derivedRatio;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
