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
import io.xmljim.algorithms.functions.statistics.LinearRegressionModel;
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.fail;

public class LinearRegressionModelImplTest {
    static StatisticsProvider functionProvider;
    static ModelProvider modelProvider;


    @BeforeAll
    @DisplayName("is created from a FunctionProvider")
    static void createFunctionProvider() {
        Iterable<FunctionProvider> functionProviders = ServiceLoader.load(FunctionProvider.class);
        functionProvider = (StatisticsProvider) functionProviders.iterator().next();

        Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
        modelProvider = modelProviders.iterator().next();
    }

    @Test
    void solve() {

        ScalarVector vectorX = null;
        ScalarVector vectorY = null;
        try (InputStream inputStream = getClass().getResourceAsStream("/stockmarket.json")) {
            JSONFactory factory = JSONFactory.newFactory();
            JSONParser parser = factory.newParser();
            JSONObject jsonObject = parser.parse(inputStream).asJSONObject();
            long parsingEnd = System.currentTimeMillis();

            JSONArray xArray = jsonObject.getJSONArray("year");
            List<Number> listX = new ArrayList<>();
            for (JSONValue<?> value : xArray) {
                listX.add((Number)value.getValue());
            }

            JSONArray yArray = jsonObject.getJSONArray("netLogGainAdjusted");
            List<Number> listY = new ArrayList<>();
            for (JSONValue<?> value : yArray) {
                listY.add((Number)value.getValue());
            }

            vectorX = modelProvider.getVectorFactory().createScalarVector(StatisticsNameConstants.VECTOR, StatisticsNameConstants.X_VARIABLE, listX);
            vectorY = modelProvider.getVectorFactory().createScalarVector(StatisticsNameConstants.VECTOR, StatisticsNameConstants.Y_VARIABLE, listY);
        } catch (Exception e) {
            fail(e);

        }

        LinearRegressionModel lrm = functionProvider.getFactory().linearRegression(vectorX, vectorY);
        lrm.solve();

        lrm.coefficients().forEach(c -> {
            System.out.println(c.toString());
        });

        System.out.println(Math.pow(10, lrm.getSlope().asDouble()));
    }
}
