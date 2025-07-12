package com.ewsv3.ews.dashboard.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class demo {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();

        // Get the start of the current week (Monday)
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // Print the start of the week
        System.out.println("Start of the week: " + startOfWeek);

        LocalDate startOfPrevious5thWeek = startOfWeek.minus(5, ChronoUnit.WEEKS);

        // Print the start of the previous 5th week
        System.out.println("Start of the previous 5th week: " + startOfPrevious5thWeek);

    }
}
