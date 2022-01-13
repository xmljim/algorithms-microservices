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

package io.xmljim.algorithms.model;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A sequence of values. Values can be accessed by position or through
 * streams, and also contain helper methods for sorting and splicing.
 * The vector is "storage-agnostic", meaning that it leaves that to the
 * specific implementation (e.g., arrays, lists, sets) to address.
 *
 * @param <T> The value type
 */
@SuppressWarnings("unused")
public interface Vector<T> extends VariableEntity, Iterable<T> {

    /**
     * Return the first value in the vector
     *
     * @return the first value
     */
    T first();

    /**
     * Return the last value in the vector
     *
     * @return the last value in the vector
     */
    T last();

    /**
     * Return the value at a given position
     *
     * @param index the position within the vector
     * @return the value at a given position
     */
    T get(long index);

    /**
     * Return the vector length
     *
     * @return the vector length
     */
    int length();

    default int getElementSize() {
        return this.length();
    }

    /**
     * Returns whether the vector does not contain values
     *
     * @return {@code true} if the vector does not contain any values; {@code false} otherwise
     */
    default boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Return a stream for the vector
     *
     * @return the vector stream
     */
    Stream<T> stream();

    /**
     * Returns a sorted stream
     *
     * @return a sorted stream. Note that the underlying order within the vector is not modified.
     */
    default Stream<T> sorted() {
        return stream().sorted();
    }

    /**
     * Return a sorted stream using a specified comparator
     *
     * @param comparator the comparator to apply for sorting
     * @return a sorted stream. Note that the underlying order within the vector is not modified.
     */
    default Stream<T> sorted(Comparator<T> comparator) {
        return stream().sorted(comparator);
    }

    /**
     * Return a subset of a vector from the a given position to the end of the vector
     *
     * @param startIndex the starting position.
     * @return a spliced vector
     */
    default Stream<T> splice(int startIndex) {
        return IntStream.range(startIndex, (int)length()).mapToObj(this::get);
    }

    /**
     * Return a subset of a vector from a given starting position and length
     *
     * @param startIndex the starting position to begin splicing the vector
     * @param length     the number of elements to include including the starting position value
     * @return a spliced vector
     */
    default Stream<T> splice(int startIndex, int length) {
        return IntStream.range(startIndex, startIndex + length).mapToObj(this::get);
    }
}
