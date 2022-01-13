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
import org.apache.commons.math3.distribution.TDistribution;

class PSlopeFunction extends AbstractScalarFunction {
    private Scalar result;

    public PSlopeFunction(ScalarParameter degreesOfFreedom, ScalarFunctionParameter tStatisticParameter) {
        super(StatisticsFunctionTypes.P_SLOPE, degreesOfFreedom, tStatisticParameter);
    }

    Scalar getResult() {
        Scalar dfValue = getValue(StatisticsNameConstants.DEGREES_OF_FREEDOM);
        ScalarFunction tStatFunction = getValue(StatisticsFunctionTypes.T_SLOPE.getName());
        double tStat = tStatFunction.compute().asDouble();

        TDistribution tDistribution = new TDistribution(dfValue.asDouble());
        double cp = tDistribution.cumulativeProbability(Math.abs(tStat));
        double p = 2 * (1 - cp);
        return Scalar.of(p);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = getResult();
        }
        return result;
    }
}
