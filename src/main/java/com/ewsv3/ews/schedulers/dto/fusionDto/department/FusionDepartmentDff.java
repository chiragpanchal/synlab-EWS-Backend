package com.ewsv3.ews.schedulers.dto.fusionDto.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record FusionDepartmentDff(

                @JsonProperty("OrganizationId") long organizationId,

                @JsonProperty("mainDepartment") String attribute1,

                @JsonProperty("parentDepartment") String attribute2,

                @JsonProperty("sectionsParentDepartmentMane") String attribute3,
                String attribute4,
                String attribute5,

                @JsonIgnore LocalDate EffectiveStartDate,

                @JsonIgnore LocalDate EffectiveEndDate,

                @JsonIgnore String __FLEX_Context,

                @JsonIgnore String links

) {
}
