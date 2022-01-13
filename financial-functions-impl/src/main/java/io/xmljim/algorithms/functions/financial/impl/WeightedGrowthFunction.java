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

package io.xmljim.algorithms.functions.financial.impl;

import io.xmljim.algorithms.functions.common.AbstractScalarFunction;
import io.xmljim.algorithms.model.ScalarParameter;
import io.xmljim.algorithms.model.util.Scalar;

class WeightedGrowthFunction extends AbstractScalarFunction {
    public WeightedGrowthFunction(ScalarParameter stockGrowthRateParam, ScalarParameter treasuryYieldParam, ScalarParameter proportionStocks) {
        super(FinancialFunctionTypes.WEIGHTED_GROWTH, stockGrowthRateParam, treasuryYieldParam, proportionStocks);
    }

    @Override
    public Scalar compute() {
        double stockRate = ((Scalar)getValue(FinancialNameConstants.FIN_STOCK_GROWTH_RATE)).asDouble();
        double treasuryYield = ((Scalar)getValue(FinancialNameConstants.FIN_TREASURY_YIELD)).asDouble();
        double investmentRatio = ((Scalar)getValue(FinancialNameConstants.FIN_INVESTMENT_RATIO)).asDouble();

        return Scalar.of((stockRate * investmentRatio) + (treasuryYield * (1 - investmentRatio)));
    }
}
