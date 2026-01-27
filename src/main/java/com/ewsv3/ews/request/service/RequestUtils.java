package com.ewsv3.ews.request.service;

public class RequestUtils {

    static String RequestsMasterSql = """
            SELECT
                srm.request_master_id,
                srm.request_name,
                srm.enabled,
                srm.punch_exception,
                srm.time_type,
                srm.req_code,
                srm.multiple_day_allowed,
                srr.request_reason_id,
                srr.reason,
                src.request_criteria_id,
                src.job_id,
                src.department_id,
                src.position_id,
                src.work_location_id,
                src.employee_type_id,
                src.business_unit_id,
                src.payroll_id,
                src.job_family,
                src.legal_entity_id,
                src.gender,
                src.nationality,
                src.religion,
                src.grade_id
              FROM
                sc_requests_master  srm,
                sc_request_reasons  srr,
                sc_request_criteria src
             WHERE
                    nvl(
                        srm.enabled,
                        'N'
                    ) = 'Y'
                   AND srr.request_master_id (+) = srm.request_master_id
                   AND nvl(
                    srr.enabled(+),
                    'N'
                )  = 'Y'
                   AND src.request_master_id(+) = srm.request_master_id
             ORDER BY
                srm.request_name,
                srr.reason""";

    static String RequestLinesSql = """
            SELECT
                spra.person_id,
                spra.person_request_id,
                srm.request_name,
                srr.reason,
                spra.date_start,
                spra.date_end,
                spra.time_start,
                spra.time_end,
                spra.item_key,
                spra.created_on,
                decode(si.completion_date, NULL, 'Awaiting Approval', 'Approved') status,
                (select listagg(mgr.full_name,', ') within group (order by mgr.full_name) from sc_notifications sn, sc_person_v mgr
                where sn.item_key= spra.item_key
                and sn.status='OPEN'
                and sn.ACTION_TYPE = 'Approval'
                and (mgr.user_id=sn.to_user_id
                or mgr.user_id= sn.more_info_user_id)) pending_with,
                spra.comments 
            FROM
                sc_person_requests_appr spra,
                sc_requests_master      srm,
                sc_items                si,
                sc_request_reasons      srr
            WHERE
                    spra.person_id = :personId
                AND srm.request_master_id = spra.request_master_id
                AND ((trunc(spra.date_start) between :startDate and :endDate
                OR trunc(spra.date_end) between :startDate and :endDate)
                OR :startDate between trunc(spra.date_start) and trunc(spra.date_end))
                AND si.item_key = spra.item_key
                AND srr.request_master_id (+) = srm.request_master_id
                AND srr.request_reason_id(+)= spra.request_reason_id
            ORDER BY
                spra.created_on DESC""";


