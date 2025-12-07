package com.ewsv3.ews.schedulers.dto.fusionDto.businessUnits;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FusionBusinessUnitResponse(

                List<FusionBusinessUnit> items,

                @JsonProperty("count") long count,

                @JsonProperty("hasMore") String hasMore,

                @JsonProperty("limit") long limit,

                @JsonProperty("offset") long offset,

                @JsonIgnore String links) {
}
