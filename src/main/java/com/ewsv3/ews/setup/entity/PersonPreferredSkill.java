package com.ewsv3.ews.setup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_PERSON_PREFERRED_SKILLS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PERSON_PREFERRED_JOB_ID", "SKILL_ID"})
})
public class PersonPreferredSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_PREFERRED_SKILL_ID")
    private Long personPreferredSkillId;

    @Column(name = "SKILL_ID", nullable = false)
    private Long skillId;

    @Column(name = "PERSON_PREFERRED_JOB_ID", nullable = false)
    private Long personPreferredJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_PREFERRED_JOB_ID", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private PersonPreferredJob personPreferredJob;

    @Column(name = "RATING", nullable = false)
    private Long rating;

    @Column(name = "COMMENTS", length = 4000)
    private String comments;

    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @Column(name = "CREATED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "LAST_UPDATED_BY", nullable = false)
    private Long lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "PERSON_ID", nullable = false)
    private Long personId;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    // Default constructor
    public PersonPreferredSkill() {}

    // Constructor with all fields
    public PersonPreferredSkill(Long personPreferredSkillId, Long skillId, Long personPreferredJobId,
                               PersonPreferredJob personPreferredJob, Long rating, String comments,
                               Long createdBy, Date createdOn, Long lastUpdatedBy, Date lastUpdateDate,
                               Long personId, Date endDate) {
        this.personPreferredSkillId = personPreferredSkillId;
        this.skillId = skillId;
        this.personPreferredJobId = personPreferredJobId;
        this.personPreferredJob = personPreferredJob;
        this.rating = rating;
        this.comments = comments;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
        this.personId = personId;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getPersonPreferredSkillId() {
        return personPreferredSkillId;
    }

    public void setPersonPreferredSkillId(Long personPreferredSkillId) {
        this.personPreferredSkillId = personPreferredSkillId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public Long getPersonPreferredJobId() {
        return personPreferredJobId;
    }

    public void setPersonPreferredJobId(Long personPreferredJobId) {
        this.personPreferredJobId = personPreferredJobId;
    }

    public PersonPreferredJob getPersonPreferredJob() {
        return personPreferredJob;
    }

    public void setPersonPreferredJob(PersonPreferredJob personPreferredJob) {
        this.personPreferredJob = personPreferredJob;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
