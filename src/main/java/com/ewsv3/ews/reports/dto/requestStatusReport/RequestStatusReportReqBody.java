package com.ewsv3.ews.reports.dto.requestStatusReport;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public record RequestStatusReportReqBody (
        LocalDate startDate,
        LocalDate endDate,
        String requestName,
        String status
){
}
