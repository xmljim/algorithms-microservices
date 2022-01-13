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

import java.util.stream.Stream;

/**
 * A model represents a computed set of coefficients that can be
 * used to solve for input values.
 */
public interface Model extends Parameterized {

    /**
     * Return a named coefficient
     * @param name the coefficient name
     * @param <T> the assigned type of the coefficient
     * @return the named coefficient or null if it doesn't exist
     */
    <T> Coefficient<T> getCoefficient(String name);

    /**
     * Return a named coefficient's value
     * @param name the coefficient name
     * @param <T> the assigned type of the coefficient
     * @return the named coefficient's value or null if it doesn't exist
     */
    default <T> T getCoefficientValue(String name) {
        T value = null;
        Coefficient<T> coefficient = getCoefficient(name);
        if (coefficient != null) {
            value = coefficient.getValue();
        }

        return value;
    }

    /**
     * Return a stream of all coefficients in the model
     * @return a stream of coefficients
     */
    Stream<Coefficient<?>> coefficients();

    /**
     * invoke a solution for the model after initialization
     */
    void solve();

}
