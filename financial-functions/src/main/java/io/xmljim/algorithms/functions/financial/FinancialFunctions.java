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

package io.xmljim.algorithms.functions.financial;

import io.xmljim.algorithms.functions.common.provider.FunctionFactory;
import io.xmljim.algorithms.model.Function;
import io.xmljim.algorithms.model.ScalarFunction;

public interface FinancialFunctions extends FunctionFactory {
    /**
     * Calculate an amortized payment given a principle balance, an annual interest rate, payment frequency (annually),
     * the duration, and optionally weighted for inflation
     * @param balance the principle balance
     * @param interest the annual interest rate
     * @param frequency payment frequency
     * @param durationYear the number of years payments are spread over
     * @return a function to calculate an amortized payment
     */
    ScalarFunction amortize(double balance, double interest, PaymentFrequency frequency, int durationYear);

    /**
     * Calculate a weighted growth rate.
     * <p>
     *     The weighted growth rate is calculated as:
     *     <pre>
     *         g<sub>weighted</sub> = (r<sub>stock</sub> * p) + (r<sub>bond</sub> * (1-p))
     *
     *         Where:
     *
     *         g<sub>weighted</sub> is the weighted growth rate
     *         r<sub>stock</sub> is the stock growth rate
     *         r<sub>bond</sub> is the bond growth rate
     *         p is the proportion weight applied to a growth rate
     *     </pre>
     * </p>
     * <p><strong>Example</strong></p>
     * <p>Assume the stock growth rate is 10%, and the average bond yield is 2%. Assume that
     *  the investor has a portfolio of 90% stocks and 10% bonds. The weighted growth rate
     *  would be:
     *  <pre>
     *      (.1 * .9) + (.02 * .1) = .092
     *  </pre>
     *
     *  Or a 9.2% weighted growth rate
     * </p>
     * @param stockRate the rate of growth in stocks
     * @param treasuryRate the rate of growth in bonds
     * @param proportion the proportion to apply to each rate. Value must be between 0.0 and 1.0
     * @return the function to calculate the weighted growth rate
     */
    ScalarFunction weightedGrowth(double stockRate, double treasuryRate, double proportion);

    /**
     * Estimates contribution balance for given span of time
     * @param currentSalary
     * @param colaPct
     * @param currentRetirementBalance
     * @param selfContributionPct
     * @param employerContributionPct
     * @param weightedGrowthRate
     * @param currentYear
     * @param endYear
     * @return
     */
    Function<ContributionBalance> contributionBalance(double currentSalary, double colaPct, double currentRetirementBalance, double selfContributionPct,
                                                      double employerContributionPct, double weightedGrowthRate, PaymentFrequency contributionFrequency, int currentYear, int endYear);


    Function<ContributionBalance> contributionBalance(ContributionBalance previousBalance, int endYear);

    /**
     * Calculate a distribution for a given year
     * @param currentBalance the current retirement balance
     * @param amortizedValue the base annual distribution amount at the start of retirement
     * @param inflation the inflation rate
     * @param retirementInterest the amount of interest to apply to the remaining retirement account after distribution
     * @param retirementYear the year the investor retired
     * @param currentYear the current year - used to compute the distribution amount, adjusted for inflation
     * @return A distribution balance computed to include the estimated annualized distribution amount, interest, and updated retirement account balance
     */
    Function<DistributionBalance> distributionBalance(double currentBalance, double amortizedValue, double inflation, double retirementInterest, int retirementYear,
                                                      int currentYear, PaymentFrequency paymentFrequency);


    /**
     * Retirement Contribution Model. Using given inputs, the model will calculate
     * a balance at the specified retirement year.
     * @param currentAge The investor's current age. Used as the basis for calculating timeline
     * @param retirementAge The expected retirement age.
     * @param currentSalary The investor's current salary
     * @param employeeContribution the percentage of salary to contribute annually
     * @param employerContribution the percentage of salary the employer contributes annually
     * @param currentBalance the current retirement account balance
     * @param colaPct Average rate of salary increase annually
     * @param weightedGrowthRate the weighted annual grow rate - see {@link #weightedGrowth(double, double, double)}
     * @return The Retirement contribution model
     */
    RetirementContributionModel retirementContributionModel(int currentAge, int retirementAge, double currentSalary, double employeeContribution,
                                                            double employerContribution, double currentBalance, double colaPct, double weightedGrowthRate,
                                                            PaymentFrequency contributionFrequency);

    /**
     * Create a retirement distribution model
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
     */
    RetirementDistributionModel retirementDistributionModel(double startingBalance, int retirementYear, double interestRate, PaymentFrequency paymentFrequency,
                                                            double inflationRate, int duration, double annualizedDistribution);


    /**
     * Creates a Retirement model to estimate both contributions and distributions from a retirement account
     * @param currentAge the investor's current age in years
     * @param retirementAge the expected age at retirement
     * @param currentSalary the investor's current salary
     * @param currentRetirementBalance the investor's current retirement balance
     * @param selfContributionPct the percentage of salary to contribute to the retirement account
     * @param employerContributionPct the percentage of salary the employer will contribute to the retirement account
     * @param colaPct the estimated annual salary cost of living adjustment
     * @param weightedGrowthPct the weighted annual growth rate of the retirement account prior to retirement
     * @param contributionFrequency the frequency of contributions annually
     * @param postRetirementInterest the estimated annual interest on the retirement account
     * @param distributionFrequency the frequency of distributions
     * @param inflationRate the estimated annual inflation rate
     * @param duration the retirement duration in years.  Set to 0 if you want to estimate either by a defined annualized distribution.
     *                 If the annualized distribution is also 0, then the function will automatically calculate
     *                 the annualized distribution at 4% of the original retirement balance
     * @param annualizedLastSalaryPct The percentage of the investor's last annual salary to use as a basis for distributions.
     *                                If set to zero, the annualized distribution will be computed from the expected duration parameter.
     *                                Generally, financial advisors recommend that investors estimate that retirement income
     *                                from all sources should replace between 75-80% of the last salary. Keep in mind that this
     *                                model <strong>does not</strong> include other income sources like Social Security, IRAs or other investments
     * @return A retirement model
     */
    RetirementModel retirementModel(int currentAge, int retirementAge, double currentSalary, double currentRetirementBalance, double selfContributionPct,
                                    double employerContributionPct, double colaPct, double weightedGrowthPct, PaymentFrequency contributionFrequency,
                                    double postRetirementInterest, PaymentFrequency distributionFrequency,
                                    double inflationRate, int duration, double annualizedLastSalaryPct);
}
