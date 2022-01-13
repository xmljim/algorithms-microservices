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
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarParameter;
import io.xmljim.algorithms.model.util.Scalar;

class AmortizeFunction extends AbstractScalarFunction {
    private Scalar result;

    //final double balance, final double interest, PaymentFrequency frequency, final int durationYear, final double inflation, final double retirementSTartYear

    public AmortizeFunction(ScalarParameter balance, ScalarParameter interest, Parameter<PaymentFrequency> frequencyParameter, ScalarParameter duration) {
        super(FinancialFunctionTypes.AMORTIZE, balance, interest, frequencyParameter, duration);
    }

    private Scalar getResult() {
        double amount = getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE);
        double interest = getDouble(FinancialNameConstants.FIN_POST_RETIRE_INTEREST);
        PaymentFrequency frequency = getValue(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY);
        int duration = getInteger(FinancialNameConstants.FIN_RETIREMENT_DURATION);

        double intervalRate = interest / frequency.getAnnualFrequency();
        int totalDistributions = duration * frequency.getAnnualFrequency();

        double value = amount / ((Math.pow(1 + intervalRate, totalDistributions) - 1) / (intervalRate * Math.pow(1 + intervalRate, totalDistributions)));

        return Scalar.of(value);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = getResult();
        }

        return result;
    }
}
