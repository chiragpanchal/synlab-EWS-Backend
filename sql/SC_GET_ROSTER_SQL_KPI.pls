create or replace PROCEDURE "SC_GET_ROSTER_SQL_KPI" (
    p_user_id    number,
    p_start_date date,
    p_end_date   date,
    p_profile_id number default null,
    p_text       varchar2 default '%',
    p_kpi_string out varchar2
) is

    l_draft_count     number := 0;
    l_submit_count    number := 0;
    l_unpub_count     number := 0;
    l_pub_count       number := 0;
    l_correct_count   number := 0;
    l_on_call_count   number := 0;
    l_emergency_count number := 0;
    l_leave_count number := 0;
    --
    l_search_string   varchar2(100);
begin
    l_search_string := lower(ltrim(rtrim(p_text)));
    p_kpi_string    := 'D:'
                    || l_draft_count
                    || '#PA:'
                    || l_submit_count
                    || '#UP:'
                    || l_unpub_count
                    || '#P:'
                    || l_pub_count
                    || '#C:'
                    || l_correct_count
                    || '#OC:'
                    || l_on_call_count
                    || '#EN:'
                    || l_emergency_count
                    || '#LV:'
                    || l_leave_count;


    begin
--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_draft_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'DRAFT'
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and per.person_id           = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_submit_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'SUBMIT'
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and per.person_id           = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_unpub_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'APPROVED'
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and per.person_id           = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_pub_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'APPROVED'
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and nvl(
                spr.published,
                'N'
            ) = 'Y'
            and per.person_id           = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_correct_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'RMI'
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and per.person_id           = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

--      SELECT SUM (REQ_HRS)
--        INTO L_OPEN_SHIFT_COUNT
--        FROM (SELECT DISTINCT
--                     OS.OPEN_SHIFT_REQ_ID,
--                     OS.EFFECTIVE_DATE,
--                     NVL (ROUND ( (time_end - time_start) * 24, 2), 0)
--                        REQ_HRS
--                FROM SC_OPEN_SHIFTS OS, sc_person_roster_pivot_t t
--               WHERE     t.login_user_id = p_user_id
--                     AND t.seq_id = p_seq_id
--                     AND OS.PERSON_ID = t.PERSON_ID
--                     AND TRUNC (OS.EFFECTIVE_DATE) BETWEEN TRUNC (
--                                                              p_start_date)
--                                                       AND TRUNC (p_end_date));

--        select
--            nvl(
--                round(
--                    sum((time_end - time_start) * 24),
--                    2
--                ),
--                0
--            )
        select
            count(spr.person_roster_id)
        into l_on_call_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.on_call is not null
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and per.person_id = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        select
            count(spr.person_roster_id)
        into l_emergency_count
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
            trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.emergency is not null
            and ( lower(
                per.employee_number
            ) like '%'
                   || l_search_string
                   || '%'
                  or lower(
                per.full_name
            ) like '%'
                   || l_search_string
                   || '%' )
            and per.person_id = spr.person_id
            and trunc(
                spr.effective_date
            ) between trunc(
                per.hire_date
            ) and nvl(
                per.termination_date,
                trunc(
                                spr.effective_date
                            )
            )
            and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = spr.person_id
            )
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        select
            count(pa.absence_attendances_id)
        into l_leave_count
        from
            sc_person_absences_t pa
        where
            trunc(
                pa.leave_date
            ) between trunc(p_start_date) and trunc(p_end_date)
             and exists (
                select
                    'Y'
                from
                    sc_timekeeper_person_v tkv
                where
                        tkv.timekeeper_user_id = p_user_id
                    and tkv.profile_id = p_profile_id
                    and tkv.person_id  = pa.person_id
            );

      --      SELECT SUM (REQ_HRS)
      --        INTO L_OPEN_SHIFT_COUNT
      --        FROM (SELECT DISTINCT
      --                     OS.OPEN_SHIFT_REQ_ID,
      --                     OS.EFFECTIVE_DATE,
      --                     NVL (ROUND ( (time_end - time_start) * 24, 2), 0)
      --                        REQ_HRS
      --                FROM SC_OPEN_SHIFTS OS, SC_TIMEKEEPER_PERSON_V TPV
      --               WHERE     TRUNC (OS.EFFECTIVE_DATE) BETWEEN TRUNC (
      --                                                              p_start_date)
      --                                                       AND TRUNC (p_end_date)
      --                     AND OS.PERSON_ID = TPV.PERSON_ID
      --                     AND p_end_date >= TPV.HIRE_DATE
      --                     AND (   TPV.TERMINATION_DATE IS NULL
      --                          OR p_start_date <= TPV.TERMINATION_DATE)
      --                     AND TPV.PROFILE_ID = p_profile_id
      --                     AND TPV.TIMEKEEPER_USER_ID = p_user_id);

        p_kpi_string := 'D:'
                        || l_draft_count
                        || '#PA:'
                        || l_submit_count
                        || '#UP:'
                        || l_unpub_count
                        || '#P:'
                        || l_pub_count
                        || '#C:'
                        || l_correct_count
                        || '#ON:'
                        || nvl(
                              l_on_call_count,
                              0
                           )
                        || '#EN:'
                        || nvl(
                              l_emergency_count,
                              0
                           )
                        || '#LV:'
                        || nvl(
                              l_leave_count,
                              0
                           );

    end;

end;

