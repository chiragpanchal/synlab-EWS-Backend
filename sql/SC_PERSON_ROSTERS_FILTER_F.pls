create or replace FUNCTION "SC_PERSON_ROSTERS_FILTER_F" (
    p_person_id        number,
    p_person_roster_id number,
    p_start_date       date,
    p_end_date         date,
    p_filter_flag      varchar2 default 'ALL'
     --D '#PA:' '#UP:' '#P:' '#C:''#OC:' 'ALL';
) return varchar2 is
    l_counts number := 0;
begin

--All / No  Fileter
    if nvl(
        p_filter_flag,
        'Y'
    ) in ( 'ALL',
           'Y' ) then
--           raise_application_error(-20000,p_filter_flag );
        return 'Y';
    end if;

    if nvl(
          p_filter_flag,
          'Y'
       ) = 'Y' then
        return 'Y';
    end if;

    --Draft
    if nvl(
          p_filter_flag,
          'X'
       ) = 'D' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'DRAFT'
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and spr.person_id           = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    --Pending Approval
    if nvl(
          p_filter_flag,
          'X'
       ) = 'PA' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'SUBMIT'
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and spr.person_id           = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    --Unpublished
    if nvl(
          p_filter_flag,
          'X'
       ) = 'UP' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'APPROVED'
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and spr.person_id           = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    --Published
    if nvl(
          p_filter_flag,
          'X'
       ) = 'P' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'APPROVED'
            and nvl(
                spr.published,
                'N'
            ) = 'Y'
            and spr.person_id           = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    --Corrections
    if nvl(
          p_filter_flag,
          'X'
       ) = 'C' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.appr_status         = 'RMI'
            and nvl(
                spr.published,
                'N'
            ) = 'N'
            and spr.person_id           = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    --Oncall
    if nvl(
          p_filter_flag,
          'X'
       ) = 'ON' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.on_call is not null
            and spr.person_id = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

     --Emergency
    if nvl(
          p_filter_flag,
          'X'
       ) = 'EN' then
        select
            count(spr.person_roster_id)
        into l_counts
        from
            sc_person_rosters spr,
            sc_person_v       per
        where
                spr.person_roster_id = nvl(
                    p_person_roster_id,
                    spr.person_roster_id
                )
            and trunc(
                spr.effective_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and spr.emergency is not null
            and spr.person_id = p_person_id
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
            and not exists (
                select
                    'Y'
                from
                    sc_roster_swap rs
                where
                        rs.s_person_id = spr.person_id
                    and rs.s_roster_person_id = spr.person_roster_id
            );

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

      --Leave
    if nvl(
          p_filter_flag,
          'X'
       ) = 'LV' then
        select
            count(pa.absence_attendances_id)
        into l_counts
        from
            sc_person_absences_t pa
        where
            trunc(
                pa.leave_date
            ) between trunc(p_start_date) and trunc(p_end_date)
            and pa.person_id = p_person_id;

        if nvl(
              l_counts,
              0
           ) > 0 then
            return 'Y';
        else
            return 'N';
        end if;

    end if;

    return 'N';
end;


