-- Validates a person's rosters for the period against the profile's schedule rules.
-- Returns '#'-separated error messages, or NULL when there are no violations.
--
-- Reads the person's rosters once (single BULK COLLECT) and evaluates every rule in memory,
-- instead of issuing ~6 queries against sc_person_rosters per rule.
--
-- Behaviour changes vs. the previous version:
--   * "Max continuous Shift day(s)" no longer overwrites the rest-day error; each check has its own message.
--   * Continuous runs are counted over consecutive calendar days (working = any non-OFF shift that day,
--     rest = OFF entry and no working shift), not over changes in work_duration_code.
--   * The result has no leading/trailing '#'.
--   * Hours in weekly/monthly messages are rounded to 2 decimals.
CREATE OR REPLACE FUNCTION sc_get_schedule_rule_error_f (
    p_profile_id NUMBER,
    p_person_id  NUMBER,
    p_start_date DATE,
    p_end_date   DATE
) RETURN VARCHAR2 IS

    CURSOR c_rosters (cp_start DATE, cp_end DATE) IS
        SELECT spr.effective_date,
               spr.time_start,
               spr.time_end,
               swd.break_mins,
               swd.work_duration_code
          FROM sc_person_rosters spr
          JOIN sc_work_duration  swd ON swd.work_duration_id = spr.work_duration_id
         WHERE spr.person_id = p_person_id
           AND spr.effective_date BETWEEN cp_start AND cp_end
           AND spr.on_call IS NULL
           AND spr.emergency IS NULL
         ORDER BY spr.effective_date, spr.time_start;

    TYPE t_rosters IS TABLE OF c_rosters%ROWTYPE;
    TYPE t_num_map IS TABLE OF NUMBER INDEX BY PLS_INTEGER;

    l_rows           t_rosters;
    l_is_work        t_num_map;   -- by row index:  1 = working (non-OFF) shift
    l_day_work       t_num_map;   -- by day offset: 1 = has a working shift
    l_day_off        t_num_map;   -- by day offset: 1 = has an OFF entry
    l_day_hrs        t_num_map;   -- by day offset: scheduled hours

    l_start          DATE := trunc(p_start_date);
    l_end            DATE := trunc(p_end_date);
    l_period_days    NUMBER;
    l_period_type    VARCHAR2(1) := 'X';
    l_period_hrs     NUMBER := 0;
    l_max_off_run    NUMBER := 0;
    l_max_work_run   NUMBER := 0;
    l_off_run        NUMBER := 0;
    l_work_run       NUMBER := 0;
    l_d              PLS_INTEGER;
    l_hrs            NUMBER;
    l_prev           PLS_INTEGER;
    l_next_txt       VARCHAR2(30);
    l_days           VARCHAR2(32767);

    l_shift_gap_list       VARCHAR2(32767);
    l_rest_day_string      VARCHAR2(32767);
    l_cont_rest_string     VARCHAR2(32767);
    l_cont_shift_string    VARCHAR2(32767);
    l_max_hrs_day_string   VARCHAR2(32767);
    l_min_hrs_week_string  VARCHAR2(32767);
    l_max_hrs_week_string  VARCHAR2(32767);
    l_min_hrs_month_string VARCHAR2(32767);
    l_max_hrs_month_string VARCHAR2(32767);
    l_return               VARCHAR2(32767);

    PROCEDURE append_part (p_part VARCHAR2) IS
    BEGIN
        IF p_part IS NOT NULL THEN
            IF l_return IS NULL THEN
                l_return := p_part;
            ELSE
                l_return := l_return || '#' || p_part;
            END IF;
        END IF;
    END append_part;

