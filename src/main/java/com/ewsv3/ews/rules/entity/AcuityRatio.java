package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SC_ACUITY_RATIO")
@SequenceGenerator(name = "acuity_ratio_id_gen", sequenceName = "acuity_ratio_id_sq", allocationSize = 1)
public class AcuityRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acuity_ratio_id_gen")
    @Column(name = "RATIO_ID")
    private Long ratioId;

    @Column(name = "RATIO_NAME", length = 100)
    private String ratioName;

    @Column(name = "BEDS", nullable = false)
    private BigDecimal beds;

    @Column(name = "REQ_FTE")
    private BigDecimal reqFte;

    public AcuityRatio() {}

    public AcuityRatio(Long ratioId, String ratioName, BigDecimal beds, BigDecimal reqFte) {
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
