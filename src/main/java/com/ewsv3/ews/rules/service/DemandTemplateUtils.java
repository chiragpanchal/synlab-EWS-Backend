package com.ewsv3.ews.rules.service;

public class DemandTemplateUtils {

    public static String DemandTemplateHSql = """
            select
                demand_template_id,
                template_name,
                valid_to,
                profile_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            from
                sc_demand_template_h h
            where (:templateName is null or h.template_name = :templateName )
                and (:profileId =0 or h.profile_id=:profileId)
                and exists (
                    select
                        'Y'
                    from
                        sc_user_profile_assoc supa,
                        sc_access_profiles    sap
                    where
                            sap.profile_id = supa.profile_id
                        and sap.profile_id = h.profile_id
                        and supa.user_id   = :userId
                        and trunc(sysdate) between sap.start_date and nvl(
                            sap.end_date,
                            trunc(sysdate)
                        )
                )
            order by
                template_name""";

    public static String DemandTemplateLineSql = """
            select
                l.demand_template_line_id,
                l.demand_template_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.sun,
                l.mon,
                l.tue,
                l.wed,
                l.thu,
                l.fri,
                l.sat,
                l.created_by,
                l.created_on,
                l.last_updated_by,
                l.last_update_date
            from
                sc_demand_template_l l
            where
                l.demand_template_id = :demandTemplateId
            order by
                l.demand_template_line_id""";

    public static String insertDemandTemplateHeader = """
            insert into sc_demand_template_h (
                demand_template_id,
                created_by,
                profile_id,
                valid_to,
                last_updated_by,
                template_name
            ) values (
                :demandTemplateId,
                :createdBy,
                :profileId,
                :validTo,
                :lastUpdatedBy,
                :templateName
            )""";

    public static String updateDemandTemplateHeader = """
            update sc_demand_template_h
            set
                profile_id = :profileId,
                valid_to = :validTo,
                last_updated_by = :lastUpdatedBy,
                template_name = :templateName
            where
                demand_template_id = :demandTemplateId""";

    public static String deleteDemandTemplateHeader = """
            delete from sc_demand_template_h
            where
                demand_template_id = :demandTemplateId""";

    public static String insertDemandTemplateLine = """
            insert into sc_demand_template_l (
                demand_template_line_id,
                demand_template_id,
                department_id,
                job_title_id,
                location_id,
                work_duration_id,
                sun,
                mon,
                tue,
                wed,
                thu,
                fri,
                sat,
                created_by,
                last_updated_by
            ) values (
                :demandTemplateLineId,
                :demandTemplateId,
                :departmentId,
                :jobTitleId,
                :locationId,
                :workDurationId,
                :sun,
                :mon,
                :tue,
                :wed,
                :thu,
                :fri,
                :sat,
                :createdBy,
                :lastUpdatedBy
            )""";

    public static String updateDemandTemplateLine = """
            update sc_demand_template_l
            set
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                location_id = :locationId,
                work_duration_id = :workDurationId,
                sun = :sun,
                mon = :mon,
                tue = :tue,
                wed = :wed,
                thu = :thu,
                fri = :fri,
                sat = :sat,
                last_updated_by = :lastUpdatedBy
            where
                demand_template_line_id = :demandTemplateLineId""";

    public static String deleteDemandTemplateLine = """
            delete sc_demand_template_l
            where
                demand_template_line_id = :demandTemplateLineId""";

    public static String DemandTemplateSkillsSql = """
            SELECT
               demand_template_skill_id,
               demand_template_line_id,
               skill_id,
               rating,
               must_have,
               created_by,
               created_on,
               last_updated_by,
               last_update_date
             FROM
               sc_demand_template_skills ds
            WHERE
               ds.demand_template_line_id = :demandTemplateLineId
            ORDER BY
               demand_template_skill_id
                                   """;

    public static String CreateDemandTemplateSkillsSql = """
            INSERT INTO sc_demand_template_skills (
                demand_template_line_id,
                skill_id,
                rating,
                must_have,
                created_by,
                last_updated_by
            ) VALUES (
                :demandTemplateLineId,
                :skillId,
                :rating,
                :mustHave,
                :createdBy,
                :lastUpdatedBy
            )
                                    """;

    public static String UpdateDemandTemplateSkillsSql = """
            UPDATE sc_demand_template_skills
               SET
                skill_id = :skillId,
                rating = :rating,
                must_have = :mustHave,
                last_updated_by = :lastUpdatedBy,
                last_update_date = sysdate
             WHERE
                demand_template_skill_id = :demandTemplateSkillId
                        """;

    public static String DeleteDemandTemplateSkillsSql = """
            DELETE sc_demand_template_skills
             WHERE
                demand_template_skill_id = :demandTemplateSkillId
            """;
}
