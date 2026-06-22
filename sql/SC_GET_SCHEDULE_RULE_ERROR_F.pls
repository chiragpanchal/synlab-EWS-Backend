create or replace FUNCTION "SC_GET_SCHEDULE_RULE_ERROR_F" (
    p_profile_id number,
    p_person_id  number,
    p_start_date date,
    p_end_date   date
) return varchar2 is

    l_period_days          number := 0;
    p_period_type          varchar2(10) := 'x';
    l_shift_gap_string     varchar2(4000);
    l_rest_day_string      varchar2(4000);
    l_max_hrs_day_string   varchar2(4000);
    l_max_hrs_week_string  varchar2(4000);
    l_min_hrs_week_string  varchar2(4000);
    l_max_hrs_month_string varchar2(4000);
    l_min_hrs_month_string varchar2(4000);
    l_return_string        varchar2(4000);
    --
    l_days                 varchar2(1000);
    l_rest_days            number := 0;
    l_sch_hrs              number := 0;
    l_budget_skill         varchar2(1000);
    l_skill_count          number := 0;
begin
    l_period_days := trunc(p_end_date) - trunc(p_start_date) + 1;
    if l_period_days = 7 then
        p_period_type := 'W';
    elsif
        to_number ( to_char(
            trunc(p_start_date),
            'dd'
        ) ) = 1
        and ( last_day(trunc(p_start_date)) = add_months(
                                                        trunc(p_start_date),
                                                        1
                                              ) - 1 )
    then
        p_period_type := 'M';
    end if;

