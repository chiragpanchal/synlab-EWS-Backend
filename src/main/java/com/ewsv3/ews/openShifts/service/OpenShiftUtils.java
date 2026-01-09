package com.ewsv3.ews.openShifts.service;

public class OpenShiftUtils {

    public static String GetOpenShiftsByProfile= """
            SELECT
                open_shift_id,
                start_date,
                end_date,
                demand_template_id,
                created_by,
                created_on,
                usr.full_name last_updated_by,
                last_update_date,
                profile_id,
                recalled
              FROM
                sc_open_shifts_h h,
                sc_person_v      usr
             WHERE
                    h.profile_id = :profileId
                   AND ( ( h.start_date BETWEEN :startDate AND :endDate
                           OR h.end_date BETWEEN :startDate AND :endDate )
                           OR :startDate BETWEEN h.start_date AND h.end_date )
                   AND usr.user_id = h.last_updated_by
             ORDER BY
                start_date DESC""";

    public static String GetOpenShiftsByOpenShiftId= """
            SELECT
                h.open_shift_id,
                h.start_date,
                h.end_date,
                h.demand_template_id,
                h.created_by,
                h.created_on,
                h.last_updated_by,
                h.last_update_date,
                h.profile_id,
                h.recalled
              FROM
                sc_open_shifts_h h,
                sc_person_v      usr
             WHERE
                    h.open_shift_id = :openShiftId
                   AND usr.user_id = h.last_updated_by
             ORDER BY
                start_date DESC""";

    public static String GetOpenShiftLines= """
            SELECT
                l.open_shift_line_id,
                l.open_shift_id,
                l.demand_template_line_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.sun,
                l.mon,
                l.tue,
                l.wed,
                l.thu,
                l.fri,
                l.sat,
                l.created_by,
                l.created_on,
                l.last_updated_by,
                l.last_update_date
              FROM
                sc_open_shifts_l l,
                sc_person_v      usr
             WHERE
                    l.open_shift_id = :openShiftId
                   AND usr.user_id = l.last_updated_by
             ORDER BY
                open_shift_line_id""";

    public static String GetOpenShiftLinesFromId= """
            SELECT
                l.open_shift_line_id,
                l.open_shift_id,
                l.demand_template_line_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.sun,
                l.mon,
                l.tue,
                l.wed,
                l.thu,
                l.fri,
                l.sat,
                l.created_by,
                l.created_on,
                l.last_updated_by,
                l.last_update_date
              FROM
                sc_open_shifts_l l,
                sc_person_v      usr
             WHERE
                    l.open_shift_line_id = :openShiftLineId
                   AND usr.user_id = l.last_updated_by
             ORDER BY
                open_shift_line_id""";

    public static String GetOpenShiftDetails= """
            SELECT
                l.open_shift_line_id,
                l.open_shift_id,
                l.demand_template_line_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.effective_date,
                l.fte_requested,
                l.created_by,
                l.created_on,
                usr.full_name last_updated_by,
                l.last_update_date
              FROM
                sc_open_shifts_d l,
                sc_person_v      usr
             WHERE
                    l.open_shift_id = :openShiftId
                   AND usr.user_id = l.last_updated_by
             ORDER BY
                open_shift_line_id""";

    public static String CreateOpenShiftHeader= """
            INSERT INTO sc_open_shifts_h (
                open_shift_id,
                start_date,
                end_date,
                demand_template_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                profile_id,
                recalled
            ) VALUES (
                :openShiftId,
                :startDate,
                :endDate,
                :demandTemplateId,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate,
                :profileId,
                :recalled
            )""";

    public static String UpdateOpenShiftHeader= """
            UPDATE sc_open_shifts_h h
               SET
                h.recalled = 'Y',
                h.last_updated_by = :lastUpdatedBy,
                h.last_update_date = :lastUpdateDate
             WHERE
                open_shift_id = :openShiftId""";

    public static String CreateOpenShiftLine= """
            INSERT INTO sc_open_shifts_l (
                open_shift_id,
                demand_template_line_id,
                department_id,
                job_title_id,
                location_id,
                work_duration_id,
                sun,
                mon,
                tue,
                wed,
                thu,
                fri,
                sat,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :openShiftId,
                :demandTemplateLineId,
                :departmentId,
                :jobTitleId,
                :locationId,
                :workDurationId,
                :sun,
                :mon,
                :tue,
                :wed,
                :thu,
                :fri,
                :sat,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate
            )""";

    public static String CreateOpenShiftDetail= """
            INSERT INTO sc_open_shifts_d (
                open_shift_id,
                open_shift_line_id,
                department_id,
                job_title_id,
                location_id,
                work_duration_id,
                effective_date,
                fte_requested,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :openShiftId,
                :openShiftLineId,
                :departmentId,
                :jobTitleId,
                :locationId,
                :workDurationId,
                :effectiveDate,
                :fteRequested,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate
            )""";

