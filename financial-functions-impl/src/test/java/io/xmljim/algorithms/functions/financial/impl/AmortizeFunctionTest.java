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
import io.xmljim.algorithms.functions.financial.RetirementContributionModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.provider.ModelProvider;
import io.xmljim.algorithms.model.util.Scalar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

class AmortizeFunctionTest {

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
    void testCompute() {

        double balance = 1_000_000;
        double interest = 0.04;
        PaymentFrequency frequency = PaymentFrequency.MONTHLY;
        int duration = 30;

        ScalarFunction distributionFx = functionProvider.getFactory().amortize(balance, interest, frequency, duration);
        assertEquals(4774.15, distributionFx.compute().asDouble(), 1E-2);
    }

    @Test
    void testWithRealValue() {
        int currentAge = 43;
        int retirementAge = 67;
        double currentSalary = 100_000;
        double selfContribPct = 0.1;
        double emplContribPct = 0.04;
        double currentBalance = 500_000;
        double colaPct = 0.02;
        double weightedGrowthPct = 0.084;
        PaymentFrequency contributionFrequency = PaymentFrequency.SEMI_MONTHLY;

        RetirementContributionModel model =
                functionProvider.getFactory().retirementContributionModel(currentAge, retirementAge, currentSalary, selfContribPct, emplContribPct,
                        currentBalance, colaPct, weightedGrowthPct, contributionFrequency);

        model.solve();

        double balance = model.getBalance().asDouble();
        double interest = 0.03;
        PaymentFrequency frequency = PaymentFrequency.MONTHLY;
        int duration = 30;

        ScalarFunction distributionFx = functionProvider.getFactory().amortize(balance, interest, frequency, duration);
        Scalar result = distributionFx.compute();

        assertEquals(21191.78, result.asDouble(), .01);

    }
}