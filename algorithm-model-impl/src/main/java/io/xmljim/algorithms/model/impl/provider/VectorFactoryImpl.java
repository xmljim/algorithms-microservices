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

package io.xmljim.algorithms.model.impl.provider;

import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.Vector;
import io.xmljim.algorithms.model.provider.VectorFactory;

import java.util.stream.Stream;

class VectorFactoryImpl implements VectorFactory {

    @Override
    public <T> Vector<T> createVector(final String name, final String variable, final Stream<T> stream) {
        return new BaseVectorImpl<>(name, variable, stream);
    }

    @Override
    public ScalarVector createScalarVector(final String name, final String variable, final Stream<Number> stream) {
        return new ScalarVectorImpl(name, variable, stream);
    }
}