    public static String UpdateOpenShiftDetail= """
            UPDATE sc_open_shifts_d l
               SET
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                location_id = :locationId,
                work_duration_id = :workDurationId,
                effective_date = :effectiveDate,
                fteRequested = :fteRequested
                last_updated_by = :lastUpdatedBy,
                last_update_date = :lastUpdateDate
             WHERE
                l.open_shift_line_id = :openShiftLineId""";

    public static String UpdateOpenShiftLine= """
            UPDATE sc_open_shifts_l l
               SET
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                location_id = :locationId,
                work_duration_id = :workDurationId,
                sun = :sun,
                mon = :mon,
                tue = :tue,
                wed = :wed,
                thu = :thu,
                fri = :fri,
                sat = :sat,
                last_updated_by = :lastUpdatedBy,
                last_update_date = :lastUpdateDate
             WHERE
                l.open_shift_line_id = :openShiftLineId""";

    public static String DeleteOpenShiftLine= """
            DELETE sc_open_shifts_l l
             WHERE
                l.open_shift_line_id = :openShiftLineId;""";

    public static String DeleteOpenShiftdetail= """
            DELETE sc_open_shifts_d l
             WHERE
                l.open_shift_line_id = :openShiftLineId;""";

    public static String getOpenShiftsCountsTimekeeper= """
            SELECT
                COUNT(distinct od.open_shift_detail_id) open_shift_counts
              FROM
                sc_open_shifts_h     oh,
                sc_open_shifts_d     ol,
                sc_open_shifts_d     od,
                sc_demand_template_h dh,
                sc_person_v          usr
             WHERE
                    oh.profile_id = :profileId
                   AND ol.open_shift_id      = oh.open_shift_id
                   AND od.open_shift_line_id = ol.open_shift_line_id
                   AND oh.demand_template_id = dh.demand_template_id (+)
                   AND ( ( oh.start_date BETWEEN :startDate AND :endDate
                    OR oh.end_date BETWEEN :startDate AND :endDate )
                    OR :startDate BETWEEN oh.start_date AND oh.end_date )
                   AND usr.user_id           = oh.created_by""";

    public static String getOpenShiftListTimekeeper= """
            SELECT
                oh.open_shift_id,
                oh.start_date,
                oh.end_date,
                dh.template_name,
                ol.open_shift_line_id,
                oh.created_on,
                usr.full_name created_By,
                sd.department_name,
                sj.job_title,
                loc.location_name,
                ol.department_id,
                ol.job_title_id,
                ol.location_id,
                ol.sun,
                ol.mon,
                ol.tue,
                ol.wed,
                ol.thu,
                ol.fri,
                ol.sat,
                swd.work_duration_name,
                swd.time_start,
                swd.time_end,
                (SELECT
                     decode(
                         COUNT(pos.person_open_shift_id),
                         0,
                         'N',
                         'Y'
                     ) is_applied
                   FROM
                     sc_person_open_shifts pos
                  WHERE pos.open_shift_line_id = ol.open_shift_line_id) is_applied
              FROM
                sc_open_shifts_h oh,
                sc_open_shifts_l ol,
                sc_demand_template_h dh,
                sc_person_v usr,
                sc_departments sd,
                sc_jobs sj,
                sc_work_locations loc,
                sc_work_duration swd
             WHERE
                    oh.profile_id = :profileId
                    and ol.open_shift_id= oh.open_shift_id
                    and oh.demand_template_id = dh.demand_template_id(+)
                    and sd.department_id=ol.department_id
                    and sj.job_title_id= ol.job_title_id
                    and loc.work_location_id=ol.location_id
                   AND ( ( oh.start_date BETWEEN :startDate AND :endDate
                    OR oh.end_date BETWEEN :startDate AND :endDate )
                    OR :startDate BETWEEN oh.start_date AND oh.end_date )
                    and swd.work_duration_id=ol.work_duration_id
                    and usr.user_id= oh.created_by\s
                    order by oh.created_on desc,open_shift_line_id""";

    public static String getOpenShiftSuggestionPerson= """
            SELECT
                person_id,
                person_name,
                employee_number,
                grade_name,
                sch_hrs,
                rate
              FROM
                (
                    SELECT
                        tkv.person_id,
                        tkv.person_name,
                        tkv.employee_number,
                        tkv.grade_name,
                        pj.per_hr_sal rate,
                        (
                            SELECT
                                SUM((spr.time_end - spr.time_start) * 24) hrs
                              FROM
                                sc_person_rosters spr,
                                sc_work_duration  swd
                             WHERE
                                    spr.person_id = tkv.person_id
                                   AND swd.work_duration_id = spr.work_duration_id
                                   AND swd.work_duration_code <> 'OFF'
                                   AND trunc(
                                    spr.effective_date
                                ) BETWEEN :startDate AND :endDate
                        ) sch_hrs
                      FROM
                        sc_timekeeper_person_v   tkv,
                        sc_person_preferred_jobs pj
                     WHERE
                            tkv.timekeeper_user_id = :userId
                           AND tkv.profile_id       = :profileId
                           AND tkv.department_id    = :departmentId
                           AND tkv.work_location_id = :locationId
                           AND pj.person_id         = tkv.person_id
                           AND pj.job_title_id      = :jobTitleId
                           AND nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                           AND nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                )
             ORDER BY
                nvl(
                    sch_hrs,
                    0
                ) ASC,
                nvl(rate,0),
                person_name ASC""";

