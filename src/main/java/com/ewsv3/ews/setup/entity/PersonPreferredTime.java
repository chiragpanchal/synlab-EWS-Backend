package com.ewsv3.ews.setup.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_PERSON_PREFERRED_TIMES")
public class PersonPreferredTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_PREFERRED_TIME_ID")
    private Long personPreferredTimeId;

    @Column(name = "PERSON_ID", nullable = false)
    private Long personId;

    @Column(name = "SUN_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date sunStartTime;

    @Column(name = "SUN_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date sunEndTime;

    @Column(name = "MON_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date monStartTime;

    @Column(name = "MON_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date monEndTime;

    @Column(name = "TUE_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date tueStartTime;

    @Column(name = "TUE_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date tueEndTime;

    @Column(name = "WED_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date wedStartTime;

    @Column(name = "WED_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date wedEndTime;

    @Column(name = "THU_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date thuStartTime;

    @Column(name = "THU_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date thuEndTime;

    @Column(name = "FRI_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date friStartTime;

    @Column(name = "FRI_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date friEndTime;

    @Column(name = "SAT_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date satStartTime;

    @Column(name = "SAT_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date satEndTime;

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
    public PersonPreferredTime() {}

    // Constructor with all fields
    public PersonPreferredTime(Long personPreferredTimeId, Long personId, Date sunStartTime, Date sunEndTime,
                              Date monStartTime, Date monEndTime, Date tueStartTime, Date tueEndTime,
                              Date wedStartTime, Date wedEndTime, Date thuStartTime, Date thuEndTime,
                              Date friStartTime, Date friEndTime, Date satStartTime, Date satEndTime,
                              Long createdBy, Date createdOn, Long lastUpdatedBy, Date lastUpdateDate) {
        this.personPreferredTimeId = personPreferredTimeId;
        this.personId = personId;
        this.sunStartTime = sunStartTime;
        this.sunEndTime = sunEndTime;
        this.monStartTime = monStartTime;
        this.monEndTime = monEndTime;
        this.tueStartTime = tueStartTime;
        this.tueEndTime = tueEndTime;
        this.wedStartTime = wedStartTime;
        this.wedEndTime = wedEndTime;
        this.thuStartTime = thuStartTime;
        this.thuEndTime = thuEndTime;
        this.friStartTime = friStartTime;
        this.friEndTime = friEndTime;
        this.satStartTime = satStartTime;
        this.satEndTime = satEndTime;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    // Getters and Setters
    public Long getPersonPreferredTimeId() {
        return personPreferredTimeId;
    }

    public void setPersonPreferredTimeId(Long personPreferredTimeId) {
        this.personPreferredTimeId = personPreferredTimeId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Date getSunStartTime() {
        return sunStartTime;
    }

    public void setSunStartTime(Date sunStartTime) {
        this.sunStartTime = sunStartTime;
    }

    public Date getSunEndTime() {
        return sunEndTime;
    }

    public void setSunEndTime(Date sunEndTime) {
        this.sunEndTime = sunEndTime;
    }

    public Date getMonStartTime() {
        return monStartTime;
    }

    public void setMonStartTime(Date monStartTime) {
        this.monStartTime = monStartTime;
    }

    public Date getMonEndTime() {
        return monEndTime;
    }

    public void setMonEndTime(Date monEndTime) {
        this.monEndTime = monEndTime;
    }

    public Date getTueStartTime() {
        return tueStartTime;
    }

    public void setTueStartTime(Date tueStartTime) {
        this.tueStartTime = tueStartTime;
    }

    public Date getTueEndTime() {
        return tueEndTime;
    }

    public void setTueEndTime(Date tueEndTime) {
        this.tueEndTime = tueEndTime;
    }

    public Date getWedStartTime() {
        return wedStartTime;
    }

    public void setWedStartTime(Date wedStartTime) {
        this.wedStartTime = wedStartTime;
    }

    public Date getWedEndTime() {
        return wedEndTime;
    }

    public void setWedEndTime(Date wedEndTime) {
        this.wedEndTime = wedEndTime;
    }

    public Date getThuStartTime() {
        return thuStartTime;
    }

    public void setThuStartTime(Date thuStartTime) {
        this.thuStartTime = thuStartTime;
    }

    public Date getThuEndTime() {
        return thuEndTime;
    }

    public void setThuEndTime(Date thuEndTime) {
        this.thuEndTime = thuEndTime;
    }

    public Date getFriStartTime() {
        return friStartTime;
    }

    public void setFriStartTime(Date friStartTime) {
        this.friStartTime = friStartTime;
    }

    public Date getFriEndTime() {
        return friEndTime;
    }

    public void setFriEndTime(Date friEndTime) {
        this.friEndTime = friEndTime;
    }

    public Date getSatStartTime() {
        return satStartTime;
    }

    public void setSatStartTime(Date satStartTime) {
        this.satStartTime = satStartTime;
    }

    public Date getSatEndTime() {
        return satEndTime;
    }

    public void setSatEndTime(Date satEndTime) {
        this.satEndTime = satEndTime;
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
