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

import io.xmljim.algorithms.functions.common.AbstractModel;
import io.xmljim.algorithms.functions.financial.ContributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.functions.financial.RetirementContributionModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

class RetirementContributionModelImpl extends AbstractModel implements RetirementContributionModel {


    public RetirementContributionModelImpl(ScalarParameter currentAge, ScalarParameter retirementAge, ScalarParameter currentSalary,
                                           ScalarParameter employeeContribution, ScalarParameter employerContribution,
                                           ScalarParameter currentBalance, ScalarParameter colaPct, ScalarParameter weightedGrowthRate,
                                           Parameter<PaymentFrequency> contributionFrequency) {

        super(FinancialFunctionTypes.RETIREMENT_CONTRIBUTION_MODEL.getName(), currentAge, retirementAge, currentSalary, employeeContribution,
                employerContribution, currentBalance, colaPct, weightedGrowthRate, contributionFrequency);

    }

    private FinancialProvider getFunctionProvider() {
        FinancialProvider provider = getFunctionProvider("Financial");
        return provider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Coefficient<List<ContributionBalance>> getContributionTimelineCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.CONTRIBUTION_SCHEDULE.getName());
        return (Coefficient<List<ContributionBalance>>) coeff;
    }

    @Override
    public ScalarCoefficient getBalanceCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.CONTRIBUTION_BALANCE.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalInterestCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_INTEREST.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalSelfContributionCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_SELF_CONTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalEmployerContributionCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_EMPL_CONTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getLastSalaryCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.LAST_SALARY.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public void solve() {
        List<ContributionBalance> balanceSchedule = new ArrayList<>();

        int currentAge = getInteger(FinancialNameConstants.FIN_AGE);
        int retirementAge = getInteger(FinancialNameConstants.FIN_RETIREMENT_AGE);
        double currentSalary = getDouble(FinancialNameConstants.FIN_CURRENT_SALARY);
        double selfContributionPct = getDouble(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT);
        double emplContributionPct = getDouble(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT);
        double currentBalance = getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE);
        double colaPct = getDouble(FinancialNameConstants.FIN_COLA_PCT);
        double weightedGrowthRate = getDouble(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE);
        PaymentFrequency contributionFrequency = getValue(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY);

        int currentYear = LocalDate.now().getYear();
        int yearCount = retirementAge - currentAge;

        Function<ContributionBalance> contributionBalance =
                getFunctionProvider().getFactory().contributionBalance(currentSalary, colaPct, currentBalance, selfContributionPct, emplContributionPct,
                        weightedGrowthRate, contributionFrequency, currentYear, currentYear);

        balanceSchedule.add(contributionBalance.compute());

        final AtomicReference<ContributionBalance> currentRetirementBalance = new AtomicReference<>(contributionBalance.compute());
        final AtomicReference<Double> totalInterest = new AtomicReference<>(0.0);
        final AtomicReference<Double> totalSelfContribution = new AtomicReference<>(0.0);
        final AtomicReference<Double> totalEmplContribution = new AtomicReference<>(0.0);

        IntStream.range(0, yearCount).forEach(i -> {
            Function<ContributionBalance> newBalanceFx =
                    getFunctionProvider().getFactory().contributionBalance(currentRetirementBalance.get(), currentYear + i + 1);
            ContributionBalance newBalance = newBalanceFx.compute();
            balanceSchedule.add(newBalance);
            currentRetirementBalance.set(newBalance);
            totalInterest.getAndUpdate(interest -> interest += newBalance.getInterestAccrued());
            totalSelfContribution.getAndUpdate(selfContrib -> selfContrib += newBalance.getEstimatedSelfContribution());
            totalEmplContribution.getAndUpdate(emplContrib -> emplContrib += newBalance.getEstimatedEmployerContribution());
        });

        setCoefficient(FinancialFunctionTypes.CONTRIBUTION_SCHEDULE, balanceSchedule);
        setCoefficient(FinancialFunctionTypes.CONTRIBUTION_BALANCE, Scalar.of(currentRetirementBalance.get().getBalance()));
        setCoefficient(FinancialFunctionTypes.TOTAL_SELF_CONTRIBUTION, Scalar.of(totalSelfContribution.get()));
        setCoefficient(FinancialFunctionTypes.TOTAL_EMPL_CONTRIBUTION, Scalar.of(totalEmplContribution.get()));
        setCoefficient(FinancialFunctionTypes.TOTAL_INTEREST, Scalar.of(totalInterest.get()));
        setCoefficient(FinancialFunctionTypes.LAST_SALARY, Scalar.of(currentRetirementBalance.get().getCurrentSalary()));

    }
}
