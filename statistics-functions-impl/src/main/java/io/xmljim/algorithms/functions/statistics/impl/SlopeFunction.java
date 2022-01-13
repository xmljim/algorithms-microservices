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

class SlopeFunction extends AbstractScalarFunction {
    private Scalar result;

    public SlopeFunction(ScalarFunctionParameter covariance, ScalarFunctionParameter varianceX) {
        super(StatisticsFunctionTypes.SLOPE, covariance, varianceX);
    }

    private Scalar computeSlope() {
        ScalarFunction varianceX = getValue(StatisticsFunctionTypes.VARIANCE.getName(), "x");
        ScalarFunction covariance = getValue(StatisticsFunctionTypes.COVARIANCE.getName());

        double slope =  covariance.compute().asDouble() / varianceX.compute().asDouble();
        return Scalar.of(slope);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeSlope();
        }

        return result;
    }
}
