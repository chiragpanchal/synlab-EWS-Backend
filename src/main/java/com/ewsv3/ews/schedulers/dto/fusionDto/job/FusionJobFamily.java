package com.ewsv3.ews.schedulers.dto.fusionDto.job;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionJobFamily(

                @JsonProperty("JobFamilyId") long jobFamilyId,

                @JsonProperty("EffectiveStartDate") LocalDate effectiveStartDate,

                @JsonProperty("EffectiveEndDate") LocalDate effectiveEndDate,

                @JsonProperty("JobFamilyName") String jobFamilyName,

                @JsonProperty("JobFamilyCode") String jobFamilyCode,

                @JsonProperty("ActionReasonId") Long actionReasonId,

                @JsonProperty("ActiveStatus") String activeStatus,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonIgnore @JsonProperty("links") List<LinksObject> links) {
}
