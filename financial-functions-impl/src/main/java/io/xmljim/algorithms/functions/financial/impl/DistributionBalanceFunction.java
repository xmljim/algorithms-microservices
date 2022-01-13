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

import io.xmljim.algorithms.functions.common.AbstractFunction;
import io.xmljim.algorithms.functions.financial.DistributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarParameter;

class DistributionBalanceFunction extends AbstractFunction<DistributionBalance> {
    private DistributionBalance result;

    /**
     *
     * @param currentBalance
     * @param amortizedValue
     * @param inflation
     * @param retirementInterest
     * @param retirementYear
     * @param currentYear
     */
    public DistributionBalanceFunction(ScalarParameter currentBalance, ScalarParameter amortizedValue, ScalarParameter inflation,
                                       ScalarParameter retirementInterest, ScalarParameter retirementYear, ScalarParameter currentYear,
                                       Parameter<PaymentFrequency> paymentFrequency) {

        super(FinancialFunctionTypes.DISTRIBUTION_BALANCE_FUNCTION, currentBalance, amortizedValue, inflation, retirementInterest, retirementYear, currentYear, paymentFrequency);
    }

    private DistributionBalance getResult() {
        double currentBalance = getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE);
        double annualizedBase = getDouble(FinancialNameConstants.FIN_ANNUAL_DISTRIBUTION);
        double inflation = getDouble(FinancialNameConstants.FIN_INFLATION_RATE);
        double retirementInterestRate = getDouble(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE);
        int retirementYear = getInteger(FinancialNameConstants.FIN_RETIREMENT_START_YEAR);
        int currentYear = getInteger(FinancialNameConstants.FIN_CURRENT_YEAR);
        PaymentFrequency frequency = getValue(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY);



        double estDistribution = annualizedBase * Math.pow(1 + inflation, currentYear - retirementYear);

        double realDistribution = 0.0;
        double newBalance = 0.0;
        double interest = 0.0;
        if (estDistribution < currentBalance) {
            interest = (currentBalance - estDistribution) * retirementInterestRate;
            realDistribution = estDistribution;

        } else {
            realDistribution = currentBalance;
        }

        newBalance = (currentBalance - realDistribution) + interest;

        return new DistributionBalanceImpl(currentYear, newBalance, interest, retirementInterestRate, realDistribution, inflation, frequency);
    }


    @Override
    public DistributionBalance compute() {
        if (result == null) {
            result = getResult();
        }

        return result;
    }
}