BEGIN
    l_period_days := l_end - l_start + 1;
    IF l_period_days = 7 THEN
        l_period_type := 'W';
    ELSIF to_number(to_char(l_start, 'dd')) = 1
          AND last_day(l_start) = add_months(l_start, 1) - 1 THEN
        l_period_type := 'M';
    END IF;

    OPEN c_rosters(l_start, l_end);
    FETCH c_rosters BULK COLLECT INTO l_rows;
    CLOSE c_rosters;

    -- Nothing scheduled: no rule can be violated.
    IF l_rows.COUNT = 0 THEN
        RETURN NULL;
    END IF;

    -- Aggregate per day once.
    FOR i IN 1 .. l_rows.COUNT LOOP
        l_d := trunc(l_rows(i).effective_date) - l_start;
        IF l_rows(i).work_duration_code <> 'OFF' THEN
            l_is_work(i)    := 1;
            l_day_work(l_d) := 1;
            l_hrs := (l_rows(i).time_end - l_rows(i).time_start) * 24
                     - nvl(l_rows(i).break_mins, 0) / 60;
            IF l_hrs IS NOT NULL THEN
                IF l_day_hrs.EXISTS(l_d) THEN
                    l_day_hrs(l_d) := l_day_hrs(l_d) + l_hrs;
                ELSE
                    l_day_hrs(l_d) := l_hrs;
                END IF;
                l_period_hrs := l_period_hrs + l_hrs;
            END IF;
        ELSIF l_rows(i).work_duration_code = 'OFF' THEN
            l_day_off(l_d) := 1;
        END IF;
    END LOOP;

    -- Longest runs of consecutive rest / working days.
    FOR d IN 0 .. l_period_days - 1 LOOP
        IF l_day_work.EXISTS(d) THEN
            l_work_run     := l_work_run + 1;
            l_max_work_run := greatest(l_max_work_run, l_work_run);
            l_off_run      := 0;
        ELSIF l_day_off.EXISTS(d) THEN
            l_off_run      := l_off_run + 1;
            l_max_off_run  := greatest(l_max_off_run, l_off_run);
            l_work_run     := 0;
        ELSE
            l_work_run := 0;
            l_off_run  := 0;
        END IF;
    END LOOP;

    FOR r IN (
        SELECT sr.max_hrs_per_day,
               sr.min_hrs_per_week,
               sr.max_hrs_per_week,
               sr.min_hrs_per_month,
               sr.max_hrs_per_month,
               sr.shift_gap,
               sr.min_rest_days_per_week,
               sr.max_cont_shift_days,
               sr.max_cont_rest_days
          FROM sc_schedule_rules sr
         WHERE sr.profile_id = p_profile_id
           AND (   l_start BETWEEN sr.valid_from AND nvl(sr.valid_to, l_start)
                OR l_end   BETWEEN sr.valid_from AND nvl(sr.valid_to, l_end))
    ) LOOP
        -- Shift gaps between consecutive working shifts
        l_prev := NULL;
        FOR i IN 1 .. l_rows.COUNT LOOP
            IF l_is_work.EXISTS(i) THEN
                IF l_prev IS NOT NULL
                   AND round((l_rows(i).time_start - l_rows(l_prev).time_end) * 24, 2) < r.shift_gap THEN
                    l_next_txt := to_char(l_rows(i).time_start, 'dd-Mon hh:mi am');
                    IF l_shift_gap_list IS NULL THEN
                        l_shift_gap_list := to_char(l_rows(l_prev).time_end, 'dd-Mon hh:mi am')
                                            || ' & ' || l_next_txt;
                    ELSIF instr(l_shift_gap_list, l_next_txt) = 0 THEN
                        l_shift_gap_list := l_shift_gap_list || '~'
                                            || to_char(l_rows(l_prev).time_end, 'dd-Mon hh:mi am')
                                            || ' & ' || l_next_txt;
                    END IF;
                END IF;
                l_prev := i;
            END IF;
        END LOOP;

        IF l_period_type = 'W' THEN
            -- Rest days
            IF 7 - l_day_work.COUNT < r.min_rest_days_per_week THEN
                l_rest_day_string := 'Required Rest day(s): ' || r.min_rest_days_per_week
                                     || ', but given only ' || (7 - l_day_work.COUNT);
            END IF;

            -- Continuous rest days
            IF l_max_off_run > r.max_cont_rest_days THEN
                l_cont_rest_string := 'Max continuous Rest day(s) should not be more than : '
                                      || r.max_cont_rest_days || ', but given ' || l_max_off_run;
            END IF;

            -- Continuous shift days
            IF l_max_work_run > r.max_cont_shift_days THEN
                l_cont_shift_string := 'Max continuous Shift day(s) should not be more than : '
                                       || r.max_cont_shift_days || ', but given ' || l_max_work_run;
            END IF;
        END IF;

        -- Max hours per day
        l_days := NULL;
        FOR d IN 0 .. l_period_days - 1 LOOP
            IF l_day_hrs.EXISTS(d) AND l_day_hrs(d) > r.max_hrs_per_day THEN
                IF l_days IS NULL THEN
                    l_days := to_char(l_start + d, 'dd-Mon');
                ELSE
                    l_days := l_days || ', ' || to_char(l_start + d, 'dd-Mon');
                END IF;
            END IF;
        END LOOP;

        IF l_days IS NOT NULL THEN
            l_max_hrs_day_string := 'Max schedule hours should not be more than : ' || r.max_hrs_per_day
                                    || ' hours per day, these days have more hours scheduled: ' || l_days;
        END IF;

        -- Weekly / monthly hours
        IF l_period_hrs > 0 THEN
            IF l_period_type = 'W' THEN
                IF l_period_hrs < r.min_hrs_per_week THEN
                    l_min_hrs_week_string := 'Minimum schedule hours should not be less than : ' || r.min_hrs_per_week
                                             || ' hours per week for a person , but only ' || round(l_period_hrs, 2)
                                             || ' hours scheduled';
                END IF;
                IF l_period_hrs > r.max_hrs_per_week THEN
                    l_max_hrs_week_string := 'Schedule hours should not be more than : ' || r.max_hrs_per_week
                                             || ' hours per week for a person , but ' || round(l_period_hrs, 2)
                                             || ' hours scheduled';
                END IF;
            ELSIF l_period_type = 'M' THEN
                IF l_period_hrs < r.min_hrs_per_month THEN
                    l_min_hrs_month_string := 'Minimum schedule hours should not be less than : ' || r.min_hrs_per_month
                                              || ' hours per month for a person , but only ' || round(l_period_hrs, 2)
                                              || ' hours scheduled';
                END IF;
                IF l_period_hrs > r.max_hrs_per_month THEN
                    l_max_hrs_month_string := 'Schedule hours should not be more than : ' || r.max_hrs_per_month
                                              || ' hours per month for a person , but ' || round(l_period_hrs, 2)
                                              || ' hours scheduled';
                END IF;
            END IF;
        END IF;
    END LOOP;

    IF l_shift_gap_list IS NOT NULL THEN
        append_part('Shift Gap Error for day(s):' || l_shift_gap_list);
    END IF;
    append_part(l_rest_day_string);
    append_part(l_cont_rest_string);
    append_part(l_cont_shift_string);
    append_part(l_max_hrs_day_string);
    append_part(l_min_hrs_week_string);
    append_part(l_max_hrs_week_string);
    append_part(l_min_hrs_month_string);
    append_part(l_max_hrs_month_string);

    RETURN substr(l_return, 1, 4000);
END sc_get_schedule_rule_error_f;
/