    static String RequestsApprovalSqlxcxx = """
            select
                request_name,
                person_id,
                user_id,
                full_name,
                employee_number,
                date_start,
                date_end,
                time_start,
                time_end,
                start_date,
                person_request_id,
                item_key,
                reason,
                comments,
                listagg(to_char(
                    sch_time_start,
                    'hh:mi am'
                )
                        || ' - '
                        || to_char(
                    sch_time_end,
                    'hh:mi am'
                ),
                        ', ') within group(
                order by
                    sch_time_end
                ) schedules,
                listagg(punch_lines,
                        ', ') within group(
                order by
                    punch_lines
                ) punches,
                listagg(violation_code,
                        ', ') within group(
                order by
                    violation_code
                ) violation_code
            from
                (
                    select
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
                        spra.person_request_id,
                        spra.item_key,
                        srr.reason,
                        spra.comments,
                        sch_time_start,
                        sch_time_end,
                        (
                            select
                                listagg(to_char(
                                    in_time,
                                    'hh:mi am'
                                )
                                        || ' - '
                                        || to_char(
                                    out_time,
                                    'hh:mi am'
                                ),
                                        ', ') within group(
                                order by
                                    in_time
                                )
                            from
                                sc_timecards st2
                            where
                                    st2.person_id = per.person_id
                                and st2.person_roster_id = st.person_roster_id
                                and st2.absence_attendances_id is null
                                and st2.holiday_id is null
                                and st2.person_request_id is null
                        ) punch_lines,
                        st.violation_code
                    from
                        sc_person_requests_appr spra,
                        sc_requests_master      srm,
                        sc_items                si,
                        sc_person_v             login_user,
                        sc_person_v             per,
                        sc_request_reasons      srr,
                        sc_timecards            st
                    where
                            spra.person_id = per.person_id
                        and srm.request_master_id     = spra.request_master_id
                        and si.item_key               = spra.item_key
                        and si.completion_date is null
                        and login_user.user_id        = :userId
                        and srr.request_master_id (+) = spra.request_master_id
                        and st.person_id              = spra.person_id
                        and st.effective_date         = spra.date_start
                        and st.primary_row            = 'Y'
                        and ( si.selected_person_id = login_user.person_id
                              or si.created_by_person_id    = login_user.person_id )
                    order by
                        si.start_date desc,
                        per.full_name,
                        srm.request_name
                )
            group by
                request_name,
                person_id,
                user_id,
                full_name,
                employee_number,
                date_start,
                date_end,
                time_start,
                time_end,
                start_date,
                person_request_id,
                item_key,
                reason,
                comments
            order by
                start_date desc
            """;

    static String RequestsApprovalSql = """
                        select
                sn.item_key,
                wc.notification_id,
                sn.action_type,
                sn.from_action,
                sn.created_on,
                sn.status,
                sn.role_name,
                decode(
                    si.completion_date,
                    null,
                    per_to.user_id,
                    decode(
                        per_from.user_id,
                        per_to.user_id,
                        null,
                        per_to.user_id
                    )
                )                  user_id,
                decode(
                    si.completion_date,
                    null,
                    per_to.full_name,
                    decode(
                        per_from.user_id,
                        per_to.user_id,
                        null,
                        per_to.full_name
                    )
                )                  full_name,
                decode(
                    si.completion_date,
                    null,
                    per_to.email_address,
                    decode(
                        per_from.user_id,
                        per_to.user_id,
                        null,
                        per_to.email_address
                    )
                )                  email_address,
                per_from.full_name from_user,
                sl.meaning         action_taken,
                wc.comments
            from
                sc_notifications  sn,
                sc_notif_comments wc,
                sc_person_v       per_from,
                sc_person_v       per_to,
                sc_lookups        sl,
                sc_items          si
            where
                    wc.notification_id (+) = sn.notification_id
                and sn.item_key          = :itemKey
                and per_from.user_id (+) = wc.created_by
                and per_to.user_id       = sn.to_user_id
                and sl.lookup_type       = 'NOTIF_ACTIONS'
                and sl.lookup_code       = wc.action_taken
                and si.item_key          = sn.item_key
            order by
                wc.notif_comment_id""";

    static String DestinationRostersSql = """
            select
                per.person_id,
                per.employee_number,
                per.full_name,
                spr.time_start,
                spr.time_end,
                spr.person_roster_id
            from
                sc_person_rosters spr,
                sc_person_v       per
            where
                    spr.published = 'Y'
                and spr.on_call is null
                and spr.emergency is null
                and per.person_id = spr.person_id
                and exists (
                    select
                        'Y'
                    from
                        sc_person_rosters spr2
                    where
                            spr2.person_roster_id = :personRosterId
                        and spr2.department_id = spr.department_id
                        and spr2.job_title_id  = spr.job_title_id
                        and spr.effective_date between spr2.effective_date-2 and spr2.effective_date + 2
                )
                and not exists (
                    select
                        'Y'
                    from
                        sc_person_rosters spr2
                    where
                            spr2.person_id = spr.person_id
                        and spr2.person_roster_id = :personRosterId
                )
                and not exists (
                    select
                        'Y'
                    from
                        sc_person_rosters spr2
                    where
                           1=1-- spr2.person_id = spr.person_id
                        and spr2.effective_date   = spr.effective_date
                        -- and spr2.person_roster_id = :personRosterId
                        and spr2.person_id in (select spr0.person_id from sc_person_rosters spr0 where spr0.person_roster_id=:personRosterId)
                )
            order by
                per.full_name,
                spr.time_start""";

