package com.ewsv3.ews.schedulers.dto.fusionDto.department;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record FusionDepartment(

                @JsonProperty("OrganizationId") long organizationId,

                @JsonProperty("Name") String name,

                @JsonProperty("EffectiveStartDate") LocalDate effectiveStartDate,

                @JsonProperty("EffectiveEndDate") LocalDate effectiveEndDate,

                @JsonProperty("ClassificationCode") String classificationCode,

                @JsonProperty("LocationId") long locationId,

                @JsonProperty("OrganizationCode") String organizationCode,

                @JsonProperty("Status") String status,

                @JsonIgnore @JsonProperty("Title") String title,

                List<LinksObject> links,

                @JsonIgnore @JsonProperty("InternalAddressLine") String internalAddressLine,

                @JsonIgnore @JsonProperty("OrgCode") String orgCode,

                @JsonIgnore @JsonProperty("CreationDate") String creationDate,

                @JsonIgnore @JsonProperty("LastUpdateDate") String lastUpdateDate

) {
}
