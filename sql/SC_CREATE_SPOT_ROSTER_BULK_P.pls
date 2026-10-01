CREATE OR REPLACE
PROCEDURE "SC_CREATE_SPOT_ROSTER_BULK_P" (
    p_creator_user_id number,
    p_entries         clob,
    p_results         out clob
) is
/*
    Bulk wrapper around SC_CREATE_SPOT_ROSTER_P (used by the roster Excel import).

    Creating rosters one HTTP request at a time costs several database round-trips per entry,
    which dominates when the application server is far from the database. This procedure takes a
    whole batch in one call and runs the existing single-entry procedure for each row inside the
    database, so the business logic stays in SC_CREATE_SPOT_ROSTER_P.

    p_entries: JSON array, one object per entry:
        [{ "ref": 0, "startDate": "2026-10-05", "endDate": "2026-10-05",
           "personId": 1, "assignmentId": 1, "jobTitleId": null, "departmentId": null,
           "workLocationId": null, "workDurationId": 10, "onCall": null, "emergency": null,
           "sun": "N", "mon": "mon", "tue": "N", "wed": "N", "thu": "N", "fri": "N", "sat": "N",
           "personRosterId": null }, ...]

    p_results: JSON array, one object per entry, in input order:
        [{ "ref": 0, "out": "S#1" }, { "ref": 1, "out": "E#<error message>" }, ...]
        "out" uses the same "S#<count>" / "E#<message>" format as SC_CREATE_SPOT_ROSTER_P.

    Each entry runs under its own savepoint: if an entry raises an unexpected error, only that
    entry's changes are undone and the rest of the batch continues (matching the previous
    behaviour, where every entry was a separate request). The caller commits the batch.
*/
    l_out     varchar2(4000);
    l_results json_array_t := json_array_t();
    l_result  json_object_t;
begin
    for r in (
        select *
        from
            json_table ( p_entries, '$[*]'
                columns (
                    ref              number         path '$.ref',
                    start_date       varchar2(10)   path '$.startDate',
                    end_date         varchar2(10)   path '$.endDate',
                    person_id        number         path '$.personId',
                    assignment_id    number         path '$.assignmentId',
                    job_title_id     number         path '$.jobTitleId',
                    department_id    number         path '$.departmentId',
                    work_location_id number         path '$.workLocationId',
                    work_duration_id number         path '$.workDurationId',
                    on_call          varchar2(100)  path '$.onCall',
                    emergency        varchar2(100)  path '$.emergency',
                    sun              varchar2(10)   path '$.sun',
                    mon              varchar2(10)   path '$.mon',
                    tue              varchar2(10)   path '$.tue',
                    wed              varchar2(10)   path '$.wed',
                    thu              varchar2(10)   path '$.thu',
                    fri              varchar2(10)   path '$.fri',
                    sat              varchar2(10)   path '$.sat',
                    person_roster_id number         path '$.personRosterId'
                )
            )
    ) loop
        savepoint sc_spot_bulk_entry;
        begin
            l_out := null;
            sc_create_spot_roster_p(
                p_creator_user_id  => p_creator_user_id,
                p_start_date       => to_date(r.start_date, 'YYYY-MM-DD'),
                p_end_date         => to_date(r.end_date, 'YYYY-MM-DD'),
                p_person_id        => r.person_id,
                p_assignment_id    => r.assignment_id,
                p_job_title_id     => r.job_title_id,
                p_department_id    => r.department_id,
                p_work_location_id => r.work_location_id,
                p_work_duration_id => r.work_duration_id,
                p_on_call          => r.on_call,
                p_emergency        => r.emergency,
                p_sun              => r.sun,
                p_mon              => r.mon,
                p_tue              => r.tue,
                p_wed              => r.wed,
                p_thu              => r.thu,
                p_fri              => r.fri,
                p_sat              => r.sat,
                p_person_roster_id => r.person_roster_id,
                p_out              => l_out
            );
        exception
            when others then
                -- e.g. NO_DATA_FOUND for an unknown work duration (raised outside the single
                -- procedure's own handler): undo this entry only, report it, continue the batch
                rollback to sc_spot_bulk_entry;
                l_out := 'E#' || sqlerrm;
        end;

        l_result := json_object_t();
        l_result.put('ref', r.ref);
        l_result.put('out', nvl(l_out, 'E#No result returned'));
        l_results.append(l_result);
    end loop;

    p_results := l_results.to_clob;
end;
/
