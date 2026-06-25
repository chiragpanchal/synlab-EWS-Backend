package com.ewsv3.ews.rules.dto;

public class AcuityLevelDto {
    private Long acuityLevelId;
    private String levelName;

    public AcuityLevelDto() {}

    public AcuityLevelDto(Long acuityLevelId, String levelName) {
        this.acuityLevelId = acuityLevelId;
        this.levelName = levelName;
    }

    public Long getAcuityLevelId() {
        return acuityLevelId;
    }

    public void setAcuityLevelId(Long acuityLevelId) {
        this.acuityLevelId = acuityLevelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
