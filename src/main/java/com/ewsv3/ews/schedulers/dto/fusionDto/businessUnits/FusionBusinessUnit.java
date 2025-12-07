package com.ewsv3.ews.schedulers.dto.fusionDto.businessUnits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record FusionBusinessUnit(
        long BusinessUnitId,
        String Name,
        String Status,
        @JsonIgnore String links) {

}
