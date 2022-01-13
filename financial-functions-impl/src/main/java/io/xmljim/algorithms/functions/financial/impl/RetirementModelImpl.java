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
import io.xmljim.algorithms.functions.financial.*;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.Coefficient;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarCoefficient;
import io.xmljim.algorithms.model.ScalarParameter;
import io.xmljim.algorithms.model.util.Scalar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class RetirementModelImpl extends AbstractModel implements RetirementModel {
    private RetirementContributionModel retirementContributionModel;
    private RetirementDistributionModel retirementDistributionModel;

    public RetirementModelImpl(ScalarParameter currentAge, ScalarParameter retirementAge, ScalarParameter currentSalary, ScalarParameter currentRetirementBalance,
                               ScalarParameter selfContributionPct, ScalarParameter employerContributionPct, ScalarParameter colaPct, ScalarParameter weightedGrowthRate,
                               Parameter<PaymentFrequency> contributionFrequency, ScalarParameter postRetirementInterest, Parameter<PaymentFrequency> distributionFrequency,
                               ScalarParameter inflationRate, ScalarParameter duration, ScalarParameter annualizedLastSalaryPct) {

        super(FinancialFunctionTypes.RETIREMENT_MODEL.getName(), currentAge, retirementAge, currentSalary, currentRetirementBalance, selfContributionPct, employerContributionPct, colaPct,
                weightedGrowthRate, contributionFrequency, postRetirementInterest, distributionFrequency, inflationRate, duration, annualizedLastSalaryPct);
    }

    private FinancialProvider getFunctionProvider() {
        FinancialProvider provider = getFunctionProvider("Financial");
        return provider;
    }

    @Override
    public RetirementContributionModel getContributionModel() {
        return retirementContributionModel;
    }

    @Override
    public RetirementDistributionModel getDistributionModel() {
        return retirementDistributionModel;
    }

    @Override
    public Coefficient<List<Balance>> getRetirementTimelineCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_SCHEDULE.getName());
        return (Coefficient<List<Balance>>) coeff;
    }

    @Override
    public ScalarCoefficient getRetirementIncomePctCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_INCOME_PCT.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getRetirementBalanceDepletionYearCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_DEPLETION_YEAR.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getRetirementYearCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_DEPLETION_YEAR.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getBalanceAtRetirementCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.CONTRIBUTION_BALANCE.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalInterestCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_INTEREST.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalDistributionsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_DISTRIBUTIONS.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalSelfContributionsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_SELF_CONTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalEmployerContributionsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_EMPL_CONTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalDistributionYearsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.DISTRIBUTION_YEARS.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getBaseAnnualDistributionCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_ANNUAL_DISTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public void solve() {
        //This will hold retirement balance entries from both the contribution and distribution models...
        List<Balance> retirementBalanceList = new ArrayList<>();


        retirementContributionModel = getFunctionProvider().getFactory().retirementContributionModel(
                getInteger(FinancialNameConstants.FIN_AGE), getInteger(FinancialNameConstants.FIN_RETIREMENT_AGE), getDouble(FinancialNameConstants.FIN_CURRENT_SALARY),
                getDouble(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT), getDouble(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT), getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE),
                getDouble(FinancialNameConstants.FIN_COLA_PCT), getDouble(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE), getValue(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY));

        retirementContributionModel.solve();

        retirementBalanceList.addAll(retirementContributionModel.getContributionTimeline());

        double retirementBalance = retirementContributionModel.getBalance().asDouble();
        int retirementYear = LocalDate.now().getYear() + (getInteger(FinancialNameConstants.FIN_RETIREMENT_AGE) - getInteger(FinancialNameConstants.FIN_AGE));
        int duration = getInteger(FinancialNameConstants.FIN_RETIREMENT_DURATION);
        double postRetirementInterest = getDouble(FinancialNameConstants.FIN_POST_RETIRE_INTEREST);
        double inflationRate = getDouble(FinancialNameConstants.FIN_INFLATION_RATE);
        PaymentFrequency distributionFrequency = getValue(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY);
        double annualizedPct = getDouble(FinancialNameConstants.FIN_LAST_SALARY_PCT);
        double annualizedDistribution = retirementContributionModel.getLastSalary().asDouble() * annualizedPct;

        retirementDistributionModel =
                getFunctionProvider().getFactory().retirementDistributionModel(retirementBalance, retirementYear, postRetirementInterest, distributionFrequency,
                        inflationRate, duration, annualizedDistribution);

        retirementDistributionModel.solve();
        retirementBalanceList.addAll(retirementDistributionModel.getDistributionSchedule());

        double incomeReplacementPct = retirementDistributionModel.getBaseYearAnnualDistribution().asDouble() / retirementContributionModel.getLastSalary().asDouble();
        double totalInterest = retirementContributionModel.getTotalInterest().asDouble() + retirementDistributionModel.getTotalInterest().asDouble();

        setCoefficient(FinancialFunctionTypes.RETIREMENT_SCHEDULE, retirementBalanceList);
        setCoefficient(FinancialFunctionTypes.RETIREMENT_INCOME_PCT, Scalar.of(incomeReplacementPct));
        setCoefficient(FinancialFunctionTypes.RETIREMENT_DEPLETION_YEAR, Scalar.of(retirementDistributionModel.getLastDistributionYear()));
        setCoefficient(FinancialFunctionTypes.RETIREMENT_YEAR, Scalar.of(retirementYear));
        setCoefficient(retirementContributionModel.getBalanceCoefficient());
        setCoefficient(FinancialFunctionTypes.TOTAL_INTEREST, Scalar.of(totalInterest));
        setCoefficient(retirementDistributionModel.getTotalDistributionsCoefficient());
        setCoefficient(retirementContributionModel.getTotalSelfContributionCoefficient());
        setCoefficient(retirementContributionModel.getTotalEmployerContributionCoefficient());
        setCoefficient(retirementDistributionModel.getDistributionYearsCoefficient());
        setCoefficient(retirementDistributionModel.getBaseYearAnnualDistributionCoefficient());
        setCoefficient(retirementContributionModel.getLastSalaryCoefficient());


    }
}
