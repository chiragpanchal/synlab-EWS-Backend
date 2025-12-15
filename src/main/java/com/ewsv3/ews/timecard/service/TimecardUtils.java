package com.ewsv3.ews.timecard.service;

public class TimecardUtils {

    static String TimecardLineSql = """
            SELECT
                st.timecard_id,
                st.person_roster_id,
                st.effective_date,
                st.job_title_id,
                sj.job_title,
                sd.department_id,
                sd.department_name,
                st.work_location_id,
                sl.location_name,
                st.on_call,
                st.emergency,
                null project,
                null task,
                st.sch_time_start,
                st.sch_time_end,
                st.sch_hrs,
                st.in_time,
                st.out_time,
                st.act_hrs,
                (lv.absence_name ||'-'|| ph.holiday_name) time_type,
                nvl(st.primary_row,'N') primary_row,
                st.violation_code,
                sw.work_duration_code,
                (
                        select
                            count(*)
                        from
                            sc_person_requests_appr req
                        where
                                req.person_id = st.person_id
                            and req.date_start = st.effective_date
                            and req.rejected is null
                    )    request_counts
            FROM
                sc_timecards      st,
                sc_departments    sd,
                sc_jobs           sj,
                sc_work_locations sl,
                sc_work_duration  sw,
                sc_person_absences_t lv,
                sc_person_holidays ph
            WHERE
                    st.person_id = :person_id
                AND effective_date BETWEEN :start_date AND :end_date
                AND sd.department_id (+) = st.COST_CENTER_ID
                AND sj.job_title_id (+) = st.job_title_id
                AND sl.work_location_id (+) = st.work_location_id
                AND nvl(st.primary_row,'N') = 'Y'
                AND sw.work_duration_id (+) = st.work_duration_id
                AND lv.person_id(+) = st.person_Id
                AND lv.leave_date(+) = st.effective_date
                AND ph.person_id(+) = st.person_Id
                AND ph.holiday_date(+) = st.effective_date
            ORDER BY
                st.effective_date, st.sch_time_start, st.in_time""";

    static String TimecardActualsSql = """
                SELECT
                st.timecard_id,
                st.person_roster_id,
                st.effective_date,
                st.in_time,
                st.out_time,
                --decode(st.act_hrs,0,null,st.out_time) out_time,
                st.act_hrs,
                st.time_type,
                st.absence_attendances_id,
                st.holiday_id
            FROM
                sc_timecards      st
            WHERE
                    st.person_id = :person_id
                AND nvl(st.person_roster_id,0)=nvl(:person_roster_id,nvl(st.person_roster_id,0))
                AND effective_date = :effective_date
                AND ( st.in_time IS NOT NULL
                      OR st.time_type IS NOT NULL )
                AND nvl(
                    st.primary_row,
                    'N'
                ) = 'N'
            ORDER BY
                st.effective_date,
                st.in_time,
                nvl(st.act_hrs,99)""";

    static String TimecardSummarySql = """
            SELECT
                SUM(sch_hrs)         tot_sch_hrs,
                SUM(act_hrs)         tot_act_hrs,
                SUM(violation_count) tot_violation_count,
                SUM(absence_hrs)     tot_absence_hrs,
                SUM(holiday_hrs)     tot_holiday_hrs
            FROM
                (
                    SELECT
                        st.person_id,
                        st.effective_date,
                        SUM(st.sch_hrs)          sch_hrs,
                        SUM(st.act_hrs)          act_hrs,
                        --COUNT(st.violation_code) violation_count,
                        decode(st.primary_row,'Y',COUNT(st.violation_code),0)violation_count,
                        (
                            SELECT
                                SUM(abt.absence_hrs)
                            FROM
                                sc_person_absences_t abt
                            WHERE
                                    abt.person_id = st.person_id
                                AND abt.leave_date = st.effective_date
                        )                        absence_hrs,
                        (
                            SELECT
                                SUM(st2.sch_hrs)
                            FROM
                                sc_person_holidays sh,
                                sc_timecards       st2
                            WHERE
                                    st2.person_id = st.person_id
                                AND st2.effective_date = st.effective_date
                                AND sh.person_id = st.person_id
                                AND sh.holiday_date = st.effective_date
                        )                        holiday_hrs
                    FROM
                        sc_timecards st,
                        sc_work_duration sw
                    WHERE
                            st.person_id = :person_id
                        AND effective_date BETWEEN :start_date AND :end_date
                        AND sw.work_duration_id (+) = st.work_duration_id
                        AND sw.work_duration_code (+) NOT IN ('OFF','HOL')
                    GROUP BY
                        st.person_id,
                        st.effective_date,
                        st.primary_row
                )""";

}
