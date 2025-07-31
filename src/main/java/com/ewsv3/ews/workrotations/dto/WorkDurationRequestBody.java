package com.ewsv3.ews.workrotations.dto;

public record WorkDurationRequestBody(
                Long workDurationId,
                String searchText,
                String activeOnly) {
}