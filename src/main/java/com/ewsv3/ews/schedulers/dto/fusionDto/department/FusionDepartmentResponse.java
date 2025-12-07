package com.ewsv3.ews.schedulers.dto.fusionDto.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FusionDepartmentResponse(

                List<FusionDepartment> items,
                @JsonProperty("count") long count,

                @JsonProperty("hasMore") boolean hasMore,

                @JsonProperty("limit") long limit,

                @JsonProperty("offset") int offset,

                @JsonIgnore String links

) {
}
