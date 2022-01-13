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

import java.util.List;
import java.util.stream.Collectors;

class MedianFunction extends AbstractScalarFunction {
    private Scalar result;

    public MedianFunction(ScalarVectorParameter scalarVectorParameter) {
        super(StatisticsFunctionTypes.MEDIAN, scalarVectorParameter);
    }

    public MedianFunction(ScalarVectorParameter scalarVectorParameter, String variable) {
        super(StatisticsFunctionTypes.MEDIAN, variable, scalarVectorParameter);
    }

    private Scalar computeMedian() {
        double med;
        ScalarVector vector = getValue(0);
        List<Number> sorted = vector.sorted().collect(Collectors.toList());

        if (sorted.size() % 2 == 1) {
            int index = ((sorted.size() + 1) / 2) - 1;
            med = sorted.get(index).doubleValue();
        } else {
            int left = (sorted.size() / 2) - 1;
            int right = (sorted.size() / 2);

            med = (sorted.get(left).doubleValue() + sorted.get(right).doubleValue()) / 2;
        }
        return Scalar.of(med);
    }

    @Override
    public Scalar compute() {
        if (result == null) {
            result = computeMedian();
        }
        return result;
    }
}
