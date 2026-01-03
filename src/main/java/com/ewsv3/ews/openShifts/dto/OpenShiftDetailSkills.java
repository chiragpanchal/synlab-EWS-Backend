package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDateTime;

public class OpenShiftDetailSkills {
    Long open_shifts_skill_id;
    Long open_shift_line_id;
    Long skill_id;
    Long created_by;
    LocalDateTime created_on;
    Long last_updated_by;
    LocalDateTime last_update_date;

    public OpenShiftDetailSkills(Long open_shifts_skill_id) {
        this.open_shifts_skill_id = open_shifts_skill_id;
    }


    public OpenShiftDetailSkills() {
    }

    public OpenShiftDetailSkills(Long open_shifts_skill_id, Long open_shift_line_id, Long skill_id, Long created_by, LocalDateTime created_on, Long last_updated_by, LocalDateTime last_update_date) {
        this.open_shifts_skill_id = open_shifts_skill_id;
        this.open_shift_line_id = open_shift_line_id;
        this.skill_id = skill_id;
        this.created_by = created_by;
        this.created_on = created_on;
        this.last_updated_by = last_updated_by;
        this.last_update_date = last_update_date;
    }
}