    public static String getOpenShiftSuggestionPersonRosters= """
            SELECT
                spr.person_id,
                spr.effective_date,
                spr.time_start,
                spr.time_end,
                swd.work_duration_code
              FROM
                sc_person_rosters spr,
                sc_work_duration  swd
             WHERE
                spr.person_id IN (:personId)
                   AND spr.effective_date BETWEEN :startDate AND :endDate
                   AND swd.work_duration_id = spr.work_duration_id
             ORDER BY
                spr.person_id,
                spr.effective_date,
                spr.time_start""";

    public static String getOpenShiftSuggestionPersonLeaves= """
            SELECT
                lv.person_id,
                lv.absence_name,
                lv.leave_date,
                lv.absence_days,
                absence_hrs,
                start_time,
                end_time
              FROM
                sc_person_absences_t lv
             WHERE
                lv.person_id IN (:personId)
                   AND lv.leave_date BETWEEN :startDate AND :endDate
             ORDER BY
                lv.person_id,
                lv.leave_date""";

    public static String getOpenShiftSuggestionPersonHolidays= """
            SELECT
                ph.person_id,
                ph.holiday_name,
                ph.holiday_date
              FROM
                sc_person_holidays ph
             WHERE
                ph.person_id IN (:personId)
                   AND ph.holiday_date BETWEEN :startDate AND :endDate
             ORDER BY
                ph.person_id,
                ph.holiday_date""";

    public static String getEmployeeOpenShifts= """
            SELECT
                oh.open_shift_id,
                oh.start_date,
                oh.end_date,
                '-'           template_name,
                ol.open_shift_line_id,
                oh.created_on,
                mgr.full_name created_By,
                sd.department_name,
                sj.job_title,
                loc.location_name,
                ol.department_id,
                ol.job_title_id,
                ol.location_id,
                ol.sun,
                ol.mon,
                ol.tue,
                ol.wed,
                ol.thu,
                ol.fri,
                ol.sat,
                swd.work_duration_name,
                swd.time_start,
                swd.time_end,
                (SELECT
                     decode(
                         COUNT(pos.person_open_shift_id),
                         0,
                         'N',
                         'Y'
                     ) is_applied
                   FROM
                     sc_person_open_shifts pos
                  WHERE
                         pos.person_id = per.person_id
                        AND pos.open_shift_line_id = ol.open_shift_line_id) is_applied
              FROM
                sc_person_v              per,
                sc_person_preferred_jobs pj,
                sc_open_shifts_l         ol,
                sc_open_shifts_h         oh,
                sc_work_duration         swd,
                sc_person_v              mgr,
                sc_departments           sd,
                sc_jobs                  sj,
                sc_work_locations        loc
             WHERE
                    per.user_id = :userId
                   AND pj.person_id         = per.person_id
                   AND ol.department_id     = per.department_id
                   AND ol.location_id       = per.work_location_id
                   AND ol.job_title_id      = pj.job_title_id
                   AND oh.open_shift_id     = ol.open_shift_id
                   AND swd.work_duration_id = ol.work_duration_id
                   AND mgr.user_id          = oh.created_by
                   AND sd.department_id     = ol.department_id
                   AND sj.job_title_id      = ol.job_title_id
                   AND loc.work_location_id = ol.location_id
                   AND EXISTS (
                    SELECT
                        'Y'
                      FROM
                        sc_timekeeper_person_v tkv
                     WHERE
                            tkv.person_id = per.person_id
                           AND tkv.profile_id = oh.profile_id
                )
             ORDER BY
                oh.open_shift_id desc,
                ol.open_shift_line_id""";

    public static String getTotalApplicationCountsSQL= """
            SELECT
                pos.open_shift_line_id,
                COUNT(pos.sun) sun,
                COUNT(pos.mon) mon,
                COUNT(pos.tue) tue,
                COUNT(pos.wed) wed,
                COUNT(pos.thu) thu,
                COUNT(pos.fri) fri,
                COUNT(pos.sat) sat
              FROM
                sc_person_open_shifts pos
             WHERE
                pos.open_shift_line_id = :openShiftLineId
             GROUP BY
                pos.open_shift_line_id""";

    public static String getSelfApplicationsSQL= """
            SELECT
                pos.person_open_shift_id,
                pos.open_shift_line_id,
                pos.person_id,
                pos.sun,
                pos.mon,
                pos.tue,
                pos.wed,
                pos.thu,
                pos.fri,
                pos.sat,
                pos.created_on,
                created_by_user.full_name created_by_user_name,
                pos.last_update_date,
                updated_by_user.full_name updated_by_user_name
              FROM
                sc_person_open_shifts pos,
                sc_person_v           created_by_user,
                sc_person_v           updated_by_user
             WHERE
                    pos.person_id  IN (:personId)
                   AND pos.open_shift_line_id  = :openShiftLineId
                   AND created_by_user.user_id = pos.created_by
                   AND updated_by_user.user_id = pos.last_updated_by""";

}
