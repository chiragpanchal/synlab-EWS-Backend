package com.ewsv3.ews.schedulers.dto.fusionDto.worker.citizenship;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkerCitizenships(

                @JsonIgnore long personId,

                @JsonProperty("CitizenshipId") long citizenshipId,

                @JsonProperty("Citizenship") String citizenship,

                @JsonProperty("FromDate") LocalDate fromDate,

                @JsonProperty("ToDate") LocalDate toDate,

                @JsonProperty("CitizenshipStatus") String citizenshipStatus,

                @JsonProperty("CreatedBy") String createdBy,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonIgnore() @JsonProperty("LastUpdatedBy") String lastUpdatedBy,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonIgnore List<LinksObject> links) {
}
