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

class VarianceFunction extends AbstractScalarFunction {
    private Scalar result;

    public VarianceFunction(ScalarVectorParameter vectorParameter) {
        super(StatisticsFunctionTypes.VARIANCE, vectorParameter);
    }

    public VarianceFunction(ScalarVectorParameter vectorParameter, String variable) {
        super(StatisticsFunctionTypes.VARIANCE, variable, vectorParameter);
    }

    public VarianceFunction(ScalarVectorParameter vectorParameter, ScalarFunctionParameter meanFunction) {
        super(StatisticsFunctionTypes.VARIANCE, vectorParameter, meanFunction);
    }

    public VarianceFunction(ScalarVectorParameter vectorParameter, ScalarFunctionParameter meanFunction, String variable) {
        super(StatisticsFunctionTypes.VARIANCE, variable, vectorParameter, meanFunction);
    }

    private StatisticsProvider getFunctionProvider() {
        StatisticsProvider provider = getFunctionProvider("Statistics");
        return provider;
    }

    private Scalar computeVariance() {
        ScalarVectorParameter vectorParameter = (ScalarVectorParameter) find(parameterType(ParameterTypes.SCALAR_VECTOR))
                .orElseThrow(() -> new FunctionException("Missing ScalarVector parameter"));

        ScalarVector vector = vectorParameter.getValue();
        ScalarFunction meanFunction = getOrCreateMeanFunction(vector);
        double mean = meanFunction.compute().asDouble();

        double variance = vector.stream().mapToDouble(e -> Math.pow(e.asDouble() - mean, 2)).sum() / (vector.length() - 1);
        return Scalar.of(variance);
    }

    private ScalarFunction getOrCreateMeanFunction(ScalarVector vector) {
        ScalarFunction meanFunction;

        Optional<Parameter<?>> meanFunctionParameter = find(parameterNameAndType(StatisticsFunctionTypes.MEAN.getName(), ParameterTypes.SCALAR_FUNCTION));
        if (meanFunctionParameter.isPresent()) {
            meanFunction = (ScalarFunction) meanFunctionParameter.get().getValue();
        } else {
            meanFunction = getFunctionProvider().getFactory().mean(vector, getVariable());
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
