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

import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.List;

public abstract class AbstractScalarFunction extends AbstractFunction<Scalar> implements ScalarFunction {
    public AbstractScalarFunction(final FunctionType functionType, final List<Parameter<?>> parameterList) {
        super(functionType, parameterList);
    }

    public AbstractScalarFunction(final FunctionType functionType, final Parameter<?>... parameters) {
        super(functionType, parameters);
    }

    public AbstractScalarFunction(final FunctionType functionType, final String variable, final List<Parameter<?>> parameterList) {
        super(functionType, variable, parameterList);
    }

    public AbstractScalarFunction(final FunctionType functionType, final String variable, final Parameter<?>... parameters) {
        super(functionType, variable, parameters);
    }
}