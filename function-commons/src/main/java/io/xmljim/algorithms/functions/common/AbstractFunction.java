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

import io.xmljim.algorithms.model.Function;
import io.xmljim.algorithms.model.Parameter;

import java.util.List;

public abstract class AbstractFunction<T> extends ExtendedParameterized implements Function<T> {

    private FunctionType functionType;

    public AbstractFunction(FunctionType functionType, final List<Parameter<?>> parameterList) {
        super(functionType.getName(), parameterList);
        this.functionType = functionType;
    }

    public AbstractFunction(FunctionType functionType, final Parameter<?>... parameters) {
        super(functionType.getName(), parameters);
        this.functionType = functionType;
    }

    public AbstractFunction(FunctionType functionType, final String variable, final List<Parameter<?>> parameterList) {
        super(functionType.getName(), variable, parameterList);
        this.functionType = functionType;
    }

    public AbstractFunction(FunctionType functionType, final String variable, final Parameter<?>... parameters) {
        super(functionType.getName(), variable, parameters);
        this.functionType = functionType;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

}
