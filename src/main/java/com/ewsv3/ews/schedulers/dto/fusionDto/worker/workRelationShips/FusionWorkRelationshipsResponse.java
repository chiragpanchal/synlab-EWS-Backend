package com.ewsv3.ews.schedulers.dto.fusionDto.worker.workRelationShips;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.phones.FusionWorkerPhones;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionWorkRelationshipsResponse(
        @JsonProperty("items") List<FusionWorkRelationships> items,

        @JsonProperty("count") long count,

        @JsonProperty("hasMore") boolean hasMore,

        @JsonProperty("limit") long limit,

        @JsonProperty("offset") int offset,

        @JsonIgnore List<LinksObject> links
) {
}
