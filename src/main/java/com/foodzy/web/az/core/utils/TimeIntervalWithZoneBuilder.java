package com.foodzy.web.az.core.utils;

import com.foodzy.web.az.cms_v1.analytics.service.QueryEndSelector;
import com.foodzy.web.az.cms_v1.analytics.service.QueryStartSelector;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimeIntervalWithZoneBuilder {

    /**
     * Utility method to build a {@link ZoneOffset} based on UTC timezoneOffset (in seconds).
     *
     * @return
     */
    // protected static ZoneOffset zoneOffset(int utcOffset) throws IllegalArgumentException {
    //     if (utcOffset < -43200)
    //         throw new IllegalArgumentException("Failed to get ZoneOffset (reason: timezoneOffset is out of range [-43200, 46800]");
    //     if (utcOffset > 46800)
    //         throw new IllegalArgumentException("Failed to get ZoneOffset (reason: timezoneOffset is out of range [-43200, 46800]");
    //     return ZoneOffset.ofTotalSeconds(utcOffset);
    // }

    public static TimeInterval buildTimeInterval(
            LocalDate userStartDate,
            LocalDate userEndDate,
            int utcOffset,
            boolean unprocessed)
            throws DateTimeException, IllegalArgumentException {
        return buildTimeInterval(userStartDate, userEndDate, zoneOffset(utcOffset), unprocessed);
    }

    /**
     * Build a {@link TimeInterval} based on user query date range.
     *
     * @param userStartDate
     * @param userEndDate
     * @param zoneOffset
     * @param unprocessed
     * @return
     * @throws DateTimeException
     * @throws IllegalArgumentException
     */
    public static TimeInterval buildTimeInterval(
            LocalDate userStartDate,
            LocalDate userEndDate,
            ZoneOffset zoneOffset,
            boolean unprocessed)
            throws DateTimeException, IllegalArgumentException {
        if (Objects.isNull(zoneOffset))
            throw new IllegalArgumentException("Failed to build query time interval (reason: invalid zone timezoneOffset).");
        return TimeInterval.of(
                new QueryStartSelector(zoneOffset)
                        .addUserQuery(userStartDate)
                        .select(),
                new QueryEndSelector(zoneOffset, unprocessed)
                        .addUserQuery(userEndDate)
                        .select());
    }

    public static TimeInterval buildTimeInterval(
            String userStartDate,
            String userEndDate,
            ZoneOffset zoneOffset,
            boolean unprocessed)
            throws DateTimeException, IllegalArgumentException {
        if (Objects.isNull(zoneOffset))
            throw new IllegalArgumentException("Failed to build query time interval (reason: invalid zone timezoneOffset).");
        LocalDate end = Objects.nonNull(userEndDate) ? LocalDate.parse(userEndDate, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        LocalDate start = Objects.nonNull(userStartDate) ? LocalDate.parse(userStartDate, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        return TimeInterval.of(
                new QueryStartSelector(zoneOffset)
                        .addUserQuery(start)
                        .select(),
                new QueryEndSelector(zoneOffset, unprocessed)
                        .addUserQuery(end)
                        .select());
    }

    public static TimeInterval buildTimeInterval(
            LocalDate userStartDate,
            LocalDate userEndDate,
            Integer startUtcOffset,
            Integer endUTcOffset,
            int defaultUtcOffset,
            String timeZone)
            throws DateTimeException, IllegalArgumentException {
        ZoneOffset startZoneOffset = TimeIntervalWithZoneBuilder.zoneOffset(Objects.nonNull(startUtcOffset) ? startUtcOffset : defaultUtcOffset);
        ZoneOffset endZoneOffset = TimeIntervalWithZoneBuilder.zoneOffset(Objects.nonNull(endUTcOffset) ? endUTcOffset : defaultUtcOffset);
        ZoneId zoneId = null;
        try {
            zoneId = ZoneId.of(timeZone);
        } catch (Exception ignore) {
        }
        return TimeInterval.of(
                new QueryStartSelector(startZoneOffset, zoneId)
                        .addUserQuery(userStartDate)
                        .select(),
                new QueryEndSelector(endZoneOffset, zoneId, false)
                        .addUserQuery(userEndDate)
                        .select());
    }

    public static class TimeIntervalException extends Exception {
        public TimeIntervalException(String message) {
            super(message);
        }
    }

    /**
     * Builds a TimeInterval based on user-provided dates and UTC offset.
     * @param userStartDate start date as LocalDate
     * @param userEndDate end date as LocalDate
     * @param utcOffset offset in seconds
     * @param unprocessed flag indicating whether the date is unprocessed
     * @return a TimeInterval instance
     * @throws TimeIntervalException if any issue with date parsing or zone calculation
     */
    public static TimeInterval buildTimeInterval(
            String userStartDate,
            String userEndDate,
            int utcOffset,
            boolean unprocessed) throws TimeIntervalException {
        try {
            ZoneOffset zoneOffset = safeZoneOffset(utcOffset);
            LocalDate start = parseDate(userStartDate);
            LocalDate end = parseDate(userEndDate);

            return TimeInterval.of(
                    prepareStartSelector(start, zoneOffset).select(),
                    new QueryEndSelector(zoneOffset, unprocessed).addUserQuery(end).select());
        } catch (DateTimeException | IllegalArgumentException e) {
            throw new TimeIntervalException("Failed to build time interval: " + e.getMessage());
        }
    }

    /**
     * Utility method to build a {@link ZoneOffset} based on UTC timezoneOffset (in seconds).
     *
     * @return ZoneOffset object
     * @throws IllegalArgumentException if the utcOffset is out of the expected range
     */
    protected static ZoneOffset safeZoneOffset(int utcOffset) throws IllegalArgumentException {
        if (utcOffset < -43200 || utcOffset > 46800) {
            throw new IllegalArgumentException("Timezone offset is out of allowed range [-43200, 46800].");
        }
        return ZoneOffset.ofTotalSeconds(utcOffset);
    }

    /**
     * Prepares a QueryStartSelector with a start date and a zone offset.
     * @param startDate the start date
     * @param zoneOffset the zone offset
     * @return an instance of QueryStartSelector
     */
    private static QueryStartSelector prepareStartSelector(LocalDate userStartDate, ZoneOffset zoneOffset) {
    QueryStartSelector selector = new QueryStartSelector(zoneOffset);
    if (userStartDate != null) {
            selector.addUserQuery(userStartDate);
        }
        return selector;
    }


    /**
     * Utility method to parse a date from a String.
     * @param date the date string to parse
     * @return LocalDate object or null if the string is invalid
     */
    private static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeException e) {
            return null;
        }
    }
}
