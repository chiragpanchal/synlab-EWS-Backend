package com.ewsv3.ews.schedulers.dto.fusionDto.worker;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkRelationships(

                @JsonIgnore Long personId,

                @JsonProperty("PeriodOfServiceId") long PeriodOfServiceId,

                @JsonProperty("LegislationCode") String LegislationCode,

                @JsonProperty("LegalEntityId") long LegalEntityId,

                @JsonProperty("LegalEmployerName") String LegalEmployerName,

                @JsonProperty("WorkerType") String WorkerType,

                @JsonProperty("PrimaryFlag") String PrimaryFlag,

                @JsonProperty("StartDate") LocalDate StartDate,

                @JsonProperty("LegalEmployerSeniorityDate") LocalDate LegalEmployerSeniorityDate,

                @JsonProperty("EnterpriseSeniorityDate") LocalDate EnterpriseSeniorityDate,

                @JsonProperty("OnMilitaryServiceFlag") String OnMilitaryServiceFlag,

                @JsonProperty("WorkerNumber") String WorkerNumber,

                @JsonProperty("ReadyToConvertFlag") String ReadyToConvertFlag,

                @JsonProperty("TerminationDate") LocalDate TerminationDate,

                @JsonProperty("NotificationDate") LocalDate NotificationDate,

                @JsonProperty("LastWorkingDate") LocalDate LastWorkingDate,

                @JsonProperty("RevokeUserAccess") String RevokeUserAccess,

                @JsonProperty("RecommendedForRehire") String RecommendedForRehire,

                @JsonProperty("RecommendationReason") String RecommendationReason,

                @JsonProperty("RecommendationAuthorizedByPersonId") Long RecommendationAuthorizedByPersonId,

                @JsonProperty("CreationDate") OffsetDateTime CreationDate,

                @JsonProperty("LastUpdateDate") OffsetDateTime LastUpdateDate,

                @JsonProperty("ProjectedTerminationDate") LocalDate ProjectedTerminationDate,

                @JsonIgnore List<LinksObject> links) {
}
