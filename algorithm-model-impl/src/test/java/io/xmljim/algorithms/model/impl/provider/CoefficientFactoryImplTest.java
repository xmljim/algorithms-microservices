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

package io.xmljim.algorithms.model.impl.provider;

import io.xmljim.algorithms.model.Coefficient;
import io.xmljim.algorithms.model.ScalarCoefficient;
import io.xmljim.algorithms.model.util.Scalar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coefficient Factory Tests")
class CoefficientFactoryImplTest extends ImplementationTestBase {

    @Test
    @DisplayName("Test scalar coefficient")
    void testCreateScalarCoefficient() {
        ScalarCoefficient labeledScalarCoeff = getModelProvider().getCoefficientFactory().createCoefficient("scalarName", "scalarLabel", Scalar.of(100));
        assertEquals("scalarName", labeledScalarCoeff.getName());
        assertEquals("scalarLabel", labeledScalarCoeff.getLabel());
        assertEquals(100, labeledScalarCoeff.getValue().intValue());

        ScalarCoefficient namedScalarCoeff = getModelProvider().getCoefficientFactory().createCoefficient("namedCoefficient", Scalar.of(100));
        assertEquals("namedCoefficient", namedScalarCoeff.getName());
        assertNull(namedScalarCoeff.getLabel());
        assertEquals(100, namedScalarCoeff.getValue().intValue());

    }

    @Test
    @DisplayName("Test generic coefficent types")
    void testGenericCoefficient() {
        Coefficient<String> stringCoefficient = getModelProvider().getCoefficientFactory().createCoefficient("stringCoeff", "STRING");
        assertEquals("STRING", stringCoefficient.getValue());

        Coefficient<Boolean> booleanCoefficient = getModelProvider().getCoefficientFactory().createCoefficient("booleanCoeff", true);
        assertTrue(booleanCoefficient.getValue());

        Coefficient<LocalDate> labeledLocalDateCoeff = getModelProvider().getCoefficientFactory().createCoefficient("date", "currentDate", LocalDate.now());
        assertEquals("currentDate", labeledLocalDateCoeff.getLabel());
        assertEquals(LocalDate.now(), labeledLocalDateCoeff.getValue());
    }

}