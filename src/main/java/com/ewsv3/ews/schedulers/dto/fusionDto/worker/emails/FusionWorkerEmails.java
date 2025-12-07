package com.ewsv3.ews.schedulers.dto.fusionDto.worker.emails;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkerEmails(

                @JsonIgnore long personId,

                @JsonProperty("EmailAddressId") long emailAddressId,

                @JsonProperty("EmailType") String emailType,

                @JsonProperty("EmailAddress") String emailAddress,

                @JsonProperty("FromDate") LocalDate fromDate,

                @JsonProperty("ToDate") LocalDate toDate,

                @JsonProperty("PrimaryFlag") String primaryFlag,

                @JsonProperty("CreatedBy") String createdBy,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonIgnore() @JsonProperty("LastUpdatedBy") String lastUpdatedBy,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonIgnore List<LinksObject> links) {
}
