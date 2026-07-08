package com.ewsv3.ews.userpref.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SC_USER_PREF")
public class UserPref {

    @Id
    @Column(name = "USER_PREF_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_pref_seq")
    @SequenceGenerator(name = "user_pref_seq", sequenceName = "USER_PREF_ID_SQ", allocationSize = 1)
    private Long userPrefId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TZ_INTERNAL_NAME", length = 1000)
    private String tzInternalName;

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

    @Column(name = "TSAR_MON", nullable = false, length = 1000)
    private String tsarMon;

    @Column(name = "TSAR_TUE", nullable = false, length = 1000)
    private String tsarTue;

    @Column(name = "TSAR_WED", nullable = false, length = 1000)
    private String tsarWed;

    @Column(name = "TSAR_THU", nullable = false, length = 1000)
    private String tsarThu;

    @Column(name = "TSAR_FRI", nullable = false, length = 1000)
    private String tsarFri;

    @Column(name = "TSAR_SAT", nullable = false, length = 1000)
    private String tsarSat;

    @Column(name = "TSAR_SUN", nullable = false, length = 1000)
    private String tsarSun;

    @Column(name = "TSAR_HOUR")
    private Integer tsarHour;

    @Column(name = "TSAR_MINUTE")
    private Integer tsarMinute;

    @Column(name = "TSAR_NOTIFY_TYPE", length = 1000)
    private String tsarNotifyType;

    @Column(name = "TIME_12_24", length = 100)
    private String time12_24;

    public UserPref() {}

    public UserPref(Long userPrefId, Long userId, String tzInternalName, Long createdBy, Date createdOn,
                   Long lastUpdatedBy, Date lastUpdateDate, String tsarMon, String tsarTue, String tsarWed,
                   String tsarThu, String tsarFri, String tsarSat, String tsarSun, Integer tsarHour,
                   Integer tsarMinute, String tsarNotifyType, String time12_24) {
        this.userPrefId = userPrefId;
        this.userId = userId;
        this.tzInternalName = tzInternalName;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
        this.tsarMon = tsarMon;
        this.tsarTue = tsarTue;
        this.tsarWed = tsarWed;
        this.tsarThu = tsarThu;
        this.tsarFri = tsarFri;
        this.tsarSat = tsarSat;
        this.tsarSun = tsarSun;
        this.tsarHour = tsarHour;
        this.tsarMinute = tsarMinute;
        this.tsarNotifyType = tsarNotifyType;
        this.time12_24 = time12_24;
    }

    public Long getUserPrefId() {
        return userPrefId;
    }

    public void setUserPrefId(Long userPrefId) {
        this.userPrefId = userPrefId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTzInternalName() {
        return tzInternalName;
    }

    public void setTzInternalName(String tzInternalName) {
        this.tzInternalName = tzInternalName;
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

    public String getTsarMon() {
        return tsarMon;
    }

    public void setTsarMon(String tsarMon) {
        this.tsarMon = tsarMon;
    }

    public String getTsarTue() {
        return tsarTue;
    }

    public void setTsarTue(String tsarTue) {
        this.tsarTue = tsarTue;
    }

    public String getTsarWed() {
        return tsarWed;
    }

    public void setTsarWed(String tsarWed) {
        this.tsarWed = tsarWed;
    }

    public String getTsarThu() {
        return tsarThu;
    }

    public void setTsarThu(String tsarThu) {
        this.tsarThu = tsarThu;
    }

    public String getTsarFri() {
        return tsarFri;
    }

    public void setTsarFri(String tsarFri) {
        this.tsarFri = tsarFri;
    }

    public String getTsarSat() {
        return tsarSat;
    }

    public void setTsarSat(String tsarSat) {
        this.tsarSat = tsarSat;
    }

    public String getTsarSun() {
        return tsarSun;
    }

    public void setTsarSun(String tsarSun) {
        this.tsarSun = tsarSun;
    }

    public Integer getTsarHour() {
        return tsarHour;
    }

    public void setTsarHour(Integer tsarHour) {
        this.tsarHour = tsarHour;
    }

    public Integer getTsarMinute() {
        return tsarMinute;
    }

    public void setTsarMinute(Integer tsarMinute) {
        this.tsarMinute = tsarMinute;
    }

    public String getTsarNotifyType() {
        return tsarNotifyType;
    }

    public void setTsarNotifyType(String tsarNotifyType) {
        this.tsarNotifyType = tsarNotifyType;
    }

    public String getTime12_24() {
        return time12_24;
    }

    public void setTime12_24(String time12_24) {
        this.time12_24 = time12_24;
    }
}
