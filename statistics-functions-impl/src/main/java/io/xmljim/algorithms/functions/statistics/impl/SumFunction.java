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

package io.xmljim.algorithms.functions.statistics.impl;

import io.xmljim.algorithms.functions.common.AbstractScalarFunction;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.ScalarVectorParameter;
import io.xmljim.algorithms.model.util.Scalar;

class SumFunction extends AbstractScalarFunction {
    private Scalar result = null;

    public SumFunction(ScalarVectorParameter vectorParameter) {
        super(StatisticsFunctionTypes.SUM, vectorParameter);
    }

    public SumFunction(ScalarVectorParameter vectorParameter, String variable) {
        super(StatisticsFunctionTypes.SUM, variable, vectorParameter);
    }

    private Scalar computeSum() {
        ScalarVector vector = getValue(0);
        double value = vector.stream().mapToDouble(Scalar::asDouble).sum();
        return Scalar.of(value);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeSum();
        }
        return result;
    }
}