--    if p_period_type = 'X' then
--        return null;
--    end if;
    for t in (
        select
            sr.max_hrs_per_day,
            sr.min_hrs_per_week,
            sr.max_hrs_per_week,
            sr.min_hrs_per_month,
            sr.max_hrs_per_month,
            sr.shift_gap,
            sr.min_rest_days_per_week,
            sr.max_cont_shift_days,
            sr.max_cont_rest_days
        from
            sc_schedule_rules sr
        where
                sr.profile_id = p_profile_id
            and ( trunc(p_start_date) between sr.valid_from and nvl(
                sr.valid_to,
                trunc(p_start_date)
            )
                  or trunc(p_end_date) between sr.valid_from and nvl(
                sr.valid_to,
                trunc(p_end_date)
            ) )
    ) loop
        -- for shifts gaps
        for gap in (
            select
                effective_date,
                time_start,
                time_end,
                next_time_start,
                round(
                    (next_time_start - time_end) * 24,
                    2
                ) time_diff_hrs
            from
                (
                    select
                        spr.effective_date,
                        spr.time_start,
                        spr.time_end,
                        lead(spr.time_start)
                        over(
                            order by
                                effective_date
                        ) as next_time_start
                    from
                        sc_person_rosters spr,
                        sc_work_duration  swd
                    where
                            spr.person_id = p_person_id
                        and spr.effective_date between trunc(p_start_date) and trunc(
                        p_end_date)
                        and swd.work_duration_id = spr.work_duration_id
                        and swd.work_duration_code <> 'OFF'
                    order by
                        spr.effective_date,
                        spr.time_start
                )
            where
                next_time_start is not null
                and round(
                    (next_time_start - time_end) * 24,
                    2
                ) < t.shift_gap
        ) loop
            if l_shift_gap_string is null then
                l_shift_gap_string := to_char(
                                             gap.time_end,
                                             'dd-Mon hh:mi am'
                                      )
                                      || ' & '
                                      || to_char(
                                                gap.next_time_start,
                                                'dd-Mon hh:mi am'
                                         );

            else
                if instr(
                        l_shift_gap_string,
                        to_char(
                               gap.next_time_start,
                               'dd-Mon hh:mi am'
                        )
                   ) = 0 then
                    l_shift_gap_string := l_shift_gap_string
                                          || '~'
                                          || to_char(
                                                    gap.time_end,
                                                    'dd-Mon hh:mi am'
                                             )
                                          || ' & '
                                          || to_char(
                                                    gap.next_time_start,
                                                    'dd-Mon hh:mi am'
                                             );

                end if;
            end if;
        end loop;

        if l_shift_gap_string is not null then
            l_shift_gap_string := 'Shift Gap Error for day(s):' || l_shift_gap_string
            ;
        end if;




        -- for rest days
        if p_period_type = 'W' then
            select
                7 - count(distinct spr.effective_date)
            into l_rest_days
            from
                sc_person_rosters spr,
                sc_work_duration  swd
            where
                    spr.person_id = p_person_id
                and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                )
                and swd.work_duration_id = spr.work_duration_id
                and swd.work_duration_code <> 'OFF';

            if nvl(
                  l_rest_days,
                  0
               ) < t.min_rest_days_per_week then
                l_rest_day_string := 'Required Rest day(s): '
                                     || t.min_rest_days_per_week
                                     || ', but given only '
                                     || nvl(
                                           l_rest_days,
                                           0
                                        );

            end if;

        end if;


        --for continuous rest days
        for off_d in (
            with data as (
                select
                    spr.effective_date,
                    swd.work_duration_code
                from
                         sc_person_rosters spr
                    join sc_work_duration swd on swd.work_duration_id = spr.work_duration_id
                where
                        spr.person_id = p_person_id
                    and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                    )
                    and swd.work_duration_code = 'OFF'
            ), mark as (
  -- mark where a new run starts (1 = start of a new run, 0 = continuation)
                select
                    d.*,
                    case
                        when lag(work_duration_code)
                             over(
                            order by
                                effective_date
                             ) = work_duration_code then
                            0
                        else
                            1
                    end as is_run_start
                from
                    data d
            ), grp as (
  -- create a group id by cumulatively summing the run-start markers
                select
                    m.*,
                    sum(is_run_start)
                    over(
                        order by
                            effective_date
                        rows between unbounded preceding and current row
                    ) as run_grp
                from
                    mark m
            )
            select
                effective_date,
                work_duration_code,
                count(*)
                over(partition by run_grp) as continuous_count
            from
                grp
            order by
                effective_date
        ) loop
            if nvl(
                  off_d.continuous_count,
                  0
               ) > t.max_cont_rest_days then
                l_rest_day_string := 'Max continuous Rest day(s) should not be more than : '
                                     || t.max_cont_rest_days
                                     || ', but given '
                                     || nvl(
                                           off_d.continuous_count,
                                           0
                                        );

            end if;
        end loop;

        --for continuous SHIFT days
        for off_d in (
            with data as (
                select
                    spr.effective_date,
                    swd.work_duration_code
                from
                         sc_person_rosters spr
                    join sc_work_duration swd on swd.work_duration_id = spr.work_duration_id
                where
                        spr.person_id = p_person_id
                    and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                    )
--                    and swd.work_duration_code <> 'OFF'
            ), mark as (
  -- mark where a new run starts (1 = start of a new run, 0 = continuation)
                select
                    d.*,
                    case
                        when lag(work_duration_code)
                             over(
                            order by
                                effective_date
                             ) = work_duration_code then
                            0
                        else
                            1
                    end as is_run_start
                from
                    data d
            ), grp as (
  -- create a group id by cumulatively summing the run-start markers
                select
                    m.*,
                    sum(is_run_start)
                    over(
                        order by
                            effective_date
                        rows between unbounded preceding and current row
                    ) as run_grp
                from
                    mark m
            )
            select
                effective_date,
                work_duration_code,
                count(*)
                over(partition by run_grp) as continuous_count
            from
                grp
            order by
                effective_date
        ) loop
            if nvl(
                  off_d.continuous_count,
                  0
               ) > t.max_cont_shift_days then
                l_rest_day_string := 'Max continuous Shift day(s) should not be more than : '
                                     || t.max_cont_shift_days
                                     || ', but given '
                                     || nvl(
                                           off_d.continuous_count,
                                           0
                                        );

            end if;
        end loop;

        --Max hrs per day
        l_days := null;
        for day_hrs in (
            select
                spr.effective_date,
                sum((spr.time_end - spr.time_start) * 24 -(nvl(
                    swd.break_mins,
                    0
                ) / 60)) sch_hrs
            from
                sc_person_rosters spr,
                sc_work_duration  swd
            where
                    spr.person_id = p_person_id
                and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                )
                and swd.work_duration_id = spr.work_duration_id
                and swd.work_duration_code <> 'OFF'
            group by
                spr.effective_date
        ) loop
            if day_hrs.sch_hrs > t.max_hrs_per_day then
                if l_days is null then
                    l_days := to_char(
                                     day_hrs.effective_date,
                                     'dd-Mon'
                              );
                else
                    l_days := l_days
                              || ', '
                              || to_char(
                                        day_hrs.effective_date,
                                        'dd-Mon'
                                 );
                end if;

            end if;
        end loop;

        if l_days is not null then
            l_max_hrs_day_string := 'Max schedule hours should not be more than : '
                                    || t.max_hrs_per_day
                                    || ' hours per day, these days have more hours scheduled: '
                                    || l_days;
        end if;

        l_days := null;
        if p_period_type = 'W' then

            --min sch hrs per week
            declare
                l_week_sch_hrs number := 0;
            begin
                select
                    sum((spr.time_end - spr.time_start) * 24 -(nvl(
                        swd.break_mins,
                        0
                    ) / 60)) sch_hrs
                into l_week_sch_hrs
                from
                    sc_person_rosters spr,
                    sc_work_duration  swd
                where
                        spr.person_id = p_person_id
                    and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                    )
                    and swd.work_duration_id = spr.work_duration_id
                    and swd.work_duration_code <> 'OFF';

                if nvl(
                      l_week_sch_hrs,
                      0
                   ) > 0 then
                    if nvl(
                          l_week_sch_hrs,
                          0
                       ) < t.min_hrs_per_week then
                        l_min_hrs_week_string := 'Minimum schedule hours should not be less than : '
                                                 || t.min_hrs_per_week
                                                 || ' hours per week for a person , but only '
                                                 || l_week_sch_hrs
                                                 || ' hours scheduled';
                    end if;

                    if nvl(
                          l_week_sch_hrs,
                          0
                       ) > t.max_hrs_per_week then
                        l_max_hrs_week_string := 'Schedule hours should not be more than : '
                                                 || t.max_hrs_per_week
                                                 || ' hours per week for a person , but '
                                                 || l_week_sch_hrs
                                                 || ' hours scheduled';

                    end if;

                end if;

            end;
        end if;

        if p_period_type = 'M' then
            declare
                l_monthly_sch_hrs number := 0;
            begin
                select
                    sum((spr.time_end - spr.time_start) * 24 -(nvl(
                        swd.break_mins,
                        0
                    ) / 60)) sch_hrs
                into l_monthly_sch_hrs
                from
                    sc_person_rosters spr,
                    sc_work_duration  swd
                where
                        spr.person_id = p_person_id
                    and spr.effective_date between trunc(p_start_date) and trunc(p_end_date
                    )
                    and swd.work_duration_id = spr.work_duration_id
                    and swd.work_duration_code <> 'OFF';

                if nvl(
                      l_monthly_sch_hrs,
                      0
                   ) > 0 then
                    if nvl(
                          l_monthly_sch_hrs,
                          0
                       ) < t.min_hrs_per_month then
                        l_min_hrs_month_string := 'Minimum schedule hours should not be less than : '
                                                  || t.min_hrs_per_week
                                                  || ' hours per month for a person , but only '
                                                  || l_monthly_sch_hrs
                                                  || ' hours scheduled';
                    end if;

                    if nvl(
                          l_monthly_sch_hrs,
                          0
                       ) > t.max_hrs_per_month then
                        l_max_hrs_month_string := 'Schedule hours should not be more than : '
                                                  || t.max_hrs_per_week
                                                  || ' hours per month for a person , but '
                                                  || l_monthly_sch_hrs
                                                  || ' hours scheduled';

                    end if;

                end if;

            end;
        end if;

