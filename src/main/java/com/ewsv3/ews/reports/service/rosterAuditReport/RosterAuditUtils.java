package com.ewsv3.ews.reports.service.rosterAuditReport;

public class RosterAuditUtils {

    public static String rosterAuditSQL= """
            SELECT
                per.employee_number,
                per.full_name,
                per.department_name,
                per.job_title,
                per.grade_name,
                per.location_name,
                per.legal_entity,
                v.effective_date,
                v.audit_flag,
                v.audit_time,
                v.audit_user,
                sc_get_roster_audit_changed_values_f(
                    v.person_rosters_audit_id
                )                    changed_values,
                sc_get_roster_audit_delete_logs_f(
                    v.person_rosters_audit_id
                )                    delete_logs,
                ( to_char(
                    v.time_start_o,
                    'hh24:mi'
                )
                  || '-'
                  || to_char(
                    v.time_end_o,
                    'hh24:mi'
                ) )                  old_shift,
                ( to_char(
                    v.time_start,
                    'hh24:mi'
                )
                  || '-'
                  || to_char(
                    v.time_end,
                    'hh24:mi'
                ) )                  new_shift,
                sd_o.department_name old_department_name,
                sd_n.department_name new_department_name,
                sj_o.job_title       old_job_title,
                sj_n.job_title       new_job_title,
                swl_o.location_name  old_location_name,
                swl_n.location_name  new_location_name,
                v.delete_reason,
                v.delete_comments
              FROM
                sc_roster_audit_v      v,
                sc_person_v            per,
                sc_timekeeper_person_v tkp,
                sc_departments         sd_o,
                sc_departments         sd_n,
                sc_jobs                sj_o,
                sc_jobs                sj_n,
                sc_work_locations      swl_o,
                sc_work_locations      swl_n,
                sc_person_v            mgr_o,
                sc_person_v            mgr_n
             WHERE
                    per.person_id = v.person_id
                   AND tkp.timekeeper_user_id     = :userId
                   AND tkp.person_id              = per.person_id
                   AND v.effective_date BETWEEN :startDate AND :endDate
                   AND sd_o.department_id (+)     = v.department_id_o
                   AND sd_n.department_id (+)     = v.department_id
                   AND sj_o.job_title_id (+)      = v.job_title_id_o
                   AND sj_n.job_title_id (+)      = v.job_title_id
                   AND swl_o.work_location_id (+) = v.work_location_id_o
                   AND swl_n.work_location_id (+) = v.work_location_id
                   AND mgr_o.person_id (+)        = v.manager_id_o
                   AND mgr_n.person_id (+)        = v.manager_id
                   AND (lower(per.employee_number) like lower('%'||:str||'%')
                   or  lower(per.full_name) like lower('%'||:str||'%'))
                   AND ( :departmentIds IS NULL
                    OR per.department_id IN ( :departmentIds ))
                   AND ( :jobTitleIds IS NULL
                    OR per.job_title_id IN ( :jobTitleIds ))
                   AND ( :workLocationIds IS NULL
                    OR per.work_location_id IN ( :workLocationIds ))
             ORDER BY
                per.full_name,
                v.effective_date,
                v.audit_time
             OFFSET :offset * :pageSize ROWS FETCH NEXT :pageSize ROWS ONLY""";
}
