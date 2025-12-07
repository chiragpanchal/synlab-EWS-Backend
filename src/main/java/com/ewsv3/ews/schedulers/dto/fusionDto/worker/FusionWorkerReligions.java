package com.ewsv3.ews.schedulers.dto.fusionDto.worker;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkerReligions(

                @JsonIgnore long personId,

                @JsonProperty("ReligionId") long ReligionId,

                @JsonProperty("LegislationCode") String LegislationCode,

                @JsonProperty("PrimaryFlag") String PrimaryFlag,

                @JsonProperty("Religion") String Religion,

                @JsonProperty("CreationDate") OffsetDateTime CreationDate,

                @JsonProperty("LastUpdateDate") OffsetDateTime LastUpdateDate,

                @JsonIgnore List<LinksObject> links) {
}
