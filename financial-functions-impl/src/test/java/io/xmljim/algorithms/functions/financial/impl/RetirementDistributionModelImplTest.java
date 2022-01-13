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
import io.xmljim.algorithms.functions.financial.RetirementDistributionModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.provider.ModelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetirementDistributionModelImplTest {

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
        double balance = 1_000_000;
        int retirementYear = 2033;
        double interestRate = 0.04;
        PaymentFrequency frequency = PaymentFrequency.MONTHLY;
        double inflationRate = 0.029;
        int duration = 30;
        double annualizedDistribution = 0;

        RetirementDistributionModel model =
                functionProvider.getFactory().retirementDistributionModel(balance, retirementYear, interestRate, frequency, inflationRate, duration, annualizedDistribution);
        model.solve();

        assertEquals(duration, model.getDistributionYears().asInt());
    }

}
