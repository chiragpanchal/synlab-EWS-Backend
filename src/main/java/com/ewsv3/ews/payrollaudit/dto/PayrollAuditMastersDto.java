package com.ewsv3.ews.payrollaudit.dto;

import java.util.List;

public record PayrollAuditMastersDto(
        List<PayCodeDto> payCodes,
        List<DepartmentMasterDto> departments,
        List<JobTitleMasterDto> jobTitles,
        List<LocationMasterDto> locations,
        List<GradeMasterDto> grades
) {
}
