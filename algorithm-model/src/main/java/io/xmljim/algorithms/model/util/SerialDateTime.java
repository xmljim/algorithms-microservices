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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * A numeric representation of a {@link LocalDateTime}. The numeric representation
 * is stored as Epoch Seconds (the number of seconds since January 1, 1970).
 * SerialDate extends from number and can be used as a {@link Scalar} value
 */
public final class SerialDateTime extends SerialTemporal {

    /**
     * Private constructor. Use #of methods to create instances
     *
     * @param epochSeconds the number of epoch seconds since January 1, 1970
     * @param ldt          the localDateTime instance
     */
    private SerialDateTime(Number epochSeconds, LocalDateTime ldt) {
        super(epochSeconds, ldt);
    }

    /**
     * Parse a date-time string into a LocalDateTime instance and transform to a numeric representation
     *
     * @param dateTimeString the date-time string to parse
     * @return a new SerialDateTime instance
     */
    public static SerialDateTime of(String dateTimeString) {
        LocalDateTime ldt = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        return new SerialDateTime(ldt.toEpochSecond(ZoneOffset.ofTotalSeconds(0)), ldt);
    }

    /**
     * Convert a LocalDateTime instance to a SerialDateTime instance
     *
     * @param localDateTime the date-time instance
     * @return a new SerialDateTime instance
     */
    public static SerialDateTime of(LocalDateTime localDateTime) {
        return new SerialDateTime(localDateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)), localDateTime);
    }

    /**
     * Compute a LocalDateTime instance from epoch seconds and convert to a numeric representation
     *
     * @param epochSeconds the number of seconds since January 1, 1970
     * @return a new SerialDateTime instance
     */
    public static SerialDateTime ofSeconds(long epochSeconds) {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.ofTotalSeconds(0));
        return new SerialDateTime(epochSeconds, ldt);
    }

    /**
     * Return a formatted date-time string
     *
     * @return a formatted date-time
     */
    @SuppressWarnings("unused")
    @Override
    public String toDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return formatter.format(toLocalDateTime());
    }
}
