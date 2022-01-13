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

enum FinancialFunctionTypes implements FinancialFunctionType {
    RETIREMENT_CONTRIBUTION_MODEL("retirementContributionModel", "Retirement Contribution Estimates"),
    CONTRIBUTION_BALANCE_FUNCTION("calculateContributionFunction", "Calculate Contribution Balance"),
    CONTRIBUTION_SCHEDULE("contributionSchedule", "Contribution Schedule"),
    CONTRIBUTION_BALANCE("contributionBalance", "Contribution Balance"),
    RETIREMENT_DISTRIBUTION_MODEL("retirementDistributionModel", "Retirement Distribution Estimates"),
    DISTRIBUTION_BALANCE_FUNCTION("calculateDistributionFunction", "Calculate Distribution Balance"),
    DISTRIBUTION_SCHEDULE("distributionSchedule", "Distribution Schedule"),
    DISTRIBUTION_YEARS("distributionLengthYears", "Distribution Number of Years"),
    DISTRIBUTION_LAST_YEAR("distributionLastYear", "Distribution Last Year"),
    WEIGHTED_GROWTH("calculateWeightedGrowthRate", "Weighted Growth Rate"),
    TOTAL_INTEREST("totalInterest", "Total Interest Accrued"),
    TOTAL_SELF_CONTRIBUTION("totalSelfContribution", "Total Self Contribution"),
    TOTAL_EMPL_CONTRIBUTION("totalEmplContribution", "Total Employer Contribution"),
    LAST_SALARY("lastSalary", "Last Salary"),
    AMORTIZE("amortize", "Amoritization Value"),
    RETIREMENT_MODEL("retirementModel", "Retirement Model"),
    RETIREMENT_SCHEDULE("retirementSchedule", "Retirement Schedule"),
    RETIREMENT_DEPLETION_YEAR("retirementDepletionYear", "Retirement Depletion Year"),
    RETIREMENT_INCOME_PCT("retirementIncomePct", "Retirement Income Replacement Percentage"),
    RETIREMENT_ANNUAL_DISTRIBUTION("retirementBaseAnnualDistribution", "Base Retirement Annual Distribution"),
    RETIREMENT_YEAR("retirementYear", "Retirement Year"),
    TOTAL_DISTRIBUTIONS("totalDistributions", "Total Distributions")
    ;

    private final String name;
    private final String label;

    FinancialFunctionTypes(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
