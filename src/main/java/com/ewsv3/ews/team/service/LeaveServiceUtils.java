package com.ewsv3.ews.team.service;

public class LeaveServiceUtils {

    public static String sqlSelfLeaveData = """
            select
                lv.person_id,
                lv.leave_date,
                listagg(lv.absence_name,
                        ', ') within group(
                order by
                    lv.leave_date
                )                    absence_name,
                sum(lv.absence_days) absence_days,
                sum(lv.absence_hrs)  absence_hrs
            from
                sc_person_absences_t lv
            where
                    lv.person_id = :personId
                and lv.leave_date between :startDate and :endDate
            group by
                lv.person_id,
                lv.leave_date
            order by
                lv.person_id,
                lv.leave_date
                        """;

    public static String sqlLineManagerLeaveData = """
            select
                    lv.person_id,
                    lv.leave_date,
                    listagg(lv.absence_name,
                            ', ') within group(
                    order by
                        lv.leave_date
                    )                    absence_name,
                    sum(lv.absence_days) absence_days,
                    sum(lv.absence_hrs)  absence_hrs
                from
                    sc_person_absences_t lv
                where
                    lv.leave_date between  :startDate and :endDate
                    and exists (
                        select
                            'Y'
                        from
                            sc_person_manager sm,
                            sc_person_v       mgr
                        where
                                mgr.person_id = sm.manager_id
                            and mgr.user_id   = :userId
                            and mgr.person_id = lv.person_id
                    )
                group by
                    lv.person_id,
                    lv.leave_date
                order by
                    lv.person_id,
                    lv.leave_date""";

    public static String sqlTimekeeperLeaveData = """
            select
                lv.person_id,
                lv.leave_date,
                listagg(lv.absence_name,
                        ', ') within group(
                order by
                    lv.leave_date
                )                    absence_name,
                sum(lv.absence_days) absence_days,
                sum(lv.absence_hrs)  absence_hrs
            from
                sc_person_absences_t lv
            where
                lv.leave_date between :startDate and :endDate
                and exists (
                    select
                        'Y'
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.timekeeper_user_id = :userId
                        and tkv.profile_id = :profileId
                        and tkv.person_id  = lv.person_id
                )
            group by
                lv.person_id,
                lv.leave_date
            order by
                lv.person_id,
                lv.leave_date
                                    """;
}
