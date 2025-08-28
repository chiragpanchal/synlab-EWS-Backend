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
                exp_type
            from
                sc_project_exp_types
            where
                project_id = :projectId
                order by EXP_TYPE_ID""";
}
