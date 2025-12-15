package com.ewsv3.ews.punch.dto;

import java.util.List;

import com.ewsv3.ews.timesheets.dto.form.TsDepartmentDto;
import com.ewsv3.ews.timesheets.dto.form.TsJobDto;

public record ClockAttributes(
                List<TsDepartmentDto> departmentList,
                List<TsJobDto> jobList,
                List<TimeType> timeTypeList) {

}
