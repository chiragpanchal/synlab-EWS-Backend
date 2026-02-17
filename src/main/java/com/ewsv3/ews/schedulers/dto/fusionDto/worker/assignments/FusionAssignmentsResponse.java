package com.ewsv3.ews.schedulers.dto.fusionDto.worker.assignments;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionAssignmentsResponse(
        @JsonProperty("items") List<FusionAssignments> items,

        @JsonProperty("count") long count,

        @JsonProperty("hasMore") String hasMore,

        @JsonProperty("limit") long limit,

        @JsonProperty("offset") long offset,

        @JsonIgnore List<LinksObject> links
) {
}
