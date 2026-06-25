package com.ewsv3.ews.rules.dto.req;

import java.math.BigDecimal;

public class AcuityRatioRequest {
    private String ratioName;
    private BigDecimal beds;
    private BigDecimal reqFte;

    public AcuityRatioRequest() {}

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
