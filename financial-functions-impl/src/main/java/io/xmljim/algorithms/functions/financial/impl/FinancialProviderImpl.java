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

import io.xmljim.algorithms.functions.financial.FinancialFunctions;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FinancialProviderImpl implements FinancialProvider {
    private FinancialFunctions financialFunctions = new FinancialFunctionsImpl();


    @Override
    public FinancialFunctions getFactory() {
        return financialFunctions;
    }

    @Override
    public Version getProviderVersion() {
        try (InputStream inputStream = getClass().getResourceAsStream("/function-impl-build.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Version version = new Version(properties.getProperty("build.version"));
            return version;
        } catch (IOException ioe) {
            return new Version();
        }
    }
}
