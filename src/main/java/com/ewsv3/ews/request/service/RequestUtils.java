package com.ewsv3.ews.request.service;

public class RequestUtils {

    static String RequestsMasterSql = """
            SELECT
                srm.request_master_id,
                srm.request_name,
                srm.enabled,
                srm.punch_exception,
                srm.time_type,
                srm.req_code,
                srm.multiple_day_allowed,
                srr.request_reason_id,
                srr.reason,
                src.request_criteria_id,
                src.job_id,
                src.department_id,
                src.position_id,
                src.work_location_id,
                src.employee_type_id,
                src.business_unit_id,
                src.payroll_id,
                src.job_family,
                src.legal_entity_id,
                src.gender,
                src.nationality,
                src.religion,
                src.grade_id
              FROM
                sc_requests_master  srm,
                sc_request_reasons  srr,
                sc_request_criteria src
             WHERE
                    nvl(
                        srm.enabled,
                        'N'
                    ) = 'Y'
                   AND srr.request_master_id (+) = srm.request_master_id
                   AND nvl(
                    srr.enabled(+),
                    'N'
                )  = 'Y'
                   AND src.request_master_id(+) = srm.request_master_id
             ORDER BY
                srm.request_name,
                srr.reason""";

    static String RequestLinesSql = """
            SELECT
                spra.person_id,
                spra.person_request_id,
                srm.request_name,
                srr.reason,
                spra.date_start,
                spra.date_end,
                spra.time_start,
                spra.time_end,
                spra.item_key,
                spra.created_on,
                decode(si.completion_date, NULL, 'Awaiting Approval', 'Approved') status,
                spra.comments
            FROM
                sc_person_requests_appr spra,
                sc_requests_master      srm,
                sc_items                si,
                sc_request_reasons      srr
            WHERE
                    spra.person_id = :personId
                AND srm.request_master_id = spra.request_master_id
                AND ((trunc(spra.date_start) between :startDate and :endDate
                OR trunc(spra.date_end) between :startDate and :endDate)
                OR :startDate between trunc(spra.date_start) and trunc(spra.date_end))
                AND si.item_key = spra.item_key
                AND srr.request_master_id (+) = srm.request_master_id
                AND srr.request_reason_id(+)= spra.request_reason_id
            ORDER BY
                spra.created_on DESC""";

    static String RequestsApprovalSql = """
            SELECT
                sn.item_Key,
                sn.notification_Id,
                sn.action_Type,
                sn.from_Action,
                sn.created_On,
                sn.status,
                sn.role_Name,
                mgr.user_Id,
                mgr.full_Name,
                mgr.email_Address,
                usr.full_Name from_user
              FROM
                sc_notifications  sn,
                sc_notif_comments snc,
                sc_person_v       mgr,
                sc_person_v       usr 
             WHERE
                    sn.item_key = :itemKey
                   AND snc.notification_id = sn.notification_id
                   AND mgr.user_id         = sn.to_user_id
                   AND usr.user_id         = sn.from_user_id
             ORDER BY
                sn.notification_id,
                usr.full_name""";
}
