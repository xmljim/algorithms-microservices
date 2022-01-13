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

import io.xmljim.algorithms.functions.common.provider.AbstractFunctionFactory;
import io.xmljim.algorithms.functions.financial.*;
import io.xmljim.algorithms.model.Function;
import io.xmljim.algorithms.model.Parameter;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.ScalarParameter;

class FinancialFunctionsImpl extends AbstractFunctionFactory implements FinancialFunctions {

    /**
     * {@inheritDoc}
     */
    @Override
    public ScalarFunction amortize(final double balance, final double interest, PaymentFrequency frequency, final int durationYear) {
        ScalarParameter balanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, balance);
        ScalarParameter interestParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_POST_RETIRE_INTEREST, interest);
        Parameter<PaymentFrequency> frequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY, frequency);
        ScalarParameter durationParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_DURATION, durationYear);

        return new AmortizeFunction(balanceParam, interestParam, frequencyParam, durationParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScalarFunction weightedGrowth(final double stockRate, final double treasuryRate, final double proportion) {
        ScalarParameter stockRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_STOCK_GROWTH_RATE, stockRate);
        ScalarParameter treasuryRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_TREASURY_YIELD, treasuryRate);
        ScalarParameter investmentRatioParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_INVESTMENT_RATIO, proportion);

        return new WeightedGrowthFunction(stockRateParam, treasuryRateParam, investmentRatioParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RetirementContributionModel retirementContributionModel(final int currentAge, final int retirementAge, final double currentSalary,
                                                                   final double employeeContribution, final double employerContribution,
                                                                   final double currentBalance, final double colaPct, final double weightedGrowthRate,
                                                                   final PaymentFrequency contributionFrequency) {

        ScalarParameter currentAgeParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_AGE, currentAge);
        ScalarParameter retirementAgeParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_AGE, retirementAge);
        ScalarParameter currentSalaryParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_SALARY, currentSalary);
        ScalarParameter selfContribParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT, employeeContribution);
        ScalarParameter emplContribParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT, employerContribution);
        ScalarParameter current401kBalanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, currentBalance);
        ScalarParameter colaPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_COLA_PCT, colaPct);
        ScalarParameter weightedGrowthParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE, weightedGrowthRate);
        Parameter<PaymentFrequency> contributionFrequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY, contributionFrequency);

        return new RetirementContributionModelImpl(currentAgeParam, retirementAgeParam, currentSalaryParam, selfContribParam, emplContribParam,
                current401kBalanceParam, colaPctParam, weightedGrowthParam, contributionFrequencyParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<ContributionBalance> contributionBalance(final double currentSalary, final double colaPct,
                                                             final double currentRetirementBalance, final double selfContributionPct,
                                                             final double employerContributionPct, final double weightedGrowthRate,
                                                             PaymentFrequency contributionFrequency,
                                                             final int currentYear, final int endYear) {

        ScalarParameter currentSalaryParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_SALARY, currentSalary);
        ScalarParameter colaPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_COLA_PCT, colaPct);
        ScalarParameter currentRetirementBalanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, currentRetirementBalance);
        ScalarParameter selfContributionPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT, selfContributionPct);
        ScalarParameter emplContributionPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT, employerContributionPct);
        ScalarParameter weightedGrowthRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE, weightedGrowthRate);
        Parameter<PaymentFrequency> contributionFrequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY, contributionFrequency);
        ScalarParameter currentYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_YEAR, currentYear);
        ScalarParameter endYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_END_YEAR, endYear);

        return new ContributionBalanceFunction(currentSalaryParam, colaPctParam, currentRetirementBalanceParam, selfContributionPctParam,
                emplContributionPctParam, weightedGrowthRateParam, contributionFrequencyParam, currentYearParam, endYearParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<ContributionBalance> contributionBalance(final ContributionBalance previousBalance, final int endYear) {

        Parameter<ContributionBalance> contributionBalanceParameter = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_PREVIOUS_BALANCE, previousBalance);
        ScalarParameter endYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_END_YEAR, endYear);
        return new ContributionBalanceFunction(contributionBalanceParameter, endYearParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<DistributionBalance> distributionBalance(final double currentBalance, final double amortizedValue, final double inflation,
                                                             final double retirementInterest, final int retirementYear, final int currentYear,
                                                             PaymentFrequency distributionFrequency) {

        ScalarParameter currentBalanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, currentBalance);
        ScalarParameter amortizedParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_ANNUAL_DISTRIBUTION, amortizedValue);
        ScalarParameter inflationParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_INFLATION_RATE, inflation);
        ScalarParameter retirementInterestParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE, retirementInterest);
        ScalarParameter retirementYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_START_YEAR, retirementYear);
        ScalarParameter currentYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_YEAR, currentYear);
        Parameter<PaymentFrequency> distributionFrequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY, distributionFrequency);

        return new DistributionBalanceFunction(currentBalanceParam, amortizedParam, inflationParam, retirementInterestParam, retirementYearParam, currentYearParam, distributionFrequencyParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RetirementDistributionModel retirementDistributionModel(double startingBalance, int retirementYear, double interestRate, PaymentFrequency paymentFrequency,
                                                                   double inflationRate, int duration, double annualizedDistribution) {


        ScalarParameter currentBalanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, startingBalance);
        ScalarParameter retirementYearParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_START_YEAR, retirementYear);
        ScalarParameter interestRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_POST_RETIRE_INTEREST, interestRate);
        ScalarParameter durationParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_DURATION, duration);
        ScalarParameter inflationRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_INFLATION_RATE, inflationRate);
        Parameter<PaymentFrequency> paymentFrequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY, paymentFrequency);
        ScalarParameter annualizedDistributionParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_ANNUAL_DISTRIBUTION, annualizedDistribution);

        return new RetirementDistributionModelImpl(currentBalanceParam, retirementYearParam, interestRateParam, paymentFrequencyParam, inflationRateParam, durationParam, annualizedDistributionParam);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RetirementModel retirementModel(final int currentAge, final int retirementAge, final double currentSalary, final double currentRetirementBalance,
                                           final double selfContributionPct, final double employerContributionPct, final double colaPct, final double weightedGrowthPct,
                                           final PaymentFrequency contributionFrequency, final double postRetirementInterest, final PaymentFrequency distributionFrequency,
                                           final double inflationRate, final int duration, final double annualizedLastSalaryPct) {

        ScalarParameter currentAgeParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_AGE, currentAge);
        ScalarParameter retirementAgeParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_AGE, retirementAge);
        ScalarParameter currentSalaryParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_SALARY, currentSalary);
        ScalarParameter currentRetirementBalanceParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CURRENT_401K_BALANCE, currentRetirementBalance);
        ScalarParameter selfContributionPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYEE_CONTRIB_PCT, selfContributionPct);
        ScalarParameter emplContributionPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_EMPLOYER_CONTRIB_PCT, employerContributionPct);
        ScalarParameter colaPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_COLA_PCT, colaPct);
        ScalarParameter weightedGrowthParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_WEIGHTED_GROWTH_RATE, weightedGrowthPct);
        Parameter<PaymentFrequency> contributionFequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_CONTRIBUTION_FREQUENCY, contributionFrequency);
        ScalarParameter postRetirementInterestParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_POST_RETIRE_INTEREST, postRetirementInterest);
        Parameter<PaymentFrequency> paymentFrequencyParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY, distributionFrequency);
        ScalarParameter inflationRateParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_INFLATION_RATE, inflationRate);
        ScalarParameter durationParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_RETIREMENT_DURATION, duration);
        ScalarParameter incomeReplacementPctParam = getModelProvider().getParameterFactory().createParameter(FinancialNameConstants.FIN_LAST_SALARY_PCT, annualizedLastSalaryPct);

        return new RetirementModelImpl(currentAgeParam, retirementAgeParam, currentSalaryParam, currentRetirementBalanceParam, selfContributionPctParam, emplContributionPctParam,
                colaPctParam, weightedGrowthParam, contributionFequencyParam, postRetirementInterestParam, paymentFrequencyParam, inflationRateParam, durationParam, incomeReplacementPctParam);
    }

    @Override
    public String getFactoryName() {
        return "Financial";
    }
}