--        l_max_hrs_week_string  varchar2(4000);
--    l_min_hrs_week_string  varchar2(4000);
--    l_max_hrs_month_string varchar2(4000);
--    l_min_hrs_month_string varchar2(4000);
    end loop;

    if l_shift_gap_string is not null then
        l_return_string := l_shift_gap_string;
    end if;
    if
        l_return_string is null
        and l_rest_day_string is not null
    then
        l_return_string := l_rest_day_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_rest_day_string;
    end if;

    if
        l_return_string is null
        and l_max_hrs_day_string is not null
    then
        l_return_string := l_max_hrs_day_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_max_hrs_day_string;
    end if;

    if
        l_return_string is null
        and l_min_hrs_week_string is not null
    then
        l_return_string := l_min_hrs_week_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_min_hrs_week_string;
    end if;

    if
        l_return_string is null
        and l_max_hrs_week_string is not null
    then
        l_return_string := l_max_hrs_week_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_max_hrs_week_string;
    end if;

    if
        l_return_string is null
        and l_min_hrs_month_string is not null
    then
        l_return_string := l_min_hrs_month_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_min_hrs_month_string;
    end if;

    if
        l_return_string is null
        and l_max_hrs_month_string is not null
    then
        l_return_string := l_max_hrs_month_string;
    else
        l_return_string := l_return_string
                           || '#'
                           || l_max_hrs_month_string;
    end if;

    for i in 1..6 loop
        l_return_string := replace(
                                  l_return_string,
                                  '##',
                                  '#'
                           );
    end loop;

    if l_return_string = '#' then
        return null;
    else
        return l_return_string;
    end if;
end;