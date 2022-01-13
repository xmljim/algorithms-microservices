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

import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.provider.ParameterFactory;

class ParameterFactoryImpl implements ParameterFactory {

    @Override
    public <T> Parameter<T> createParameter(final String name, final String variable, final T value) {
        return new BaseParameterImpl<>(name, variable, value);
    }

    @Override
    public <T> FunctionParameter<T> createParameter(final String name, final String variable, final Function<T> value) {
        return new FunctionParameterImpl<>(name, variable, value);
    }

    @Override
    public ScalarParameter createParameter(final String name, final String variable, final Number scalar) {
        return new ScalarParameterImpl(name, variable, scalar);
    }

    @Override
    public <T> VectorParameter<T> createParameter(final String name, final String variable, final Vector<T> vector) {
        return new VectorParameterImpl<>(name, variable, vector);
    }

    @Override
    public ScalarVectorParameter createParameter(final String name, final String variable, final ScalarVector scalarVector) {
        return new ScalarVectorParameterImpl(name, variable, scalarVector);
    }

    @Override
    public ScalarFunctionParameter createParameter(final String name, final String variable, final ScalarFunction function) {
        return new ScalarFunctionParameterImpl(name, variable, function);
    }

    @Override
    public MatrixParameter createParameter(final String name, final String variable, final Matrix matrix) {
        return new MatrixParameterImpl(name, variable, matrix);
    }
}
