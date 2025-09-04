package com.ewsv3.ews.timesheets.service.form;

public class TimesheetFormUtils {

    public static String sqlPayCodeList= """
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

    public static String sqlDepartmentList= """
            select
                sd.department_id,
                sd.department_name
            from
                sc_person_preferred_cc pc,
                sc_departments         sd
            where
                    sd.department_id = pc.cost_center_id
                and pc.person_id = :personId""";

    public static String sqlJobList= """
            select
                sj.job_title_id,
                sj.job_title,
                pj.per_hr_sal,
                sc.currency_code,
                pj.pay_code_id
            from
                sc_person_preferred_jobs pj,
                sc_jobs                  sj,
                sc_currencies            sc
            where
                    sj.job_title_id = pj.job_title_id
                and pj.person_id = :personId
                and sc.currency_id=pj.currency_id""";

    public static String sqlProjectTaskList= """
            select
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
                sc_get_balanced_cost(pt.task_id,'H') balanced_hrs,
                sc_get_balanced_cost(pt.task_id,'C') balanced_cost
            from
                sc_person_project_assoc pra,
                sc_project_tasks        pt,
                sc_projects             sp
            where
                    pra.person_id = :personId
                and pt.task_id    = pra.task_id
                and sp.project_id = pra.project_id
                order by sp.project_name,pt.task_name""";

    public static String sqlExpenditureList= """
            select
                exp_type_id,
                exp_type,
                project_id
            from
                sc_project_exp_types et
            where
                exists (
                    select
                        'Y'
                    from
                        sc_person_project_assoc pra
                    where
                            pra.person_id = :personId
                        and pra.project_id = et.project_id
                )
            order by
                project_id,
                exp_type_id""";

    public static String sqlTimesheetDetails= """
            select
                tts.tts_timesheet_id,
                tts.person_Id,
                tts.effective_date,
                tts.time_start,
                tts.time_end,
                tts.pay_code_id,
                tts.cost_center_id department_Id,
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
                per.user_id person_user_id
            from
                sc_tts_timesheets tts,
                sc_person_v       per
            where
                    tts.person_id = :personId
                and per.person_id = tts.person_id
                and trunc(
                    tts.effective_date
                ) between :startDate and :endDate
            order by
                tts.effective_date,
                tts.time_start""";

    public static String sqlTsTimecardData= """
            select
                person_id,
                effective_date,
                listagg(distinct decode(
                    work_duration_code,
                    'OFF',
                    work_duration_code,
                    schedule_line
                ),
                                 ', ') within group(
                order by
                    sch_time_start
                ) schedule_line,
                listagg(distinct punch_line,
                                 ', ') within group(
                order by
                    in_time
                ) punch_line,
                listagg(distinct leave_line,
                                 ', ') within group(
                order by
                    leave_line
                ) leave_line,
                listagg(distinct holiday_line,
                                 ', ') within group(
                order by
                    holiday_line
                ) holiday_line,
                listagg(distinct violation_line,
                                 ', ') within group(
                order by
                    violation_line
                ) violation_line
            from
                (
                    select distinct
                        st.person_id,
                        st.effective_date,
                        st.sch_time_start,
                        st.in_time,
                        decode(
                            nvl(
                                st.sch_hrs,
                                0
                            ),
                            0,
                            null,
                            (sc_getshort_time_f(
                                st.sch_time_start
                            )
                             || '-'
                             || sc_getshort_time_f(
                                st.sch_time_end
                            )
                             || '#'
                             || round(
                                st.sch_hrs,
                                2
                            ))
                        )                 schedule_line,
                        decode(
                            nvl(
                                st.act_hrs,
                                0
                            ),
                            0,
                            null,
                            (sc_getshort_time_f(
                                st.in_time
                            )
                             || '-'
                             || sc_getshort_time_f(
                                st.out_time
                            )
                             || '#'
                             || round(
                                st.act_hrs,
                                2
                            ))
                        )                 punch_line,
                        decode(
                            nvl(
                                st.absence_attendances_id,
                                0
                            ),
                            0,
                            null,
                            st.time_type
                        )                 leave_line,
                        decode(
                            nvl(
                                st.holiday_id,
                                0
                            ),
                            0,
                            null,
                            st.time_type
                        )                 holiday_line,
                        st.violation_code violation_line,
                        st.work_duration_code
                    from
                        sc_timecards st
                    where
                            st.person_id = :personId
                        and st.effective_date between :startDate and :endDate
                    order by
                        st.effective_date
                )
            group by
                person_id,
                effective_date""";

    public static String sqlTimesheetAudit= """
            select
                log.effective_date,
                log.audit_date,
                mgr.full_name      audit_user,
                log.operation,
                log.delete_comments,
                log.reg_hrs,
                spc.pay_code_name,
                log.time_start,
                log.time_end,
                sd.department_name department,
                sj.job_title,
                prj.project_name,
                tsk.task_name,
                exp.exp_type       expenditure_type,
                log.comments       timesheet_comments,
                log.time_hour
            from
                sc_tts_timesheets_log log,
                sc_departments        sd,
                sc_jobs               sj,
                sc_projects           prj,
                sc_project_tasks      tsk,
                sc_project_exp_types  exp,
                sc_pay_codes          spc,
                sc_person_v           mgr
            where
                    log.person_id = :personId
                and log.effective_date between :startDate and :endDate
                and log.cost_center_id  = sd.department_id
                and log.job_title_id    = sj.job_title_id
                and prj.project_id (+)  = log.project_id
                and tsk.task_id (+)     = log.task_id
                and exp.exp_type_id (+) = log.exp_type_id
                and spc.pay_code_id     = log.pay_code_id
                and mgr.user_id         = log.audit_user_id
            order by
                effective_date,
                tts_timesheet_log_id desc""";
}
