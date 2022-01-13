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

package io.xmljim.algorithms.model;

import io.xmljim.algorithms.model.util.Scalar;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Matrix extends NamedEntity {

    default Scalar get(int row, int column) {
        ScalarVector rowVector = getRow(row);
        return rowVector.get(column);
    }

    default Stream<Scalar> getRowStream(int row) {
        return getRow(row).stream();
    }

    default Stream<Scalar> getColumnStream(int column) {
        return getColumn(column).stream();
    }

    ScalarVector getRow(int row);

    ScalarVector getColumn(int column);

    ScalarVector getColumn(String name);

    String[] getColumnNames();

    int getRowCount();

    int getColumnCount();

    default Double[][] toDoubleArray() {
        Double[][] data = new Double[getRowCount()][getColumnCount()];

        IntStream.range(0, getRowCount()).forEach(row -> {
            IntStream.range(0, getColumnCount()).forEach(column -> {
                try {
                    data[row][column] = get(row, column).asDouble();
                } catch (Exception e) {
                    data[row][column] = null;
                }
            });
        });

        return data;
    }


}