    static String RostersForDate = """
            select
                spr.person_roster_id,
                spr.time_start,
                spr.time_end
            from
                sc_person_rosters spr
            where
                    spr.person_id = :personId
                and spr.effective_date between :startDate and :endDate""";

    static String UserPendingNotifications = """
            select distinct
                request_name,
                reason,
                date_start,
                date_end,
                time_start,
                time_end,
                comments,
                person_id,
                full_name,
                employee_number,
                pending_since,
                item_key,
                notification_id,
                to_user_id,
                listagg(to_char(
                    sch_time_start,
                    'hh:mi am'
                )
                        || ' - '
                        || to_char(
                    sch_time_end,
                    'hh:mi am'
                ),
                        ', ') within group(
                order by
                    sch_time_end
                ) schedules,
                listagg(punch_lines,
                        ', ') within group(
                order by
                    punch_lines
                ) punches,
                listagg(violation_code,
                        ', ') within group(
                order by
                    violation_code
                ) violation_code,
                swap_details
            from
                (
                    select
                        srm.request_name,
                        srr.reason,
                        sapr.date_start,
                        sapr.date_end,
                        sapr.time_start,
                        sapr.time_end,
                        sapr.comments,
                        per.person_id,
                        per.full_name,
                        per.employee_number,
                        wn.created_on pending_since,
                        wn.item_key,
                        wn.notification_id,
                        wn.to_user_id,
                        st.person_roster_id,
                        sch_time_start,
                        sch_time_end,
                        (
                            select
                                listagg(to_char(
                                    in_time,
                                    'hh:mi am'
                                )
                                        || ' - '
                                        || to_char(
                                    out_time,
                                    'hh:mi am'
                                ),
                                        ', ') within group(
                                order by
                                    in_time
                                )
                            from
                                sc_timecards st2
                            where
                                    st2.person_id = per.person_id
                                and st2.person_roster_id = st.person_roster_id
                                and st2.absence_attendances_id is null
                                and st2.holiday_id is null
                                and st2.person_request_id is null
                        )             punch_lines,
                        st.violation_code,
                        sc_get_roster_name_f (
                            p_person_roster_id => sapr.d_person_roster_id
                        )swap_details
                    from
                        sc_notifications        wn,
                        sc_items                si,
                        sc_tasks                st,
                        sc_person_v             per,
                        sc_person_requests_appr sapr,
                        sc_requests_master      srm,
                        sc_request_reasons      srr,
                        sc_timecards            st
                    where
                            si.item_key = wn.item_key
                        and st.task_id                = si.task_id
                        and per.person_id             = si.selected_person_id
                        and si.completion_date is null
                        and st.task_code              = 'Request'
                        and sapr.item_key             = si.item_key
                        and srm.request_master_id     = sapr.request_master_id
                        and srr.request_master_id (+) = srm.request_master_id
                        and srr.request_reason_id (+) = sapr.request_reason_id
                        and st.person_id              = sapr.person_id
                        and st.effective_date         = sapr.date_start
                        and st.primary_row            = 'Y'
                        and not exists (
                            select 'Y' from SC_WORK_FLOW_ACTION_QUEUE_T t
                            where nvl(t.FROM_NOTIFICATION_ID,0) = wn.notification_id
                        )
                        and ( ( wn.more_info_user_id = :userId )
                              or ( wn.more_info_user_id is null
                                   and wn.to_user_id             = :userId ) )
                        and wn.status                 = 'OPEN'
                )
            group by
                request_name,
                reason,
                date_start,
                date_end,
                time_start,
                time_end,
                comments,
                person_id,
                full_name,
                employee_number,
                pending_since,
                item_key,
                notification_id,
                to_user_id,
                violation_code,
                swap_details
            order by
                pending_since desc""";
}
