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

package io.xmljim.algorithms.functions.common.provider;

import io.xmljim.algorithms.functions.common.FunctionException;
import io.xmljim.algorithms.model.provider.ModelProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFunctionFactory implements FunctionFactory {

    private ModelProvider modelProvider;
    private Map<String, FunctionProvider<?>> functionProviders = new HashMap<>();

    public AbstractFunctionFactory() {
        Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
        try {
            modelProvider = modelProviders.iterator().next();
        } catch (NoSuchElementException nse) {
            throw new FunctionException("No Model Provider Found");
        }
    }

    @Override
    public ModelProvider getModelProvider() {
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
