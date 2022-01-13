/*
 * Copyright 2021-2022 Jim Earley (xml.jim@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.xmljim.algorithms.model.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A numeric representation of a {@link LocalDate}. The numeric representation
 * is stored as Epoch Seconds (the number of seconds since January 1, 1970).
 * SerialDate extends from number and can be used as a {@link Scalar} value
 */
@SuppressWarnings("unused")
public final class SerialDate extends SerialTemporal {
    //private static final LocalDate BASE_DATE = LocalDate.of(1970, 01, 01);

    /**
     * Private constructor. Use static #of methods
     *
     * @param epochSeconds The number of seconds since January 1, 1970
     * @param localDate    the local date instance
     */
    private SerialDate(Number epochSeconds, LocalDate localDate) {
        super(epochSeconds, localDate);
    }

    /**
     * Parse a date string into a local date and convert to a numeric representation
     *
     * @param dateString the date string
     * @return a new SerialDate instance
     */
    public static SerialDate of(String dateString) {
        LocalDate ld = LocalDate.parse(dateString);
        long epochSeconds = ld.toEpochDay() * DAY_SECONDS;
        return new SerialDate(epochSeconds, ld);
    }

    /**
     * Convert a LocalDate instance to a numeric representation
     *
     * @param localDate the localDate instance
     * @return a new SerialDate instance
     */
    public static SerialDate of(LocalDate localDate) {
        long epochSeconds = localDate.toEpochDay() * DAY_SECONDS;
        return new SerialDate(epochSeconds, localDate);
    }

    /**
     * Compute a serial date from the number of epoch seconds
     *
     * @param epochSeconds the number of seconds since January 1, 1970
     * @return a new SerialDate instance
     */
    public static SerialDate ofSeconds(long epochSeconds) {
        long remainder = epochSeconds % DAY_SECONDS;
        long epochDay = epochSeconds / DAY_SECONDS - remainder;
        return new SerialDate(epochSeconds - remainder, LocalDate.ofEpochDay(epochDay));
    }

    /**
     * Compute a serial date from the number of days since January 1, 1970
     *
     * @param epochDays the number of days since January 1, 1970
     * @return a new SerialDate instance
     */
    public static SerialDate ofDays(long epochDays) {
        long epochSeconds = epochDays * DAY_SECONDS;
        return new SerialDate(epochSeconds, LocalDate.ofEpochDay(epochDays));
    }

    /**
     * Return a formatted date string
     *
     * @return a formatted date
     */
    public String toDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        return formatter.format(toLocalDate());
    }

    /**
     * Returns the numeric value as a string
     *
     * @return the numeric value as a string
     */
    public String toString() {
        return String.valueOf(longValue());
    }
}
