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
import io.xmljim.algorithms.model.ScalarParameter;
import io.xmljim.algorithms.model.util.Scalar;

/**
 * Compute the standard error of the intercept
 * <p>
 *     <pre>
 *         s<sub>&#x03B1;</sub> = sqrt(MSE * ( 1/n + (x&#x0304;<sup>2</sup> / SS<sub>x</sub>)))
 *
 *         Where:
 *         MSE (mean squared error) = &#x03A3;(&#x177;<sub>i</sub> - y<sub>i</sub>)<sup>2</sup> / (n - 2)
 *         SS<sub>x</sub> (sum of squares of x) = &#x03A3;(x<sub>i</sub> - x&#x0304;)<sup>2</sup>
 *     </pre>
 * </p>
 */
class InterceptStandardErrorFunction extends AbstractScalarFunction {

    private Scalar result;

    public InterceptStandardErrorFunction(ScalarFunctionParameter meanSquaredErrorFunction, ScalarFunctionParameter meanXFunction, ScalarFunctionParameter sumOfSquaresX,
                                          ScalarParameter countParameter) {
        super(StatisticsFunctionTypes.INTERCEPT_STD_ERROR, meanSquaredErrorFunction, meanXFunction, sumOfSquaresX, countParameter);
    }

    private Scalar getResult() {
        ScalarFunction mseFx = getValue(StatisticsFunctionTypes.MSE.getName());
        ScalarFunction meanXFx = getValue(StatisticsFunctionTypes.MEAN.getName(), StatisticsNameConstants.X_VARIABLE);
        ScalarFunction sumSquaresXFx = getValue(StatisticsFunctionTypes.SST.getName(), StatisticsNameConstants.X_VARIABLE);
        Scalar countVar = getValue(StatisticsNameConstants.COUNT);

        double mse = mseFx.compute().asDouble();
        double meanX = meanXFx.compute().asDouble();
        double sumSquaresX = sumSquaresXFx.compute().asDouble();
        int count = countVar.asInt();

        double interceptVariance = mse * ((1 / count) +  (Math.pow(meanX, 2) / sumSquaresX));
        double seIntercept = Math.sqrt(interceptVariance);
        return Scalar.of(seIntercept);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = getResult();
        }

        return result;
    }
}
