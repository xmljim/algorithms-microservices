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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Matrix Factory Tests")
class MatrixFactoryImplTest extends ImplementationTestBase {

    @Test
    @DisplayName("Create Matrix With Vector Stream and Headers")
    void createMatrixAsVectorRows() {
        List<ScalarVector> vectorList = createVectors(3, 10);
        String[] headers = {"Vector A", "Vector B", "Vector C", "Vector D", "Vector E", "Vector F", "Vector G", "Vector H", "Vector I", "Vector J"};
        Matrix matrix = getModelProvider().getMatrixFactory().createMatrix(vectorList.stream(), headers);
        assertEquals(3, matrix.getColumn("Vector A").length());

        assertEquals(matrix.getColumn("Vector B").length(), matrix.getColumn("Vector J").length());
        assertEquals(headers.length, matrix.getColumnCount());
    }

    @Test
    void testCreateMatrixAsVectorColumns() {

        ScalarVector vector1 = generateVector(10, "Vector1");
        ScalarVector vector2 = generateVector(10, "Vector2");
        ScalarVector vector3 = generateVector(10, "Vector3");

        Matrix matrix = getModelProvider().getMatrixFactory().createMatrix(vector1, vector2, vector3);

        assertEquals(3, matrix.getColumnCount());
        assertEquals(10, matrix.getColumn("Vector2").length());
        assertEquals(10, matrix.getColumn(0).length());
        assertEquals(10, matrix.getRowCount());
    }

    @Test
    void testCreateMatrix1() {
    }

    private List<ScalarVector> createVectors(int numberOfVectors, int numberOfRows) {
        List<ScalarVector> vectors = new ArrayList<>();

        int i = 0;
        while (i < numberOfVectors) {
            vectors.add(generateVector(numberOfRows, "vector-" + i));
            i++;
        }

        return vectors;

    }

    private ScalarVector generateVector(int numberOfRows, String name) {
        List<Number> numbers = new ArrayList<>();

        int i = 0;

        while (i < numberOfRows) {
            Random r = new Random(numberOfRows);
            numbers.add(r.nextDouble());
            i++;
        }

        return getModelProvider().getVectorFactory().createScalarVector(name, numbers.stream());
    }
}