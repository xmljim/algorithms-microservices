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

import io.xmljim.algorithms.functions.common.AbstractScalarFunction;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.ScalarFunctionParameter;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.ScalarVectorParameter;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.stream.IntStream;
/**
 * Calculate the residual sum of squares, also known as the Error Sum of Squares (SSE)
 */
class ResidualSumOfSquaresFunction extends AbstractScalarFunction {
    Scalar result;

    public ResidualSumOfSquaresFunction(ScalarVectorParameter vectorX, ScalarVectorParameter vectorY, ScalarFunctionParameter slopeFunction, ScalarFunctionParameter interceptFunction) {
        super(StatisticsFunctionTypes.SSE, vectorX, vectorY, slopeFunction, interceptFunction);
    }

    /**
     * σ2ŷ = Σ(ŷi - yi)2
     *
     * Where:
     *
     * ŷi = βxi + α
     * @return
     */
    private Scalar computeResidualVariance() {
        ScalarVector vectorX = getValue(StatisticsNameConstants.VECTOR, StatisticsNameConstants.X_VARIABLE);
        ScalarVector vectorY = getValue(StatisticsNameConstants.VECTOR, StatisticsNameConstants.Y_VARIABLE);
        ScalarFunction slopeFx = getValue(StatisticsFunctionTypes.SLOPE.getName());
        ScalarFunction interceptFx = getValue(StatisticsFunctionTypes.INTERCEPT.getName());

        double slope = slopeFx.compute().asDouble();
        double intercept = interceptFx.compute().asDouble();

        double residualVariance = IntStream.range(0, vectorX.length())
                .mapToDouble(i -> {
                    double yHat = slope * vectorX.get(i).asDouble() + intercept;
                    return Math.pow(yHat - vectorY.get(i).asDouble(), 2);
                }).sum();

        return Scalar.of(residualVariance);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeResidualVariance();
        }
        return result;
    }
}
