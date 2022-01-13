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

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.model.AbstractParameterized;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ParameterTypes;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.provider.ModelProvider;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ExtendedParameterized extends AbstractParameterized {
    private ModelProvider modelProvider;

    private Map<String, FunctionProvider<?>> functionProviders = new HashMap<>();

    public ExtendedParameterized(final String name, final List<Parameter<?>> parameterList) {
        super(name, parameterList);
    }

    public ExtendedParameterized(final String name, final Parameter<?>... parameters) {
        super(name, parameters);
    }

    public ExtendedParameterized(final String name, final String variable, final List<Parameter<?>> parameterList) {
        super(name, variable, parameterList);
    }

    public ExtendedParameterized(final String name, final String variable, final Parameter<?>... parameters) {
        super(name, variable, parameters);
    }

    /**
     * Get an integer value from either a {@link io.xmljim.algorithms.model.ScalarParameter} or {@link io.xmljim.algorithms.model.ScalarFunctionParameter}
     * parameter
     * @param paramName The parameter name
     * @return the integer value from the parameter
     * @throws FunctionException thrown if the parameter does not exist, or cannot be cast to an integer
     */
    public int getInteger(String paramName) {
        ParameterTypes parameterType = getParameterType(paramName).orElseThrow(() -> new FunctionException("No parameter with name " + paramName + " found"));
        if (ParameterTypes.SCALAR.equals(parameterType)) {
            return ((Scalar)getValue(paramName)).asInt();
        } else if (ParameterTypes.SCALAR_FUNCTION.equals(parameterType)) {
            return ((ScalarFunction)getValue(paramName)).compute().asInt();
        } else {
            throw new FunctionException("Invalid parameter cast: cannot return an integer from a " + parameterType.name());
        }
    }

    /**
     * Get an double value from either a {@link io.xmljim.algorithms.model.ScalarParameter} or {@link io.xmljim.algorithms.model.ScalarFunctionParameter}
     * parameter
     * @param paramName The parameter name
     * @return the integer value from the parameter
     * @throws FunctionException thrown if the parameter does not exist, or cannot be cast to a double
     */
    public double getDouble(String paramName) {
        ParameterTypes parameterType = getParameterType(paramName).orElseThrow(() -> new FunctionException("No parameter with name " + paramName + " found"));
        if (ParameterTypes.SCALAR.equals(parameterType)) {
            return ((Scalar)getValue(paramName)).asDouble();
        } else if (ParameterTypes.SCALAR_FUNCTION.equals(parameterType)) {
            return ((ScalarFunction)getValue(paramName)).compute().asDouble();
        } else {
            throw new FunctionException("Invalid parameter cast: cannot return an integer from a " + parameterType.name());
        }
    }

    public ModelProvider getModelProvider() {
        if (modelProvider == null) {
            Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
            modelProvider = modelProviders.iterator().next();
        }

        return modelProvider;
    }

    public <T extends FunctionProvider<?>> T getFunctionProvider(String providerName) {
        final AtomicReference<FunctionProvider<?>> functionProvider = new AtomicReference<>();

        if (functionProviders.containsKey(providerName)) {
            functionProvider.set(functionProviders.get(providerName));
        } else {
            Iterable<FunctionProvider> providers = ServiceLoader.load(FunctionProvider.class);

            providers.forEach(fp -> {
                if (fp.getProviderName().equals(providerName)) {
                    functionProvider.set(fp);
                    functionProviders.put(providerName, fp);
                }
            });
        }

        return (T) functionProvider.get();
    }

}
