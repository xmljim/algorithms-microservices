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

import java.text.DecimalFormat;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A scalar represents any value that can be interpreted as a number.
 * Internally, the class can transform specific types into numeric representations
 * e.g., dates ({@link LocalDate}), date-times ({@link LocalDateTime}, booleans.
 * classes that are subclasses of {@link Number} are stored without transformation.
 */

@SuppressWarnings("unused")
public final class Scalar extends Number implements Comparable<Scalar> {

    private final Number internalValue;
    private final Object refValue;

    /**
     * Private constructor. Use {@link #of} methods
     *
     * @param refValue      the underlying object containing the scalar value
     * @param internalValue the numeric representation of the object
     */
    private Scalar(Object refValue, Number internalValue) {
        this.refValue = refValue;
        this.internalValue = internalValue;
    }

    /**
     * Creates a scalar instance from a number
     *
     * @param number a number subclass
     * @return the scalar for the number
     */
    public static Scalar of(Number number) {
        return new Scalar(number, number);
    }

    /**
     * Return a scalar of a local date
     *
     * @param localDate the date value
     * @return a new scalar
     * @see SerialTemporal#of(LocalDate)
     */
    public static Scalar of(LocalDate localDate) {
        return new Scalar(localDate, SerialTemporal.of(localDate));
    }

    /**
     * Return a scalar of a local date time
     *
     * @param localDateTime the local date time
     * @return a new scalar
     * @see SerialTemporal#of(LocalDateTime)
     */
    public static Scalar of(LocalDateTime localDateTime) {
        return new Scalar(localDateTime, SerialTemporal.of(localDateTime));
    }

    /**
     * Return a scalar of a boolean value. Internally the boolean value
     * is transformed into {@code 0-true} or {@code 1-false}
     *
     * @param booleanValue the boolean value
     * @return a new scalar
     */
    public static Scalar of(Boolean booleanValue) {
        return new Scalar(booleanValue, booleanValue ? 0 : 1);
    }

    /**
     * Return the scalar as a casted number value
     *
     * @param <T> the number type
     * @return the numeric value
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> T asNumber() {
        return (T) internalValue;
    }

    /**
     * Return the scalar as a boolean
     *
     * @return {@code true} if the numeric value is 0, otherwise false;
     */
    public Boolean asBoolean() {
        return internalValue.intValue() == 0;
    }

    /**
     * Return the scalar as a double value
     *
     * @return the double value
     */
    public double asDouble() {
        return asNumber().doubleValue();
    }

    /**
     * Return the scalar as an integer
     *
     * @return the integer value
     */
    public int asInt() {
        return asNumber().intValue();
    }

    /**
     * Return the scalar as a long
     *
     * @return the long value
     */
    public long asLong() {
        return asNumber().longValue();
    }

    @Override
    public String toString() {
        //return refValue.toString();
        return new DecimalFormat("0.000###########").format(internalValue);
    }

    /**
     * Apply a formatter to the underlying data object and return the formatted string
     *
     * @param format the formatter
     * @return the formatted string
     */
    public String format(Format format) {
        return format.format(refValue);
    }

    public boolean equals(Object object) {
        if (object instanceof Scalar) {
            return asDouble() == ((Scalar) object).asDouble();
        } else if (object instanceof Number) {
            equals(Scalar.of((Number)object));
        }
        return false;
    }

    @Override
    public int compareTo(final Scalar o) {
        return Scalar.of(equals(o)).asInt();
    }

    @Override
    public int intValue() {
        return asInt();
    }

    @Override
    public long longValue() {
        return asLong();
    }

    @Override
    public float floatValue() {
        return internalValue.floatValue();
    }

    @Override
    public double doubleValue() {
        return asDouble();
    }
}