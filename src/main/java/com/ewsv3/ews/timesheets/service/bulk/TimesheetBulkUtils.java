package com.ewsv3.ews.timesheets.service.bulk;

public class TimesheetBulkUtils {

    public static String sqlBulkPerson= """
            select
                person_id,
                person_name,
                employee_number,
                job_title,
                department_name,
                grade_name,
                person_user_id
            from
                (
                    select
                        tkv.person_id,
                        tkv.person_name,
                        tkv.employee_number,
                        tkv.job_title,
                        tkv.department_name,
                        tkv.grade_name,
                        tkv.person_user_id
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.timekeeper_user_id = :userId
                        and tkv.profile_id = :profileId
                        and nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                    order by
                        tkv.person_name,
                        tkv.employee_number
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";

    public static String sqlBulkDepartments= """
            select
                pc.person_id,
                sd.department_id,
                sd.department_name
            from
                sc_person_preferred_cc pc,
                sc_departments         sd
            where
                    sd.department_id = pc.cost_center_id
                and pc.person_id in (
                    select
                        q.person_id
                    from
                        (
                            select
                                tkv.person_id,
                                tkv.person_name,
                                tkv.employee_number,
                                tkv.job_title,
                                tkv.department_name,
                                tkv.grade_name,
                                tkv.person_user_id
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and nvl(
                                    tkv.hire_date,
                                    :startDate
                                ) <= :startDate
                                and nvl(
                                    tkv.termination_date,
                                    :endDate
                                ) >= :endDate
                            order by
                                tkv.person_name,
                                tkv.employee_number
                        ) q
                    offset :offset * :pageSize rows
                    fetch next :pageSize rows only
                )""";
    public static String sqlBulkJobs= """
            select distinct
                pj.person_id,
                sj.job_title_id,
                sj.job_title,
                pj.pay_code_id,
                pj.per_hr_sal
            from
                sc_person_preferred_jobs pj,
                sc_jobs                  sj
            where
                    sj.job_title_id = pj.job_title_id
                and pj.person_id in (
                    select
                        q.person_id
                    from
                        (
                            select
                                tkv.person_id,
                                tkv.person_name,
                                tkv.employee_number,
                                tkv.job_title,
                                tkv.department_name,
                                tkv.grade_name,
                                tkv.person_user_id
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and nvl(
                                    tkv.hire_date,
                                    :startDate
                                ) <= :startDate
                                and nvl(
                                    tkv.termination_date,
                                    :endDate
                                ) >= :endDate
                            order by
                                tkv.person_name,
                                tkv.employee_number
                        ) q
                    offset :offset * :pageSize rows
                    fetch next :pageSize rows only
                )""";

    public static String sqlBulkProjects= """
            select
                pra.person_id,
                pt.task_id,
                pt.task_name,
                sp.project_id,
                sp.project_number,
                sp.project_name,
                pra.start_date,
                nvl(
                    least(
                        pra.end_date,
                        pt.end_date,
                        sp.end_date
                    ),
                    trunc(sysdate) + 30
                ) end_date,
                pt.budget_hrs,
                pt.budget_cost,
                sc_get_balanced_cost(
                    pt.task_id,
                    'H'
                ) balanced_hrs,
                sc_get_balanced_cost(
                    pt.task_id,
                    'C'
                ) balanced_cost
            from
                sc_person_project_assoc pra,
                sc_project_tasks        pt,
                sc_projects             sp
            where
                    pt.task_id = pra.task_id
                and sp.project_id = pra.project_id
                and pra.person_id in (
                    select
                        q.person_id
                    from
                        (
                            select
                                tkv.person_id,
                                tkv.person_name,
                                tkv.employee_number,
                                tkv.job_title,
                                tkv.department_name,
                                tkv.grade_name,
                                tkv.person_user_id
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and nvl(
                                    tkv.hire_date,
                                    :startDate
                                ) <= :startDate
                                and nvl(
                                    tkv.termination_date,
                                    :endDate
                                ) >= :endDate
                            order by
                                tkv.person_name,
                                tkv.employee_number
                        ) q
                    offset :offset * :pageSize rows
                    fetch next :pageSize rows only
                )
            order by
                pra.person_id,
                sp.project_name,
                pt.task_name""";


    public static String sqlBulkExpTypes= """
            select distinct
                et.exp_type_id,
                et.exp_type,
                et.project_id
            from
                sc_project_exp_types    et,
                sc_person_project_assoc pra
            where
                    pra.project_id = et.project_id
                and pra.person_id in  (
                    select
                        q.person_id
                    from
                        (
                            select
                                tkv.person_id,
                                tkv.person_name,
                                tkv.employee_number,
                                tkv.job_title,
                                tkv.department_name,
                                tkv.grade_name,
                                tkv.person_user_id
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and nvl(
                                    tkv.hire_date,
                                    :startDate
                                ) <= :startDate
                                and nvl(
                                    tkv.termination_date,
                                    :endDate
                                ) >= :endDate
                            order by
                                tkv.person_name,
                                tkv.employee_number
                        ) q
                    offset :offset * :pageSize rows
                    fetch next :pageSize rows only
                )
            order by
                project_id,
                exp_type_id""";

    public static String sqlBulkPayCodes= """
            select
                pay_code_id,
                pay_code,
                pay_code_name,
                allw_hour_code
            from
                sc_pay_codes spc
            where
                spc.enabled = 'Y'
            order by
                decode(
                    allw_hour_code,
                    'H',
                    1,
                    2
                ),
                pay_code_id""";


    public static String sqlBulkTimesheetDetails= """
            select
                tts.tts_timesheet_id,
                tts.person_id,
                tts.effective_date,
                tts.time_start,
                tts.time_end,
                tts.pay_code_id,
                tts.cost_center_id department_id,
                tts.job_title_id,
                tts.project_id,
                tts.task_id,
                tts.exp_type_id,
                tts.reg_hrs,
                tts.item_key,
                tts.time_hour,
                tts.allw_value,
                tts.comments,
                tts.person_roster_id,
                v.user_id          person_user_id
            from
                sc_person_v       v,
                sc_tts_timesheets tts
            where
                    tts.person_id = v.person_id
                and trunc(
                    tts.effective_date
                ) between :startDate and :endDate
                and tts.person_id in  (
                    select
                        q.person_id
                    from
                        (
                            select
                                tkv.person_id,
                                tkv.person_name,
                                tkv.employee_number,
                                tkv.job_title,
                                tkv.department_name,
                                tkv.grade_name,
                                tkv.person_user_id
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and nvl(
                                    tkv.hire_date,
                                    :startDate
                                ) <= :startDate
                                and nvl(
                                    tkv.termination_date,
                                    :endDate
                                ) >= :endDate
                            order by
                                tkv.person_name,
                                tkv.employee_number
                        ) q
                    offset :offset * :pageSize rows
                    fetch next :pageSize rows only
                )
            order by
                v.person_id,
                tts.effective_date,
                tts.time_start,
                tts.time_end""";
}
