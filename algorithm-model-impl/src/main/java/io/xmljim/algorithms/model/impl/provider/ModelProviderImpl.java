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

import io.xmljim.algorithms.model.provider.*;
import io.xmljim.algorithms.model.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ModelProviderImpl implements ModelProvider {
    private final ParameterFactory parameterFactory = new ParameterFactoryImpl();
    private final VectorFactory vectorFactory = new VectorFactoryImpl();
    private final MatrixFactory matrixFactory = new MatrixFactoryImpl(this);
    private final CoefficientFactory coefficientFactory = new CoefficientFactoryImpl();

    @Override
    public ParameterFactory getParameterFactory() {
        return parameterFactory;
    }

    @Override
    public VectorFactory getVectorFactory() {
        return vectorFactory;
    }

    @Override
    public MatrixFactory getMatrixFactory() {
        return matrixFactory;
    }

    @Override
    public CoefficientFactory getCoefficientFactory() {
        return coefficientFactory;
    }

    @Override
    public Version getProviderVersion() {
        try (InputStream inputStream = getClass().getResourceAsStream("/algorithm-model-build.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Version version = new Version(properties.getProperty("build.version"));
            return version;
        } catch (IOException ioe) {
            return new Version();
        }
    }
}
