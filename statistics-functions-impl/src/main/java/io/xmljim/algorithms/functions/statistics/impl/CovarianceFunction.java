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


import io.xmljim.algorithms.functions.common.FunctionException;
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.Optional;
import java.util.stream.IntStream;

class CovarianceFunction extends AbstractScalarFunction {
    private Scalar result;

    public CovarianceFunction(ScalarVectorParameter vectorXParameter, ScalarVectorParameter vectorYParameter) {
        super(StatisticsFunctionTypes.COVARIANCE, vectorXParameter, vectorYParameter);
    }

    public CovarianceFunction(ScalarVectorParameter vectorXParameter, ScalarVectorParameter vectorYParameter, ScalarFunctionParameter meanX, ScalarFunctionParameter meanY) {
        super(StatisticsFunctionTypes.COVARIANCE, vectorXParameter, vectorYParameter, meanX, meanY);
    }

    private StatisticsProvider getFunctionProvider() {
        StatisticsProvider provider = getFunctionProvider("Statistics");
        return provider;
    }

    Scalar computeVariance() {
        ScalarVector vectorX = getVector(StatisticsNameConstants.X_VARIABLE);
        ScalarVector vectorY = getVector(StatisticsNameConstants.Y_VARIABLE);
        ScalarFunction meanXFunction = getOrCreateMeanFunction(vectorX, StatisticsNameConstants.X_VARIABLE);
        ScalarFunction meanYFunction = getOrCreateMeanFunction(vectorY, StatisticsNameConstants.Y_VARIABLE);

        double meanX = meanXFunction.compute().asDouble();
        double meanY = meanYFunction.compute().asDouble();

        double covariance = IntStream.range(0, (int)vectorX.length())
                .mapToDouble(i ->  (vectorX.get(i).asDouble() - meanX) * (vectorY.get(i).asDouble() - meanY))
                .sum() / (vectorX.length() - 1);

        return Scalar.of(covariance);
    }

    ScalarVector getVector(String variable) {
        ScalarVectorParameter vectorParameter =
                (ScalarVectorParameter) find(parameterVariableAndType(variable, ParameterTypes.SCALAR_VECTOR))
                        .orElseThrow(() -> new FunctionException("Expected to find ScalarVectorParameter for variable " + variable + ", but it was not present"));

        return vectorParameter.getValue();
    }

    ScalarFunction getOrCreateMeanFunction(ScalarVector vector, String variable) {
        ScalarFunction meanFunction;

        Optional<Parameter<?>> meanParam = find(parameterNameVariableType(StatisticsFunctionTypes.MEAN.getName(), variable, ParameterTypes.SCALAR_FUNCTION));

        if (meanParam.isPresent()) {
            meanFunction = ((ScalarFunctionParameter)meanParam.get()).getValue();
        } else {
            meanFunction = getFunctionProvider().getFactory().mean(vector, variable);
        }

        return meanFunction;
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeVariance();
        }
        return result;
    }
}
