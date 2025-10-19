package com.ewsv3.ews.selfroster.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SelfRoster {
        private Long selfRosterId;
        private LocalDate fromDate;
        private LocalDate toDate;
        private Long personId;
        private Long departmentId;
        private Long jobTitleId;
        private Long workLocationId;
        private String comments;
        private String status;
        private Long itemKey;
        private Long createdBy;
        private LocalDateTime createdOn;
        private Long lastUpdatedBy;
        private LocalDateTime lastUpdateDate;
        private List<SelfRosterLine> selfRosterLine;

        public SelfRoster() {
        }

        public Long getSelfRosterId() {
                return selfRosterId;
        }

        public void setSelfRosterId(Long selfRosterId) {
                this.selfRosterId = selfRosterId;
        }

        public LocalDate getFromDate() {
                return fromDate;
        }

        public void setFromDate(LocalDate fromDate) {
                this.fromDate = fromDate;
        }

        public LocalDate getToDate() {
                return toDate;
        }

        public void setToDate(LocalDate toDate) {
                this.toDate = toDate;
        }

        public Long getPersonId() {
                return personId;
        }

        public void setPersonId(Long personId) {
                this.personId = personId;
        }

        public Long getDepartmentId() {
                return departmentId;
        }

        public void setDepartmentId(Long departmentId) {
                this.departmentId = departmentId;
        }

        public Long getJobTitleId() {
                return jobTitleId;
        }

        public void setJobTitleId(Long jobTitleId) {
                this.jobTitleId = jobTitleId;
        }

        public Long getWorkLocationId() {
                return workLocationId;
        }

        public void setWorkLocationId(Long workLocationId) {
                this.workLocationId = workLocationId;
        }

        public String getComments() {
                return comments;
        }

        public void setComments(String comments) {
                this.comments = comments;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public Long getItemKey() {
                return itemKey;
        }

        public void setItemKey(Long itemKey) {
                this.itemKey = itemKey;
        }

        public Long getCreatedBy() {
                return createdBy;
        }

        public void setCreatedBy(Long createdBy) {
                this.createdBy = createdBy;
        }

        public LocalDateTime getCreatedOn() {
                return createdOn;
        }

        public void setCreatedOn(LocalDateTime createdOn) {
                this.createdOn = createdOn;
        }

        public Long getLastUpdatedBy() {
                return lastUpdatedBy;
        }

        public void setLastUpdatedBy(Long lastUpdatedBy) {
                this.lastUpdatedBy = lastUpdatedBy;
        }

        public LocalDateTime getLastUpdateDate() {
                return lastUpdateDate;
        }

        public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
                this.lastUpdateDate = lastUpdateDate;
        }

        public List<SelfRosterLine> getSelfRosterLine() {
                return selfRosterLine;
        }

        public void setSelfRosterLine(List<SelfRosterLine> selfRosterLine) {
                this.selfRosterLine = selfRosterLine;
        }

}
