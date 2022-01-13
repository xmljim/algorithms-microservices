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
import io.xmljim.algorithms.functions.financial.DistributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.functions.financial.RetirementDistributionModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.ArrayList;
import java.util.List;

class RetirementDistributionModelImpl extends AbstractModel implements RetirementDistributionModel {
    private final List<DistributionBalance> distributionBalanceList = new ArrayList<>();

    /**
     * Constructor
     * @param startingBalance starting balance
     * @param retirementYear retirement year
     * @param interestRate the interest rate
     * @param paymentFrequency the distribution frequency
     * @param inflationRate the inflation rate
     * @param duration the duration.  Set to 0 if you want to estimate either by a defined annualized distribution.
     *                 If the annualized distribution is also 0, then the function will automatically calculate
     *                 the annualized distribution at 4% of the original retirement balance
     * @param annualizedDistribution if set to 0, then the distribution value will be calculated based on the specified
     *                               duration parameter. If both the duration and annualizedDistribution parameters
     *                               are set to 0, then the function will automatically calculate the annualized distribution
     *                               at 4% of the original retirement balance.
     *
     */
    public RetirementDistributionModelImpl(ScalarParameter startingBalance, ScalarParameter retirementYear, ScalarParameter interestRate,
                                           Parameter<PaymentFrequency> paymentFrequency, ScalarParameter inflationRate, ScalarParameter duration,
                                           ScalarParameter annualizedDistribution) {

        super(FinancialFunctionTypes.RETIREMENT_DISTRIBUTION_MODEL.getName(),
                startingBalance, retirementYear, interestRate, paymentFrequency, inflationRate, duration, annualizedDistribution);
    }

    private FinancialProvider getFunctionProvider() {
        FinancialProvider provider = getFunctionProvider("Financial");
        return provider;
    }

    @Override
    public Coefficient<List<DistributionBalance>> getDistributionScheduleCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.DISTRIBUTION_SCHEDULE.getName());
        return (Coefficient<List<DistributionBalance>>) coeff;
    }

    @Override
    public ScalarCoefficient getDistributionYearsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.DISTRIBUTION_YEARS.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getBaseYearAnnualDistributionCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.RETIREMENT_ANNUAL_DISTRIBUTION.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getLastDistributionYearCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.DISTRIBUTION_LAST_YEAR.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalDistributionsCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_DISTRIBUTIONS.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getTotalInterestCoefficient() {
        Coefficient<?> coeff = getCoefficient(FinancialFunctionTypes.TOTAL_INTEREST.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public void solve() {
        double currentBalance = getDouble(FinancialNameConstants.FIN_CURRENT_401K_BALANCE);
        int retirementYear = getInteger(FinancialNameConstants.FIN_RETIREMENT_START_YEAR);
        double interestRate = getDouble(FinancialNameConstants.FIN_POST_RETIRE_INTEREST);
        int duration = getInteger(FinancialNameConstants.FIN_RETIREMENT_DURATION);
        double inflationRate = getDouble(FinancialNameConstants.FIN_INFLATION_RATE);
        PaymentFrequency frequency = getValue(FinancialNameConstants.FIN_DISTRIBUTION_FREQUENCY);
        double annualizedDistribution = getDouble(FinancialNameConstants.FIN_ANNUAL_DISTRIBUTION);

        double amortizedAnnualDist = duration <= 0 ?
                (annualizedDistribution > 0 ? annualizedDistribution : currentBalance * .04)
                : getAmortizedDistribution(currentBalance, inflationRate, interestRate, duration, frequency);

        double updatedBalance = currentBalance;
        int currentYear = retirementYear;
        double totalDistributions = 0.0;
        double totalInterest = 0.0;

        while(updatedBalance > 0) {
            currentYear++;
            DistributionBalance distributionBalance = calculateNewBalance(updatedBalance, amortizedAnnualDist, inflationRate, interestRate, retirementYear, currentYear, frequency);
            distributionBalanceList.add(distributionBalance);
            updatedBalance = distributionBalance.getBalance();
            totalDistributions += distributionBalance.getAnnualDistributionAmount();
            totalInterest += distributionBalance.getInterestAccrued();
        }

        setCoefficient(FinancialFunctionTypes.DISTRIBUTION_SCHEDULE, distributionBalanceList);
        setCoefficient(FinancialFunctionTypes.DISTRIBUTION_YEARS, Scalar.of(currentYear - retirementYear));
        setCoefficient(FinancialFunctionTypes.RETIREMENT_ANNUAL_DISTRIBUTION, Scalar.of(amortizedAnnualDist));
        setCoefficient(FinancialFunctionTypes.DISTRIBUTION_LAST_YEAR, Scalar.of(currentYear));
        setCoefficient(FinancialFunctionTypes.TOTAL_INTEREST, Scalar.of(totalInterest));
        setCoefficient(FinancialFunctionTypes.TOTAL_DISTRIBUTIONS, Scalar.of(totalDistributions));
    }

    /**
     * Compute a distribution balance using the {@link io.xmljim.algorithms.functions.financial.FinancialFunctions#distributionBalance(double, double, double, double, int, int, PaymentFrequency)}
     * function
     * @param currentBalance the current balance
     * @param amortizedValue the annualized distribution from the first retirement year
     * @param inflation the inflation rate
     * @param retirementInterest the interest rate on the retirement balance each year
     * @param retirementYear the year the investor retired
     * @param currentYear the current year to estimate
     * @return the distribution balance for a given year
     */
    private DistributionBalance calculateNewBalance(double currentBalance, double amortizedValue, double inflation, double retirementInterest, int retirementYear, int currentYear,
                                                    PaymentFrequency frequency) {

        Function<DistributionBalance> distFx = getFunctionProvider().getFactory().distributionBalance(currentBalance, amortizedValue, inflation,
                retirementInterest, retirementYear, currentYear, frequency);

        return distFx.compute();
    }

    /**
     * Return the amortized annual distribution amount from the start of retirement
     * @param balance the retirement balance
     * @param duration the duration of the distributions in years
     * @return the amortized annual distribution amount
     */
    private double getAmortizedDistribution(double balance, double inflation, double interest, int duration, PaymentFrequency frequency) {
        double baseRate = interest * (1 - interest);
        ScalarFunction amortize = getFunctionProvider().getFactory().amortize(balance, baseRate - inflation, frequency, duration);
        double result = amortize.compute().asDouble();

        return result * frequency.getAnnualFrequency();
    }

}
