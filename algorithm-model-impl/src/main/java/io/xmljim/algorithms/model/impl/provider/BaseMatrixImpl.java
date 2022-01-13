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

import io.xmljim.algorithms.model.Matrix;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.provider.ModelProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class BaseMatrixImpl extends AbstractMatrix implements Matrix {
    private final Map<String, Integer> columnNameMap = new HashMap<>();


    public BaseMatrixImpl(final String name, final ModelProvider modelProvider, final Stream<ScalarVector> rows) {
        super(name, modelProvider, rows);
    }

    public BaseMatrixImpl(final String name, final ModelProvider modelProvider, final Stream<ScalarVector> rows, String...columnHeaders) {
        this(name, modelProvider, rows);
        IntStream.range(0, columnHeaders.length).forEach(i -> {
            columnNameMap.put(columnHeaders[i], i);
        });
    }

    public BaseMatrixImpl(final String name, final ModelProvider modelProvider, final ScalarVector... columns) {
        super(name, modelProvider, columns);
        IntStream.range(0, columns.length).forEach(i -> {
            columnNameMap.put(columns[i].getName(), i);
        });
    }

    public BaseMatrixImpl(final String name, final ModelProvider modelProvider, final Number[][] numberArray) {
        super(name, modelProvider, numberArray);
    }

    public BaseMatrixImpl(final String name, final ModelProvider modelProvider, final Number[][] numberArray, String...columnHeaders) {
        super(name, modelProvider, numberArray);
        IntStream.range(0, columnHeaders.length).forEach(i -> {
            columnNameMap.put(columnHeaders[i], i);
        });
    }

    @Override
    public ScalarVector getColumn(final String name) {
        return getColumn(columnNameMap.get(name));
    }

    @Override
    public String[] getColumnNames() {
        return columnNameMap.keySet().toArray(String[]::new);
    }
}