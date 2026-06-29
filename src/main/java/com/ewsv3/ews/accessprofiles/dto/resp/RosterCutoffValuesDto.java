package com.ewsv3.ews.accessprofiles.dto.resp;

public record RosterCutoffValuesDto(
        Long valueSetValueId,
        String valueMeaning,
        String enabled
) {
}
