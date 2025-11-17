package com.ewsv3.ews.reports.service.productivity;

public class ProductivityUtils {
    public static String timecardInfoSql = """
            select
                person_id,
                employee_number,
                full_name,
                job_title,
                department_name,
                effective_date,
                sum(sch_hrs)         sch_hrs,
                sum(punch_hrs)       punch_hrs,
                sum(violation_count) violation_count,
                sum(leave_count)     leave_count
            from
                (
                    select
                        per.person_id,
                        per.employee_number,
                        per.full_name,
                        per.job_title,
                        per.department_name,
                        st.effective_date,
                        st.sch_time_start,
                        st.sch_time_end,
                        decode(
                            st.work_duration_code,
                            'OFF',
                            0,
                            round(st.sch_hrs,2)
                        ) sch_hrs,
                        (
                            select
                                round(
                                    sum(st2.act_hrs),
                                    2
                                )
                            from
                                sc_timecards st2
                            where
                                    st2.person_id = st.person_id
                                and st2.effective_date = st.effective_date
                                and st2.absence_attendances_id is null
                                and st2.holiday_id is null
                        ) punch_hrs,
                        decode(
                            st.violation_code,
                            null,
                            0,
                            1
                        ) violation_count,
                        (
                            select
                                count(lv.absence_attendances_id)
                            from
                                sc_person_absences_t lv
                            where
                                    lv.person_id = st.person_id
                                and lv.leave_date = st.effective_date
                        ) leave_count
                    from
                        sc_timecards      st,
                        sc_person_v       per
                    where
                        st.effective_date between :startDate and :endDate
                        and per.person_id          = st.person_id
                        and st.primary_row         = 'Y'
                        and exists (
                            select
                                'Y'
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and tkv.person_id  = st.person_id
                        )
                )
            group by
                person_id,
                employee_number,
                full_name,
                job_title,
                department_name,
                effective_date
            order by
                full_name,
                person_id,
                effective_date
            """;

    public static String timesheetInfoSql = """
            select
                tts.person_id,
                tts.effective_date,
                spc.pay_code_name,
                round(
                    sum(tts.reg_hrs),
                    2
                ) reg_hrs
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                    spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and exists (
                            select
                                'Y'
                            from
                                sc_timekeeper_person_v tkv
                            where
                                    tkv.timekeeper_user_id = :userId
                                and tkv.profile_id = :profileId
                                and tkv.person_id  = tts.person_id
                        )
            group by
                tts.person_id,
                tts.effective_date,
                spc.pay_code_name
            order by
                tts.person_id,
                tts.effective_date,
                spc.pay_code_name
            """;
}
