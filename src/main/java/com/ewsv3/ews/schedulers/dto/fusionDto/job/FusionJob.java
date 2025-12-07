package com.ewsv3.ews.schedulers.dto.fusionDto.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record FusionJob(

        @JsonProperty("JobId") long jobId,

        @JsonProperty("JobCode") String jobCode,

        @JsonProperty("JobFamilyId") Long jobFamilyId,

        @JsonProperty("ActiveStatus") String activeStatus,

        @JsonProperty("FullPartTime") String fullPartTime,

        @JsonProperty("JobFunctionCode") String jobFunctionCode,

        @JsonProperty("ManagerLevel") String managerLevel,

        @JsonProperty("MedicalCheckupRequired") String medicalCheckupRequired,

        @JsonProperty("StandardWorkingHours") Long standardWorkingHours,

        @JsonProperty("StandardWorkingFrequency") String standardWorkingFrequency,

        @JsonProperty("StandardAnnualWorkingDuration") Long standardAnnualWorkingDuration,

        @JsonProperty("AnnualWorkingDurationUnits") String annualWorkingDurationUnits,

        @JsonProperty("RegularTemporary") String regularTemporary,

        @JsonProperty("SetId") Long setId,

        @JsonProperty("EffectiveStartDate") LocalDate effectiveStartDate,

        @JsonProperty("EffectiveEndDate") LocalDate effectiveEndDate,

        @JsonProperty("Name") String name,

        @JsonProperty("ApprovalAuthority") String approvalAuthority,

        @JsonProperty("SchedulingGroup") String schedulingGroup,

        @JsonProperty("GradeLadderId") Long gradeLadderId,

        @JsonProperty("CreationDate") OffsetDateTime creationDate,

        @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

        @JsonIgnore String links) {
}
