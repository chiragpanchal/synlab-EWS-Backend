package com.ewsv3.ews.selfroster.service;

public class SelfRosterUtils {

    static String sqlGetSelfRosters = """
            select
                sr.self_roster_id,
                sr.from_date,
                sr.to_date,
                sr.person_id,
                sr.department_id,
                sr.job_title_id,
                sr.work_location_id,
                sr.comments,
                sr.status,
                sr.item_key,
                sr.created_by,
                sr.created_on,
                sr.last_updated_by,
                sr.last_update_date
            from
                sc_self_roster sr,
                sc_person_v    per
            where
                    per.person_id = sr.person_id
                and per.user_id = :userId
            order by
                self_roster_id
                """;

    static String sqlGetSelfRosterLines = """
            select
                    self_roster_line_id,
                    self_roster_id,
                    d_1,
                    d_2,
                    d_3,
                    d_4,
                    d_5,
                    d_6,
                    d_7,
                    person_roster_id_1,
                    person_roster_id_2,
                    person_roster_id_3,
                    person_roster_id_4,
                    person_roster_id_5,
                    person_roster_id_6,
                    person_roster_id_7,
                    created_by,
                    created_on,
                    last_updated_by,
                    last_update_date,
                    start_date,
                    end_date
                from
                    sc_self_roster_lines srl
                where
                    srl.self_roster_id = :selfRosterId""";

    static String sqlGetSelfRosterApprovals = "";

}
