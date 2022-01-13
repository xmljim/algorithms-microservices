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

import io.xmljim.algorithms.functions.common.FunctionType;

enum StatisticsFunctionTypes implements FunctionType {
    SUM("sum", "SUM"),
    MEAN("mean", "MEAN"),
    MEDIAN("median", "MEDIAN"),
    VARIANCE("variance", "VAR"),
    STANDARD_DEVIATION("standardDeviation", "STDDEV"),
    COVARIANCE("covariance", "COV"),
    SLOPE("slope", "B"),
    INTERCEPT("intercept", "a"),
    SSE("residualsumOfSquares", "SSR"),
    SST("totalSumOfSquares", "SST"),
    R_SQUARED("rSquared", "R-SQUARED"),
    SLOPE_STD_ERROR("slopeStandardError", "SE Slope"),
    INTERCEPT_STD_ERROR("interceptStandardError", "SE Intercept"),
    MSE("meanSquaredError", "MSE"),
    LINEAR_REGRESSION_MODEL("linearRegressionModel", "LRM"),
    T_SLOPE("TSlope", "t (slope)"),
    T_INTERCEPT("TIntercept", "t (intercept)"),
    P_SLOPE("PSlope", "p (slope)")
    ;

    private final String name;
    private final String label;

    StatisticsFunctionTypes(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
