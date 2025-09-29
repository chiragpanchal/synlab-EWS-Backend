package com.ewsv3.ews.dashboard.service;

public class DashboardUtils {

    static String RequestPendingSql = """
            SELECT
                srm.request_name requestType,
                COUNT(spra.person_request_id) counts
            FROM
                sc_person_requests_appr spra,
                sc_requests_master      srm,
                sc_items                si
            WHERE
                    spra.person_id = :personId
                AND srm.request_master_id = spra.request_master_id
                AND si.item_key = spra.item_key
                AND si.completion_date IS NULL
            GROUP BY
                srm.request_name
            ORDER BY
                srm.request_name""";

    static String SelfRosterPendingSql = """
            SELECT
                COUNT(self_roster_id) counts
            FROM
                sc_self_roster ssr,
                sc_items       si
            WHERE
                    ssr.person_id = :personId
                AND si.item_key = ssr.item_key
                AND si.completion_date IS NULL""";

    static String TimesheetPendingSql = """
            SELECT
              COUNT(DISTINCT tts.effective_date) counts
            FROM
                sc_tts_timesheets tts,
                sc_items          si
            WHERE
                    tts.person_id = :personId
                AND si.item_key = tts.item_key
                AND si.completion_date IS NULL""";

    static String ViolationCountsSql = """
            select * from (
            SELECT
                tkv.person_name,
                tkv.employee_number,
                tkv.email_address,
                tkv.person_id,
                tkv.job_title,
                tkv.department_name,
                COUNT(st.timecard_id) violation_counts,
                0 curr_violation_counts,
                0 prev_violation_counts
            FROM
                sc_timekeeper_person_v tkv,
                sc_timecards           st
            WHERE
                    tkv.timekeeper_user_id = :userId
                AND st.person_id = tkv.person_id
                AND st.violation_code IS NOT NULL
                AND trunc(st.effective_date) BETWEEN :startDate AND :endDate
            GROUP BY
                tkv.person_name,
                tkv.employee_number,
                tkv.email_address,
                tkv.person_id,
                tkv.job_title,
                tkv.department_name
            ORDER BY
                tkv.person_name,
                tkv.employee_number)
                """;

    static String PendingTeamRequestsSql = """
            SELECT
                srm.request_name,
                per.person_id,
                per.user_id,
                per.full_name,
                per.employee_number,
                spra.date_start,
                spra.date_end,
                spra.time_start,
                spra.time_end,
                si.start_date,
                spra.person_request_id
              FROM
                sc_person_requests_appr spra,
                sc_requests_master      srm,
                sc_items                si,
                sc_person_v             login_user,
                sc_person_v             per
             WHERE
                    spra.person_id = per.person_id
                   AND srm.request_master_id  = spra.request_master_id
                   AND si.item_key            = spra.item_key
                   AND si.completion_date IS NULL
                   AND login_user.user_id     = :userId
                   AND ( si.selected_person_id = login_user.person_id
                    OR si.created_by_person_id = login_user.person_id )
             ORDER BY
                si.start_date DESC,
                per.full_name,
                srm.request_name""";

    static String AwaitingActionsSql = """
            SELECT
                st.task_name,
                per.person_id,
                per.full_name,
                per.employee_number,
                MIN(si.start_date)                    pending_since,
                COUNT(DISTINCT si.selected_person_id) person_counts,
                COUNT(si.item_key)                    notification_counts
              FROM
                sc_notifications wn,
                sc_items         si,
                sc_tasks         st,
                sc_person_v      per
             WHERE
                    si.item_key = wn.item_key
                   AND st.task_id                               = si.task_id
                   AND per.person_id                            = si.selected_person_id
                   AND si.completion_date IS NULL
                   and ( ( wn.more_info_user_id = :userId )
                                     or ( wn.more_info_user_id is null
                                          and wn.to_user_id = :userId ) )
                   AND wn.status                                = 'OPEN'
             GROUP BY
                st.task_name,
                per.person_id,
                per.full_name,
                per.employee_number
             ORDER BY
                per.full_name,
                st.task_name""";

    static String AwaitingActionsSummarySql = """
            select
                st.task_name,
                min(si.start_date)                    pending_since,
                count(distinct si.selected_person_id) person_counts,
                count(si.item_key)                    notification_counts
            from
                sc_notifications wn,
                sc_items         si,
                sc_tasks         st
            where
                    si.item_key = wn.item_key
                and st.task_id    = si.task_id
                and si.completion_date is null
                and ( ( wn.more_info_user_id = :userId )
                    or ( wn.more_info_user_id is null
                        and wn.to_user_id = :userId ) )
                and wn.status     = 'OPEN'
            group by
                st.task_name
            order by
                st.task_name
            """;
}
