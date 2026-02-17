package com.ewsv3.ews.schedulers.dto.fusionDto.worker.assignments;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record FusionAssignmentDff(
        @JsonProperty("AssignmentId")  Long assignmentId,
        @JsonProperty("EffectiveStartDate") LocalDate effectiveStartDate,
        @JsonProperty("EffectiveEndDate")   LocalDate effectiveEndDate,
        @JsonProperty("EffectiveSequence")  Long effectiveSequence,
        @JsonProperty("__FLEX_Context")  String flexContext,
        @JsonProperty("otlGroup")  String otlGroup,
        @JsonProperty("otlGroup_Display")  String otlGroupDisplay,
        @JsonIgnore @JsonProperty("EffectiveLatestChange") String EffectiveLatestChange,
        @JsonIgnore @JsonProperty("startDateForCrossCharge") String startDateForCrossCharge,
        @JsonIgnore @JsonProperty("endDateForCrossCharge") String endDateForCrossCharge,
        @JsonIgnore @JsonProperty("costCentreForCrossCharge") String costCentreForCrossCharge,
        @JsonIgnore @JsonProperty("contractType") String contractType,
        @JsonIgnore @JsonProperty("workingHoursEndDatee") String workingHoursEndDatee,
        @JsonIgnore @JsonProperty("scopeOfJob") String scopeOfJob,
        @JsonIgnore @JsonProperty("currentjobtitleBusinessTitle") String currentjobtitleBusinessTitle,
        @JsonIgnore @JsonProperty("careerlevelofassignment") String careerlevelofassignment,
        @JsonIgnore @JsonProperty("entitlementcategory") String entitlementcategory,
        @JsonIgnore @JsonProperty("percentageofpartTimeworkers") String percentageofpartTimeworkers,
        @JsonIgnore @JsonProperty("termsandconditions") String termsandconditions,
        @JsonIgnore @JsonProperty("legacyJobTitles") String legacyJobTitles,
        @JsonIgnore @JsonProperty("jobManagementLevel") String jobManagementLevel,
        @JsonIgnore @JsonProperty("autocalculateHourlyRate") String autocalculateHourlyRate,
        @JsonIgnore @JsonProperty("noTechnicalLodasTransmission") String noTechnicalLodasTransmission,
        @JsonIgnore @JsonProperty("employmentRelationship") String employmentRelationship,
        @JsonIgnore @JsonProperty("employmentRelationship_Display") String employmentRelationship_Display,
        @JsonIgnore @JsonProperty("reasonOfTemporaryAssignment") String reasonOfTemporaryAssignment,
        @JsonIgnore @JsonProperty("noticePeriod") String noticePeriod,
        @JsonIgnore @JsonProperty("noticePeriod_Display") String noticePeriod_Display,
        @JsonIgnore @JsonProperty("timeClocking") String timeClocking,
        @JsonIgnore @JsonProperty("timeClocking_Display") String timeClocking_Display,
        @JsonIgnore @JsonProperty("christmasBonusEntitlement") String christmasBonusEntitlement,
        @JsonIgnore @JsonProperty("vacationBonusEntitlement") String vacationBonusEntitlement,
        @JsonIgnore @JsonProperty("additionalInformation") String additionalInformation,
        @JsonIgnore @JsonProperty("annualBonusEntitelment") String annualBonusEntitelment,
        @JsonIgnore @JsonProperty("annualBonusEntitlementAmount") String annualBonusEntitlementAmount,
        @JsonIgnore @JsonProperty("additionalLeave") String additionalLeave,
        @JsonIgnore @JsonProperty("internshipEmployeesNameOfTheUn") String internshipEmployeesNameOfTheUn,
        @JsonIgnore @JsonProperty("accessAuthorization") String accessAuthorization,
        @JsonIgnore @JsonProperty("links") List<LinksObject> links

) {
}
