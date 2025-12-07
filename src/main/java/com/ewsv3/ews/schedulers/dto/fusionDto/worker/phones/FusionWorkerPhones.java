package com.ewsv3.ews.schedulers.dto.fusionDto.worker.phones;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkerPhones(

                @JsonIgnore long personId,

                @JsonProperty("PhoneId") long phoneId,

                @JsonProperty("PhoneType") String phoneType,

                @JsonProperty("LegislationCode") String legislationCode,

                @JsonProperty("CountryCodeNumber") String countryCodeNumber,

                @JsonProperty("AreaCode") String areaCode,

                @JsonProperty("PhoneNumber") String phoneNumber,

                @JsonProperty("Extension") String extension,

                @JsonProperty("FromDate") LocalDate fromDate,

                @JsonProperty("ToDate") LocalDate toDate,

                @JsonProperty("Validity") String validity,

                @JsonIgnore() @JsonProperty("CreatedBy") String createdBy,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonIgnore() @JsonProperty("LastUpdatedBy") String lastUpdatedBy,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonProperty("PrimaryFlag") String primaryFlag,

                @JsonIgnore List<LinksObject> links) {
}
