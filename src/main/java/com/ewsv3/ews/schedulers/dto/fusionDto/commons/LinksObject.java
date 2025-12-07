package com.ewsv3.ews.schedulers.dto.fusionDto.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record LinksObject(
                String rel,
                String href,
                String name,
                String kind,

                @JsonIgnore String properties) {
}
