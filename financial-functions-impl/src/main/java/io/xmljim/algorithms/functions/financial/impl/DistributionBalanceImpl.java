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

import io.xmljim.algorithms.functions.financial.DistributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;

class DistributionBalanceImpl extends AbstractBalance implements DistributionBalance {
    private final double annualDistributionAmount;
    private final double inflationRate;
    private PaymentFrequency paymentFrequency;
    private double periodicDistributionAmount;

    public DistributionBalanceImpl(final int year, final double balance, final double interest, final double weightedGrowthRate, final double distributionAmount,
                                   final double inflationRate, PaymentFrequency paymentFrequency) {
        super("distribution", year, balance, interest, weightedGrowthRate);
        this.annualDistributionAmount = distributionAmount;
        this.inflationRate = inflationRate;
        this.paymentFrequency = paymentFrequency;
        this.periodicDistributionAmount = annualDistributionAmount / paymentFrequency.getAnnualFrequency();
    }

    @Override
    public double getAnnualDistributionAmount() {
        return annualDistributionAmount;
    }

    @Override
    public double getInflationRate() {
        return inflationRate;
    }

    @Override
    public PaymentFrequency getDistributionFrequency() {
        return paymentFrequency;
    }

    @Override
    public double getPeriodicDistributionAmount() {
        return periodicDistributionAmount;
    }

    @Override
    public String toString() {
        return "DistributionBalanceImpl{" +
                "annualDistributionAmount=" + annualDistributionAmount +
                ", inflationRate=" + inflationRate +
                ", balance=" + super.getBalance() +
                ", interest=" + super.getInterestAccrued() +
                ", year=" + super.getYear() +
                "} ";
    }
}
