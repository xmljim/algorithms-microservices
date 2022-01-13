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

import io.xmljim.algorithms.functions.financial.Balance;

abstract class AbstractBalance implements Balance {
    private final int year;
    private double balance;
    private double interest;
    private final double weightedGrowthRate;
    private final String type;

    public AbstractBalance(String type, final int year, final double balance, final double interest, final double weightedGrowthRate) {
        this.type = type;
        this.year = year;
        this.balance = balance;
        this.interest = interest;
        this.weightedGrowthRate = weightedGrowthRate;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public double getInterestAccrued() {
        return interest;
    }

    @Override
    public double getWeightedGrowthRate() {
        return weightedGrowthRate;
    }

    @Override
    public String getType() {
        return type;
    }

    protected void updateBalanceWithInterest(double interest) {
        this.interest = interest;
        balance += interest;
    }

    @Override
    public String toString() {
        return "AbstractRetirementBalance{" +
                "year=" + year +
                ", balance=" + balance +
                ", interest=" + interest +
                ", weightedGrowthRate=" + weightedGrowthRate +
                '}';
    }
}
