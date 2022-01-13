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

package io.xmljim.algorithms.functions.statistics.impl;

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.provider.ModelProvider;
import io.xmljim.algorithms.model.util.Scalar;
import org.junit.jupiter.api.*;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Create a function to calculate the covariance")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CovarianceFunctionTest {

    ScalarVector vectorX;
    ScalarVector vectorY;
    StatisticsProvider functionProvider;
    ModelProvider modelProvider;
    ScalarFunction meanFunction;

    @BeforeEach
    @DisplayName("is created from a FunctionProvider")
    void createFunctionProvider() {
        Iterable<FunctionProvider> functionProviders = ServiceLoader.load(FunctionProvider.class);
        functionProvider = (StatisticsProvider) functionProviders.iterator().next();

        Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
        modelProvider = modelProviders.iterator().next();

        vectorX = modelProvider.getVectorFactory().createScalarVector("vector", "x", 1,2,3,4,5);
        vectorY = modelProvider.getVectorFactory().createScalarVector("vector", "y", 73, 62, 81, 88, 90);
    }

    @Test
    @DisplayName("1. A FunctionProvider is initialized")
    @Order(1)
    void test1FunctionProviderExists() {
        assertNotNull(functionProvider);
        assertEquals("Statistics", functionProvider.getProviderName());
    }

    @Test
    @DisplayName("2. A Statistics factory is initialized from FunctionProvider")
    @Order(2)
    void test2StatisticsFactoryInitialized() {
        assertNotNull(functionProvider.getFactory());
    }

    @Test
    @DisplayName("3. A scalar vectors (x and y) are initialized")
    @Order(3)
    void test3VectorValues() {
        assertNotNull(vectorX);
        assertNotNull(vectorY);
    }

    @Test
    @DisplayName("4. The covariance function is initialized and value computed with vectors x and y")
    @Order(4)
    void compute() {
        ScalarFunction varianceFunction = functionProvider.getFactory().covariance(vectorX, vectorY);
        Scalar value = varianceFunction.compute();
        assertEquals(15.0, value.asDouble());
    }

    @Test
    @DisplayName("5. Covariance function initialized with vectors x and y and mean functions for x and y")
    @Order(5)
    void computeWithMeanFunction() {
        ScalarFunction meanXFunction = functionProvider.getFactory().mean(vectorX);
        ScalarFunction meanYFunction = functionProvider.getFactory().mean(vectorY);
        ScalarFunction varianceFunction = functionProvider.getFactory().covariance(vectorX, vectorY, meanXFunction, meanYFunction);
        Scalar value = varianceFunction.compute();
        assertEquals(15.0, value.asDouble());
    }
}