package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SC_ACUITY_LEVELS")
@SequenceGenerator(name = "acuity_level_id_gen", sequenceName = "acuity_level_id_sq", allocationSize = 1)
public class AcuityLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acuity_level_id_gen")
    @Column(name = "ACUITY_LEVEL_ID")
    private Long acuityLevelId;

    @Column(name = "LEVEL_NAME", length = 100, nullable = false)
    private String levelName;

    public AcuityLevel() {}

    public AcuityLevel(Long acuityLevelId, String levelName) {
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
