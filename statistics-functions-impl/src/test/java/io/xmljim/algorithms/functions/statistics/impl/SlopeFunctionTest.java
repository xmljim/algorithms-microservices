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
import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlopeFunctionTest {
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
    @DisplayName("Load data and compute slope")
    void compute() throws IOException {
        long parsingTime = 0;
        long listX = 0;
        long listY = 0;
        long vectX = 0;
        long vectY = 0;
        long compute = 0;

        ScalarVector yearVector;
        ScalarVector logAdjustedNetGainVector;

        long start = System.currentTimeMillis();
        try (InputStream inputStream = getClass().getResourceAsStream("/stockmarket.json")) {
            JSONFactory factory = JSONFactory.newFactory();
            JSONParser parser = factory.newParser();
            JSONObject jsonObject = parser.parse(inputStream).asJSONObject();
            long parsingEnd = System.currentTimeMillis();
            parsingTime = parsingEnd - start;

            long listXStart = System.currentTimeMillis();
            JSONArray yearArray = jsonObject.getJSONArray("year");
            List<Number> numbers = new ArrayList<>();
            for (JSONValue<?> value : yearArray) {
                numbers.add((Number)value.getValue());
            }
            long listXEnd = System.currentTimeMillis();
            listX = listXEnd - listXStart;

            long listYStart = System.currentTimeMillis();
            JSONArray logAdjustedArray = jsonObject.getJSONArray("netLogGainAdjusted");
            List<Number> logList = new ArrayList<>();
            for (JSONValue<?> value : logAdjustedArray) {
                logList.add((Number)value.getValue());
            }
            long listYEnd = System.currentTimeMillis();
            listY = listYEnd - listYStart;


            long vectXStart = System.currentTimeMillis();
            yearVector = modelProvider.getVectorFactory().createScalarVector("year", "x" , numbers);
            long vectXEnd = System.currentTimeMillis();
            vectX = vectXEnd - vectXStart;

            long vectYStart = System.currentTimeMillis();
            logAdjustedNetGainVector = modelProvider.getVectorFactory().createScalarVector("netLogGainAdjusted", "y", logList);
            long vectYEnd = System.currentTimeMillis();
            vectY = vectYEnd - vectXStart;

        }

        ScalarFunction meanX = functionProvider.getFactory().mean(yearVector);
        ScalarFunction meanY = functionProvider.getFactory().mean(logAdjustedNetGainVector);
        ScalarFunction varianceX = functionProvider.getFactory().variance(yearVector, meanX, "x");
        ScalarFunction covariance = functionProvider.getFactory().covariance(yearVector, logAdjustedNetGainVector, meanX, meanY);
        ScalarFunction slope = functionProvider.getFactory().slope(varianceX, covariance);

        long computeStart = System.currentTimeMillis();
        Scalar slopeValue = slope.compute();
        long computeEnd = System.currentTimeMillis();
        compute = computeEnd - computeStart;
        assertEquals(0.03398, slopeValue.asDouble(), .00001);
        System.out.println(slopeValue);

        System.out.println("Parsing Time: " + parsingTime);
        System.out.println("List X Time: " + listX);
        System.out.println("List Y Time: " + listY);
        System.out.println("Vector X Time: " + vectX);
        System.out.println("Vector Y Time: " + vectY);
        System.out.println("Compute Time: " + compute);

    }
}
