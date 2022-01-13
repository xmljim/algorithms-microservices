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

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.functions.financial.RetirementModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RetirementModelImplTest {
    private static FinancialProvider functionProvider;
    private static ModelProvider modelProvider;


    @BeforeAll
    static void buildProviders() {
        Iterable<FunctionProvider> functionProviders = ServiceLoader.load(FunctionProvider.class);
        functionProvider = (FinancialProvider) functionProviders.iterator().next();

        Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
        modelProvider = modelProviders.iterator().next();
    }

    @Test
    void testSolve() {
        double currentSalary = 52_095;
        int currentAge = 32;
        int retirementAge = 65;
        double currentRetirementBalance = 26_932;
        double colaPct = 0.03;
        double selfContributionPct = 0.1;
        double employerContributionPct = 0.03;
        double weightedGrowthPct = 0.09203168503741728;
        double postRetirementInterest = 0.04;
        double inflationRate = 0.029;
        int duration = 0;
        double annualIncomePct = 0.0;
        PaymentFrequency distributionFrequency = PaymentFrequency.MONTHLY;
        PaymentFrequency contributionFrequency = PaymentFrequency.SEMI_MONTHLY;

        /*
        {
    "age": 32,
    "retirementAge": 65,
    "currentSalary": 52095.0,
    "colaPct": 0.03,
    "currentRetirementBalance": 26932,
    "selfContributionPct": 0.10,
    "employerContributionPct": 0.03,
    "investmentStyle": 0.85,
    "contributionFrequency": "SEMI_MONTHLY",
    "postRetirementInterestRate": 0.04,
    "distributionFrequency": "MONTHLY",
    "retirementDuration": 0,
    "incomeReplacementPct": 0.00,
    "annualizedDistribution": 0.0
}
         */

        RetirementModel retirementModel =
                functionProvider.getFactory().retirementModel(currentAge, retirementAge, currentSalary, currentRetirementBalance, selfContributionPct, employerContributionPct,
                        colaPct, weightedGrowthPct, contributionFrequency, postRetirementInterest, distributionFrequency, inflationRate, duration, annualIncomePct);

        retirementModel.solve();

        assertNotNull(retirementModel.getContributionModel());
        assertNotNull(retirementModel.getDistributionModel());
        assertEquals(retirementModel.getRetirementTimeline().size(), retirementModel.getContributionModel().getContributionTimeline().size()
                + retirementModel.getDistributionModel().getDistributionSchedule().size());
        assertEquals(28, retirementModel.getDistributionModel().getDistributionYears().asInt());
        assertEquals(LocalDate.now().getYear() + 28 + (retirementAge - currentAge), retirementModel.getRetirementBalanceDepletionYear().asInt());
        assertNotEquals(0, retirementModel.getRetirementIncomePct().asDouble());


        AtomicInteger currentYear = new AtomicInteger(LocalDate.now().getYear());
        int lastYear = retirementModel.getRetirementBalanceDepletionYear().asInt();

        //assert that the entire timeline increments by year
        retirementModel.getRetirementTimeline().forEach(balance -> {
            assertEquals(currentYear.get(), balance.getYear());
            currentYear.getAndIncrement();
        });

        System.out.println(retirementModel.getRetirementIncomePct());

    }
}
