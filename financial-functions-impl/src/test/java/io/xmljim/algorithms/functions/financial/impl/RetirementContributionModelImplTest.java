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
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetirementContributionModelImplTest {

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
    void testModel() {
        int currentAge = 32;
        int retirementAge = 67;
        double currentSalary = 85_000;
        double selfContribPct = 0.1;
        double emplContribPct = 0.04;
        double currentBalance = 32_000;
        double colaPct = 0.02;
        double weightedGrowthPct = 0.093;

        RetirementContributionModel model =
                functionProvider.getFactory().retirementContributionModel(currentAge, retirementAge, currentSalary, selfContribPct, emplContribPct,
                        currentBalance, colaPct, weightedGrowthPct, PaymentFrequency.SEMI_MONTHLY);

        model.solve();
        assertEquals(retirementAge - currentAge + 1, model.getContributionTimeline().size());
        assertEquals(169990.61, model.getLastSalary().asDouble(), 0.1);

    }
}
