package com.ewsv3.ews.schedulers.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SchedulerConstants {
    static LocalDateTime cutoffDate = LocalDateTime.now().minusDays(365);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    static String cutoffFormattedDate = cutoffDate.format(formatter);

    // Base URL and Credentials
    // public static final String baseUrl =
    // "https://fa-essm-test-saasfaprod1.fa.ocs.oraclecloud.com";
    public static final String baseUrl = "https://fa-eugp-dev2-saasfaprod1.fa.ocs.oraclecloud.com";
    public static final String fusionUserName = "Chirag.Panchal1@mastek.com";
    public static final String fusionPassword = "Welcome@123";

}
