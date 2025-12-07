package com.ewsv3.ews.schedulers.dto.fusionDto.worker.names;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionWorkerNamesResponse(
        @JsonProperty("items") List<FusionWorkerNames> items,

        @JsonProperty("count") long count,

        @JsonProperty("hasMore") boolean hasMore,

        @JsonProperty("limit") long limit,

        @JsonProperty("offset") int offset,

        @JsonIgnore String links) {
}
