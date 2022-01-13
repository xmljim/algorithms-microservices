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

package io.xmljim.algorithms.model.provider;

import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.Vector;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public interface VectorFactory {

    <T> Vector<T> createVector(String name, String variable, Stream<T> stream);

    default <T> Vector<T> createVector(String name, Stream<T> stream) {
        return createVector(name, null, stream);
    }

    default <T> Vector<T> createVector(String name, String variable, Collection<T> collection) {
        return createVector(name, variable, collection.stream());
    }

    default <T> Vector<T> createVector(String name, Collection<T> collection) {
        return createVector(name, null, collection);
    }

    default <T> Vector<T> createVector(String name, String variable, T... values) {
        return createVector(name, variable, Arrays.stream(values));
    }

    default <T> Vector<T> createVector(String name, T... values) {
        return createVector(name, values);
    }

    ScalarVector createScalarVector(String name, String variable, Stream<Number> stream);

    default ScalarVector createScalarVector(String name, Stream<Number> stream) {
        return createScalarVector(name, null, stream);
    }

    default ScalarVector createScalarVector(String name, String variable, Collection<Number> scalars) {
        return createScalarVector(name, variable, scalars.stream().map(Scalar::of));
    }

    default ScalarVector createScalarVector(String name, Collection<Number> scalars) {
        return createScalarVector(name, null, scalars);
    }

    default ScalarVector createScalarVector(String name, String variable, Number... numbers) {
        return createScalarVector(name, variable, Arrays.stream(numbers).map(Scalar::of));
    }

    default ScalarVector createScalarVector(String name, Number... numbers) {
        return createScalarVector(name, null, numbers);
    }
}
