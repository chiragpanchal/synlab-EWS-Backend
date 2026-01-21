package com.ewsv3.ews.rosters.dto.rosters;

public class AlternatePersonDto {
    Long personId;
    String personName;
    String employeeNumber;
    String gradeName;
    Double rate;
    Double schHrs;
    Double matchedPerc;

    public AlternatePersonDto() {
    }

    public AlternatePersonDto(Long personId, String personName, String employeeNumber, String gradeName, Double rate, Double schHrs, Double matchedPerc) {
        this.personId = personId;
        this.personName = personName;
        this.employeeNumber = employeeNumber;
        this.gradeName = gradeName;
        this.rate = rate;
        this.schHrs = schHrs;
        this.matchedPerc = matchedPerc;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public Double getMatchedPerc() {
        return matchedPerc;
    }

    public void setMatchedPerc(Double matchedPerc) {
        this.matchedPerc = matchedPerc;
    }
}
