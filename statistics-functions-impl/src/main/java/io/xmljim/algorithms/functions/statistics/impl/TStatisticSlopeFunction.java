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

class TStatisticSlopeFunction extends AbstractScalarFunction {
    private Scalar result;

    public TStatisticSlopeFunction(ScalarFunctionParameter slopeFunction, ScalarFunctionParameter slopeStandardError) {
        super(StatisticsFunctionTypes.T_SLOPE, slopeFunction, slopeStandardError);
    }

    Scalar getResult() {
        ScalarFunction slopeFx = getValue(StatisticsFunctionTypes.SLOPE.getName());
        ScalarFunction slopeSEFx = getValue(StatisticsFunctionTypes.SLOPE_STD_ERROR.getName());

        double slope = slopeFx.compute().asDouble();
        double slopeSE = slopeSEFx.compute().asDouble();
        return Scalar.of(slope / slopeSE);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = getResult();
        }

        return result;
    }
}
