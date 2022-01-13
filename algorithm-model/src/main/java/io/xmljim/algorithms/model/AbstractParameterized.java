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


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractParameterized extends AbstractVariableEntity implements Parameterized {
    final List<Parameter<?>> parameterList;

    public AbstractParameterized(final String name, final List<Parameter<?>> parameterList) {
        super(name);
        this.parameterList = parameterList;
    }

    public AbstractParameterized(final String name, Parameter<?>... parameters) {
        super(name);
        this.parameterList = Arrays.stream(parameters).collect(Collectors.toList());
    }

    public AbstractParameterized(final String name, final String variable, final List<Parameter<?>> parameterList) {
        super(name, variable);
        this.parameterList = parameterList;
    }

    public AbstractParameterized(final String name, final String variable, Parameter<?>... parameters) {
        super(name, variable);
        this.parameterList = Arrays.stream(parameters).collect(Collectors.toList());
    }

    @Override
    public Parameter<?> getParameter(final int index) {
        return parameterList.get(index);
    }

    @Override
    public Stream<Parameter<?>> stream() {
        return parameterList.stream();
    }
}
