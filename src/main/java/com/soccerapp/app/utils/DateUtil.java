package com.soccerapp.app.utils;

public class DateUtil {
    // Existing date conversion methods, if any

    public static String getDayWithSuffix(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
        if (day % 10 == 1 && day % 100 != 11) {
            return day + "st";
        } else if (day % 10 == 2 && day % 100 != 12) {
            return day + "nd";
        } else if (day % 10 == 3 && day % 100 != 13) {
            return day + "rd";
        } else {
            return day + "th";
        }
    }
}
