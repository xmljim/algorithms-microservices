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

import io.xmljim.algorithms.model.util.Scalar;

/**
 * A vector holding {@link Scalar values}
 */
public interface ScalarVector extends Vector<Scalar> {

    /**
     * Return an array of {@link Scalar} values
     * @return an array of Scalar values
     */
    default Scalar[] toScalarArray() {
        return stream().toArray(Scalar[]::new);
    }

    /**
     * Return an array of double values
     * @return an array of double values
     */
    default double[] toDoubleArray() {
        return stream().mapToDouble(Scalar::asDouble).toArray();
    }
}
