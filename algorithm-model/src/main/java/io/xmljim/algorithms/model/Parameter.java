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
 * A parameter is an input value to a function or model
 * @param <T> The underlying type of the parameter value
 */
public interface Parameter<T> extends VariableEntity {
    /**
     * Return the parameter value
     * @return
     */
    T getValue();

    /**
     * Return the parameter type. The parameter type
     * provides consumers of this interface a hint to the underlying
     * data type of the parameter
     * @return the parameter type
     */
    ParameterTypes getParameterType();
}
