package com.ewsv3.ews.timesheets.service.submission;

public class TimesheetSubmissionUtils {

    public static String sqlGetTimesheetApprovals= """
            select
                wc.notif_comment_id,
                wc.notification_id,
                sn.action_type,
                per_from.full_name from_user,
                wc.action_taken,
                per_to.full_name   to_user,
                sn.status,
                sn.role_name,
                wc.comments,
                wc.created_on      transaction_date
            from
                sc_notifications  sn,
                sc_notif_comments wc,
                sc_person_v       per_from,
                sc_person_v       per_to
            where
                    wc.notification_id = sn.notification_id
                and sn.item_key      = :itemKey
                and per_from.user_id = wc.created_by
                and per_to.user_id   = sn.to_user_id
            order by
                wc.notif_comment_id""";
}
