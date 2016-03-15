package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * GKislin
 * 07.01.2015.
 */
public class TimeUtil {
    public static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetweenDate(LocalDate ld, LocalDate start, LocalDate end) {
        return ld.compareTo(start) >= 0 && ld.compareTo(end) <= 0;
    }

    public static boolean isBetweenDateTime(LocalDateTime ldt, LocalDateTime start, LocalDateTime end) {
        return ldt.compareTo(start) >= 0 && ldt.compareTo(end) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TME_FORMATTER);
    }
}
