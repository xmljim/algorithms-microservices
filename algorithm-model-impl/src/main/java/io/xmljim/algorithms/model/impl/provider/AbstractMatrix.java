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

import io.xmljim.algorithms.model.AbstractNamedEntity;
import io.xmljim.algorithms.model.Matrix;
import io.xmljim.algorithms.model.ScalarVector;
import io.xmljim.algorithms.model.provider.ModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

abstract class AbstractMatrix extends AbstractNamedEntity implements Matrix {
    private List<ScalarVector> rows;
    private ModelProvider modelProvider;
    private int columnCount;

    public AbstractMatrix(String name, ModelProvider modelProvider, Stream<ScalarVector> rows) {
        super(name);
        this.rows = rows.collect(Collectors.toList());
        columnCount = this.rows.stream().mapToInt(ScalarVector::length).max().orElse(0);

        this.modelProvider = modelProvider;
    }

    public AbstractMatrix(String name, ModelProvider modelProvider, ScalarVector... columns) {
        super(name);
        this.modelProvider = modelProvider;
        rows = new ArrayList<>();
        this.columnCount = columns.length;

        List<ScalarVector> cols = Arrays.stream(columns).collect(Collectors.toList());
        int rowCount = cols.stream().mapToInt(ScalarVector::length).max().orElse(0);

        IntStream.range(0, rowCount).forEach(row -> {
            List<Number> rowList = new ArrayList<>();
            cols.stream().forEach(column -> {
                try {
                    rowList.add(column.get(row));
                } catch (Exception e) {
                    rowList.add(null);
                }
            });


            ScalarVector rowVector = Objects.requireNonNull(modelProvider).getVectorFactory().createScalarVector("row_" + row, rowList);
            rows.add(rowVector);
        });
    }

    public AbstractMatrix(String name, ModelProvider modelProvider, Number[][] numberArray) {
        super(name);
        this.modelProvider = modelProvider;
        rows = new ArrayList<>();
        final AtomicInteger maxColumnCount = new AtomicInteger();
        int rowCount = 0;

        Arrays.stream(numberArray).forEach(rowNumberArray -> {
            maxColumnCount.set(Math.max(maxColumnCount.get(), rowNumberArray.length));
            rows.add(modelProvider.getVectorFactory().createScalarVector("row_" + rowCount, rowNumberArray));
        });

        this.columnCount = maxColumnCount.get();
    }

    public ModelProvider getModelProvider() {
        return modelProvider;
    }

    @Override
    public ScalarVector getRow(final int row) {
        return this.rows.get(row);
    }

    @Override
    public ScalarVector getColumn(final int column) {
        List<Number> columnData = new ArrayList<>();

        this.rows.stream().forEach(row -> {
            try {
                columnData.add(row.get(column));
            } catch(Exception e) {
                columnData.add(null);
            }
        });

        return modelProvider.getVectorFactory().createScalarVector("column_" + column, columnData);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }
}
