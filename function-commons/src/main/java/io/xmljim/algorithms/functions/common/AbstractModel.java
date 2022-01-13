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

package io.xmljim.algorithms.functions.common;

import io.xmljim.algorithms.model.Coefficient;
import io.xmljim.algorithms.model.Model;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public abstract class AbstractModel extends ExtendedParameterized implements Model {

    private Map<String, Coefficient<?>> coefficientMap = new HashMap<>();

    public AbstractModel(final String name, final List<Parameter<?>> parameterList) {
        super(name, parameterList);
    }

    public AbstractModel(final String name, final Parameter<?>... parameters) {
        super(name, parameters);
    }

    public <T> void setCoefficient(Coefficient<T> coefficient) {
        coefficientMap.put(coefficient.getName(), coefficient);
    }

    public void setCoefficient(String name, String label, Scalar value) {
        setCoefficient(getModelProvider().getCoefficientFactory().createCoefficient(name, label, value));
    }

    public void setCoefficient(FunctionType functionType, ScalarFunction function) {
        setCoefficient(functionType.getName(), functionType.getLabel(), function.compute());
    }

    public void setCoefficient(FunctionType functionType, Scalar scalar) {
        setCoefficient(functionType.getName(), functionType.getLabel(), scalar);
    }

    public <T> void setCoefficient(FunctionType functionType, T value) {
        Coefficient<T> coefficient = getModelProvider().getCoefficientFactory().createCoefficient(functionType.getName(), functionType.getLabel(), value);
        setCoefficient(coefficient);
    }

    @Override
    public <T> Coefficient<T> getCoefficient(final String name) {
        return (Coefficient<T>) coefficientMap.get(name);
    }

    @Override
    public Stream<Coefficient<?>> coefficients() {
        return coefficientMap.values().stream();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractModel.class.getSimpleName() + "[", "]")
                .add("coefficientMap=" + coefficientMap)
                .toString();
    }
}
