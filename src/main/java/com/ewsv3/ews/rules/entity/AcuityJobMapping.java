package com.ewsv3.ews.rules.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SC_ACUITY_JOB_MAPPINGS")
@SequenceGenerator(name = "acuity_job_mapping_id_gen", sequenceName = "acuity_joib_mapping_id_sq", allocationSize = 1)
public class AcuityJobMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acuity_job_mapping_id_gen")
    @Column(name = "ACUITY_JOIB_MAPPING_ID")
    private Long acuityJobMappingId;

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @Column(name = "FOR_JOB_TITLE_ID")
    private Long forJobTitleId;

    @Column(name = "FOR_FTE")
    private BigDecimal forFte;

    @Column(name = "REQ_JOIB_TITLE_ID")
    private Long reqJobTitleId;

    @Column(name = "REQ_FTE")
    private BigDecimal reqFte;

    public AcuityJobMapping() {}

    public AcuityJobMapping(Long profileId, Long forJobTitleId, BigDecimal forFte,
                           Long reqJobTitleId, BigDecimal reqFte) {
        this.profileId = profileId;
        this.forJobTitleId = forJobTitleId;
        this.forFte = forFte;
        this.reqJobTitleId = reqJobTitleId;
        this.reqFte = reqFte;
    }

    public Long getAcuityJobMappingId() {
        return acuityJobMappingId;
    }

    public void setAcuityJobMappingId(Long acuityJobMappingId) {
        this.acuityJobMappingId = acuityJobMappingId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getForJobTitleId() {
        return forJobTitleId;
    }

    public void setForJobTitleId(Long forJobTitleId) {
        this.forJobTitleId = forJobTitleId;
    }

    public BigDecimal getForFte() {
        return forFte;
    }

    public void setForFte(BigDecimal forFte) {
        this.forFte = forFte;
    }

    public Long getReqJobTitleId() {
        return reqJobTitleId;
    }

    public void setReqJobTitleId(Long reqJobTitleId) {
        this.reqJobTitleId = reqJobTitleId;
    }

    public BigDecimal getReqFte() {
        return reqFte;
    }

    public void setReqFte(BigDecimal reqFte) {
        this.reqFte = reqFte;
    }
}
