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

import io.xmljim.algorithms.model.*;

public interface ParameterFactory {

    <T> Parameter<T> createParameter(String name, String variable, T value);

    default <T> Parameter<T> createParameter(String name, T value) {
        return createParameter(name, null, value);
    }

    <T> FunctionParameter<T> createParameter(String name, String variable, Function<T> value);

    default <T> FunctionParameter<T> createParameter(String name, Function<T> value) {
        return createParameter(name, null, value);
    }

    default <T> FunctionParameter<T> createParameter(Function<T> function) {
        return createParameter(function.getName(), function.getVariable(), function);
    }

    ScalarParameter createParameter(String name, String variable, Number scalar);

    default ScalarParameter createParameter(String name, Number scalar) {
        return createParameter(name, null, scalar);
    }

    <T> VectorParameter<T> createParameter(String name, String variable, Vector<T> vector);

    default <T> VectorParameter<T> createParameter(String name, Vector<T> vector) {
        return createParameter(name, null, vector);
    }

    default <T> VectorParameter<T> createParameter(Vector<T> vector) {
        return createParameter(vector.getName(), vector.getVariable(), vector);
    }

    ScalarVectorParameter createParameter(String name, String variable, ScalarVector scalarVector);

    default ScalarVectorParameter createParameter(String name, ScalarVector scalarVector) {
        return createParameter(name, null, scalarVector);
    }

    default ScalarVectorParameter createParameter(ScalarVector scalarVector) {
        return createParameter(scalarVector.getName(), scalarVector.getVariable(), scalarVector);
    }

    ScalarFunctionParameter createParameter(String name, String variable, ScalarFunction function);

    default ScalarFunctionParameter createParameter(String name, ScalarFunction function) {
        return createParameter(name, null, function);
    }

    default ScalarFunctionParameter createParameter(ScalarFunction function) {
        return createParameter(function.getName(), function.getVariable(), function);
    }

    MatrixParameter createParameter(String name, String variable, Matrix matrix);

    default MatrixParameter createParameter(String name, Matrix matrix) {
        return createParameter(name, null, matrix);
    }

    default MatrixParameter createParameter(Matrix matrix) {
        return createParameter(matrix.getName(), null, matrix);
    }

}
