package com.github.coreycaplan3.bookmarket.utilities;

import java.util.Calendar;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public final class FormValidation {

    private FormValidation() {
    }

    /**
     * Checks if a field is empty (after trimming it).
     *
     * @param field The field that should be checked
     * @return True if the field is empty or false if it is not.
     */
    public static boolean isEmpty(String field) {
        return field == null || field.trim().length() == 0;
    }

    /**
     * Checks if a text field is too short (after trimming it).
     *
     * @param minimumLength The minimum length of the field.
     * @param field         The field that should be checked.
     * @return True if the field is too short or false if its length is okay.
     */
    public static boolean isTooShort(int minimumLength, String field) {
        return field == null || field.trim().length() < minimumLength;
    }

    /**
     * Checks if a text field is too long (after trimming it).
     *
     * @param maximumLength The minimum length of the field.
     * @param field         The field that should be checked.
     * @return True if the field is too long or false if its length is okay.
     */
    public static boolean isTooLong(int maximumLength, String field) {
        return field == null || field.trim().length() > maximumLength;
    }

    /**
     * Checks if a start date is before the end date.
     *
     * @param startDateMillis The start date that should be checked.
     * @param endDateMillis   The end date that should be checked.
     * @return True if the start date is on the same day or earlier than the end date.
     */
    public static boolean isStartDateBeforeEndDate(long startDateMillis, long endDateMillis) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(startDateMillis);
        endDate.setTimeInMillis(endDateMillis);
        int startYear = startDate.get(Calendar.YEAR);
        int startDay = startDate.get(Calendar.DAY_OF_YEAR);
        int endYear = endDate.get(Calendar.YEAR);
        int endDay = endDate.get(Calendar.DAY_OF_YEAR);
        return startYear < endYear || (startYear == endYear && startDay < endDay);
    }

    /**
     * Checks if a start date is the same as the end date.
     *
     * @param startDateMillis The start date that should be checked.
     * @param endDateMillis   The end date that should be checked.
     * @return True if the start date is on the same day or earlier than the end date.
     */
    public static boolean isStartDateSameAsEndDate(long startDateMillis, long endDateMillis) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(startDateMillis);
        endDate.setTimeInMillis(endDateMillis);
        int startYear = startDate.get(Calendar.YEAR);
        int startDay = startDate.get(Calendar.DAY_OF_YEAR);
        int endYear = endDate.get(Calendar.YEAR);
        int endDay = endDate.get(Calendar.DAY_OF_YEAR);
        return startYear == endYear && startDay == endDay;
    }

    /**
     * Checks if an event's start date is on the same day as the end date.
     *
     * @param startDate The event's start date.
     * @param endDate   The event's end date.
     * @return True if they're on the same day or false if they are not.
     */
    public static boolean isTimeOneDayLong(long startDate, long endDate) {
        Calendar startDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        endDateCalendar.setTimeInMillis(endDate);
        int startYear = startDateCalendar.get(Calendar.YEAR);
        int startDay = startDateCalendar.get(Calendar.DAY_OF_YEAR);
        int endYear = endDateCalendar.get(Calendar.YEAR);
        int endDay = endDateCalendar.get(Calendar.DAY_OF_YEAR);

        return (startYear == endYear) && (startDay == endDay);
    }

    /**
     * Checks if the given event's start time is within 15 minutes or less of the current time.
     *
     * @param startDate The start date of the event.
     * @param startTime The start time of the event.
     * @return True if the start time is too close to the present time (within 30 minutes) or is
     * before it. Returns false if the start time is after the current time (it's valid).
     */
    public static boolean isEventStartTimeBeforeCurrentTime(Calendar startDate,
                                                            Calendar startTime) {
        Calendar currentTime = Calendar.getInstance();
        Calendar fullStartTime = Calendar.getInstance();
        fullStartTime.setTimeInMillis(startDate.getTimeInMillis());
        fullStartTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
        fullStartTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
        long millisecondsInMinute = 60 * 1000;
        long thirtyMinutesInMillis = millisecondsInMinute * 30;

        return fullStartTime.getTimeInMillis() -
                (thirtyMinutesInMillis + currentTime.getTimeInMillis()) < 0;
    }

    /**
     * Checks if the start time is after the end time.
     *
     * @param startTime The event's start time
     * @param endTime   The event's end time
     * @return True if the event's start time is after the end time, or false if it is equal to
     * or before it.
     */
    public static boolean isStartTimeAfterEndTime(Calendar startTime, Calendar endTime) {
        if (startTime.get(Calendar.HOUR_OF_DAY) > endTime.get(Calendar.HOUR_OF_DAY)) {
            return true;
        } else if (startTime.get(Calendar.HOUR_OF_DAY) == endTime.get(Calendar.HOUR_OF_DAY)) {
            return startTime.get(Calendar.MINUTE) > endTime.get(Calendar.MINUTE);
        }
        return false;
    }

    /**
     * Checks if the start time is equal to the end time.
     *
     * @param startTime The event's start time
     * @param endTime   The event's end time
     * @return True if the event's start time is equal to the end time.
     */
    public static boolean isStartTimeEqualToEndTime(Calendar startTime, Calendar endTime) {
        return startTime.get(Calendar.HOUR_OF_DAY) == endTime.get(Calendar.HOUR_OF_DAY) &&
                startTime.get(Calendar.MINUTE) == endTime.get(Calendar.MINUTE);
    }

    /**
     * Checks if a given tag contains any spaces between words/letters.
     *
     * @param tag The tag that should be validated.
     * @return True if the tag is valid or false if it is not.
     */
    public static boolean isTagValid(String tag) {
        if (tag == null) {
            return false;
        }
        tag = tag.trim();
        for (int i = 0; i < tag.length(); i++) {
            if (tag.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }

}
