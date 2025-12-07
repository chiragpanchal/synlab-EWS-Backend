package com.ewsv3.ews.schedulers.dto.fusionDto.worker.emails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionWorkerEmailResponse(

        @JsonProperty("items") List<FusionWorkerEmails> items,

        @JsonProperty("count") long count,

        @JsonProperty("hasMore") String hasMore,

        @JsonProperty("limit") long limit,

        @JsonProperty("offset") long offset,

        @JsonIgnore String links) {
}
