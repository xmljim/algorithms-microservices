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

/**
 * A function represents a discrete computation that returns a
 * value. Functions can have 0 to <em>n</em> parameters that
 * it can act on in the process of computation.
 * @param <T> The underlying type of the return value
 */
public interface Function<T> extends Parameterized {

    /**
     * Compute and return the function value
     * @return the function value
     */
    T compute();
}
