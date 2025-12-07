package com.ewsv3.ews.schedulers.dto.fusionDto.worker.citizenship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionWorkerCitizenshipResponse(

                @JsonProperty("items") List<FusionWorkerCitizenships> items,

                @JsonProperty("count") long count,

                @JsonProperty("hasMore") String hasMore,

                @JsonProperty("limit") long limit,

                @JsonProperty("offset") long offset,

                @JsonIgnore String links

) {
}
