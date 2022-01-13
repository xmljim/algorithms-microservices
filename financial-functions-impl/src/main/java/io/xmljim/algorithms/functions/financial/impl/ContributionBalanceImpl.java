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

import io.xmljim.algorithms.functions.financial.ContributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;

class ContributionBalanceImpl extends AbstractBalance implements ContributionBalance {
    private final double selfContributionPct;
    private final double employerContributionPct;
    private final double currentSalary;
    private final double colaPct;
    private final double estimatedSelfContribution;
    private final double estimatedEmployerContribution;
    private final PaymentFrequency contributionFrequency;

    public ContributionBalanceImpl(final int year, final double balance, final double interest, final double weightedGrowthRate,
                                   final double selfContributionPct, final double employerContributionPct, final double currentSalary,
                                   final double colaPct, final double estimatedSelfContribution, final double estimatedEmployerContribution,
                                   final PaymentFrequency contributionFrequency) {

        super("contribution", year, balance, interest, weightedGrowthRate);
        this.selfContributionPct = selfContributionPct;
        this.employerContributionPct = employerContributionPct;
        this.currentSalary = currentSalary;
        this.colaPct = colaPct;
        this.estimatedSelfContribution = estimatedSelfContribution;
        this.estimatedEmployerContribution = estimatedEmployerContribution;
        this.contributionFrequency = contributionFrequency;
    }

    @Override
    public double getSelfContributionPct() {
        return selfContributionPct;
    }

    @Override
    public double getEmployerContributionPct() {
        return employerContributionPct;
    }

    @Override
    public double getCurrentSalary() {
        return currentSalary;
    }

    @Override
    public double getColaPct() {
        return colaPct;
    }

    @Override
    public double getEstimatedSelfContribution() {
        return estimatedSelfContribution;
    }

    @Override
    public double getEstimatedEmployerContribution() {
        return estimatedEmployerContribution;
    }

    @Override
    public PaymentFrequency getContributionFrequency() {
        return contributionFrequency;
    }

    @Override
    public String toString() {
        return "ContributionBalanceImpl{" +
                "selfContributionPct=" + selfContributionPct +
                ", employerContributionPct=" + employerContributionPct +
                ", currentSalary=" + currentSalary +
                ", colaPct=" + colaPct +
                ", estimatedSelfContribution=" + estimatedSelfContribution +
                ", estimatedEmployerContribution=" + estimatedEmployerContribution +
                ", estimatedRetirementBalance=" + super.getBalance() +
                ", interest=" + super.getInterestAccrued() +
                ", year=" + super.getYear() +
                '}';
    }
}
