package com.ewsv3.ews.schedulers.dto.fusionDto.worker.worker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionWorkerResponse(

                @JsonProperty("items") List<FusionWorker> items,

                @JsonProperty("count") long count,

                @JsonProperty("hasMore") boolean hasMore,

                @JsonProperty("limit") long limit,

                @JsonProperty("offset") int offset,

                @JsonIgnore String links) {

}
