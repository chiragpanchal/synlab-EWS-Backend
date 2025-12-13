package com.ewsv3.ews.timesheets.dto.form;

public record PayCodeDto(
                Long payCodeId,
                String payCode,
                String payCodeName,
                String allwHourCode,
                String considerInTotal) {
}
