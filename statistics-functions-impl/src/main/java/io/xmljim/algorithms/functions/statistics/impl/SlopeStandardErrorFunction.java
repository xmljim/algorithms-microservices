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
import io.xmljim.algorithms.model.util.Scalar;

/**
 * Function for calculating the standard error of the slope (estimate).
 *
 * <p>
 *    <pre>
 *        s<sub>&#x03B2;</sub> = sqrt(MSE) / sqrt(SS<sub>x</sub>)
 *
 *        Where:
 *
 *        MSE (mean squared error) = &#x03A3;(&#x177;<sub>i</sub> - y<sub>i</sub>)<sup>2</sup> / (n - 2)
 *        SS<sub>x</sub> (sum of squares of x) = &#x03A3;(x<sub>i</sub> - x&#x0304;)<sup>2</sup>
 *    </pre>
 *
 * </p>
 */
class SlopeStandardErrorFunction extends AbstractScalarFunction {
    private Scalar result;

    public SlopeStandardErrorFunction(ScalarFunctionParameter sumOfSquaresX, ScalarFunctionParameter meanSquaredErrorY) {
        super(StatisticsFunctionTypes.SLOPE_STD_ERROR, sumOfSquaresX, meanSquaredErrorY);
    }

    private Scalar computeSlopeStandardError() {
        ScalarFunction sumOfSquaresXFx = getValue(StatisticsFunctionTypes.SST.getName(), StatisticsNameConstants.X_VARIABLE);
        ScalarFunction meanSquareErrorYFx = getValue(StatisticsFunctionTypes.MSE.getName());
        double sumOfSquaresX = sumOfSquaresXFx.compute().asDouble();
        double meanSquareErrorY = meanSquareErrorYFx.compute().asDouble();

        return Scalar.of(Math.sqrt(meanSquareErrorY) / Math.sqrt(sumOfSquaresX));
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeSlopeStandardError();
        }

        return result;
    }
}
