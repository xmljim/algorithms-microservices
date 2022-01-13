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
import io.xmljim.algorithms.functions.financial.ContributionBalance;
import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.Function;
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Calculate an estimated retirement balance and contributions")
public class ContributionBalanceFunctionTest {

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
    @DisplayName("When the end year is the same as the current year, only interest is added")
    void computeSingleYear() {

        double salary = 25_000;
        double colaPct = 0.02;
        double currentRetirementBalance = 10_000;
        double selfContributionPct = 0.10;
        double emplContributionPct = 0.04;
        double weightedGrowth = 0.078;
        int currentYear = LocalDate.now().getYear();
        int endYear = 2022;
        PaymentFrequency contributionFrequency = PaymentFrequency.SEMI_MONTHLY;

        Function<ContributionBalance> contribution =
                functionProvider.getFactory().contributionBalance(salary, colaPct, currentRetirementBalance, selfContributionPct, emplContributionPct, weightedGrowth,
                        contributionFrequency, currentYear, endYear);
        ContributionBalance retirementBalance = contribution.compute();


        assertEquals(endYear, retirementBalance.getYear());

    }
}
