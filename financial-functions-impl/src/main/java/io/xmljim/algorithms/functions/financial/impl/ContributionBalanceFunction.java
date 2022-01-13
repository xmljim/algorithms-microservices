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
import io.xmljim.algorithms.functions.financial.ContributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarParameter;
import io.xmljim.algorithms.model.util.Scalar;
import java.time.LocalDate;

class ContributionBalanceFunction extends AbstractFunction<ContributionBalance> {
    private ContributionBalance result;

    public ContributionBalanceFunction(ScalarParameter currentSalary, ScalarParameter colaPct, ScalarParameter currentRetirementBalance,
                                       ScalarParameter employeeContribution, ScalarParameter employerContribution, ScalarParameter weightedGrowth,
                                       Parameter<PaymentFrequency> contributionFrequency, ScalarParameter currentYear, ScalarParameter endYear) {

        super(FinancialFunctionTypes.CONTRIBUTION_BALANCE_FUNCTION, currentSalary, colaPct, currentRetirementBalance,
                employeeContribution, employerContribution, weightedGrowth, contributionFrequency, currentYear, endYear);

    }


    public ContributionBalanceFunction(Parameter<ContributionBalance> contributionBalanceParameter, ScalarParameter forYear) {
        super(FinancialFunctionTypes.CONTRIBUTION_BALANCE_FUNCTION, contributionBalanceParameter, forYear);
        this.result = computeFromPreviousBalance(contributionBalanceParameter.getValue(), forYear.getValue());
    }

    private ContributionBalance createFromParameters() {
        int currentYear = getInteger(FinancialNameConstants.FIN_CURRENT_YEAR);
        double currentBalance = getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE);
        double interest = 0.0; //no interest computed yet
        double weightedGrowthRate = getDouble(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE);
        double selfContributionPct = getDouble(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT);
        double emplContributionPct = getDouble(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT);
        double currentSalary = getDouble(FinancialNameConstants.FIN_CURRENT_SALARY);
        double colaPct = getDouble(FinancialNameConstants.FIN_COLA_PCT);
        PaymentFrequency contributionFrequency = getValue(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY);
        double estimatedSelfContribution = 0.0;
        double estimatedEmplContribution = 0.0;

        return new ContributionBalanceImpl(currentYear, currentBalance, interest, weightedGrowthRate,
                selfContributionPct, emplContributionPct, currentSalary, colaPct, estimatedSelfContribution, estimatedEmplContribution, contributionFrequency);
    }

    private ContributionBalance computeFromPreviousBalance(ContributionBalance balance, Scalar endYearValue) {
        ContributionBalance estimatedBalance;

        if (LocalDate.now().getYear() == endYearValue.asInt()) {
            estimatedBalance = preProcessCurrentContribution(balance);
        } else {
            estimatedBalance = incrementBalance(balance);
        }

        return estimatedBalance;
    }

    private ContributionBalance preProcessCurrentContribution(ContributionBalance contributionBalance) {
        double periodicSelfContributionAmt = (contributionBalance.getCurrentSalary() * contributionBalance.getSelfContributionPct()) / contributionBalance.getContributionFrequency().getAnnualFrequency();
        double periodicEmplContributionAmt = (contributionBalance.getCurrentSalary() * contributionBalance.getEmployerContributionPct()) / contributionBalance.getContributionFrequency().getAnnualFrequency();

        LocalDate now = LocalDate.now();
        int dayOfYear = now.getDayOfYear();
        int yearLength = now.lengthOfYear();
        double pctYearRemaining = 1 - ((double)dayOfYear/(double)yearLength);
        double remainingContributions = Math.floor(pctYearRemaining * contributionBalance.getContributionFrequency().getAnnualFrequency());

        double newSelfContribution = remainingContributions * periodicSelfContributionAmt;
        double newEmplContribution = remainingContributions * periodicEmplContributionAmt;

        double newInterest = contributionBalance.getBalance() * (contributionBalance.getWeightedGrowthRate() * pctYearRemaining);
        double newBalance = contributionBalance.getBalance() + newInterest + newSelfContribution + newEmplContribution;
        return new ContributionBalanceImpl(contributionBalance.getYear(), newBalance, newInterest, contributionBalance.getWeightedGrowthRate(), contributionBalance.getSelfContributionPct(),
                contributionBalance.getEmployerContributionPct(), contributionBalance.getCurrentSalary(), contributionBalance.getColaPct(),
                newSelfContribution, newEmplContribution, contributionBalance.getContributionFrequency());
    }

    private ContributionBalance incrementBalance(ContributionBalance balance) {
        int endYear = balance.getYear() + 1;

        double estSalary = balance.getCurrentSalary() * (1 + balance.getColaPct());
        double estInterest = balance.getBalance() * balance.getWeightedGrowthRate();
        double estSelfContrib = estSalary * balance.getSelfContributionPct();
        double estEmpContrib = estSalary * balance.getEmployerContributionPct();
        double estBalance = balance.getBalance() + estInterest + estSelfContrib + estEmpContrib;

        return new ContributionBalanceImpl(endYear, estBalance, estInterest, balance.getWeightedGrowthRate(), balance.getSelfContributionPct(),
                balance.getEmployerContributionPct(), estSalary, balance.getColaPct(), estSelfContrib, estEmpContrib, balance.getContributionFrequency());
    }


    private ContributionBalance getResult() {
        ContributionBalance balance = createFromParameters();
        Scalar endYear = getValue(FinancialNameConstants.FIN_END_YEAR);

        return computeFromPreviousBalance(balance, endYear);
    }


    @Override
    public ContributionBalance compute() {
        //only want to compute once
        if (result == null) {
            result = getResult();
        }

        return result;
    }
}
