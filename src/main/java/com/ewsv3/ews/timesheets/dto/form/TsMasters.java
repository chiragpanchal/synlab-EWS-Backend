package com.ewsv3.ews.timesheets.dto.form;

import java.util.List;

public record TsMasters(
        List<PayCodeDto> payCodeDtoList,
        List<TsDepartmentDto> tsDepartmentDtoList,
        List<TsJobDto> tsJobDtoList,
        List<ProjectTaskDto> projectTaskDtoList,
        List<ExpTypeDto> expTypeDtoList
) {
}
