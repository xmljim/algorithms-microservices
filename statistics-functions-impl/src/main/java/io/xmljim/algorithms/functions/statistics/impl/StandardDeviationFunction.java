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
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

class StandardDeviationFunction extends AbstractScalarFunction {
    private Scalar result;

    StandardDeviationFunction(ScalarVectorParameter scalarVectorParameter) {
        super(StatisticsFunctionTypes.STANDARD_DEVIATION, scalarVectorParameter);
    }

    StandardDeviationFunction(ScalarVectorParameter scalarVectorParameter, String variable) {
        super(StatisticsFunctionTypes.STANDARD_DEVIATION, variable, scalarVectorParameter);
    }

    StandardDeviationFunction(ScalarFunctionParameter varianceFunctionParameter) {
        super(StatisticsFunctionTypes.STANDARD_DEVIATION, varianceFunctionParameter);
    }

    StandardDeviationFunction(ScalarFunctionParameter varianceFunctionParameter, String variable) {
        super(StatisticsFunctionTypes.STANDARD_DEVIATION, variable, varianceFunctionParameter);
    }

    private StatisticsProvider getFunctionProvider() {
        StatisticsProvider provider = getFunctionProvider("Statistics");
        return provider;
    }

    private Scalar computeStandardDeviation() {
        double variance = getVarianceFunction().compute().asDouble();
        return Scalar.of(Math.sqrt(variance));
    }

    private ScalarFunction getVarianceFunction() {
        ScalarFunction varianceFunction;

        if (getParameter(0).getParameterType() == ParameterTypes.SCALAR_FUNCTION) {
            varianceFunction = getValue(0);
        } else {
            ScalarVector vector = getValue(0);
            varianceFunction = getFunctionProvider().getFactory().variance(vector, getVariable());
        }

        return varianceFunction;
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeStandardDeviation();
        }

        return result;
    }
}
