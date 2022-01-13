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
import io.xmljim.algorithms.model.provider.MatrixFactory;
import io.xmljim.algorithms.model.provider.ModelProvider;

import java.util.stream.Stream;

class MatrixFactoryImpl implements MatrixFactory {
    private static final String DEFAULT_NAME = "matrix";

    private final ModelProvider modelProvider;

    public MatrixFactoryImpl(ModelProvider modelProvider) {
        this.modelProvider = modelProvider;
    }

    @Override
    public Matrix createMatrix(final Stream<ScalarVector> rows, final String... columnHeaders) {
        return new BaseMatrixImpl(DEFAULT_NAME, modelProvider, rows, columnHeaders);
    }

    @Override
    public Matrix createMatrix(final ScalarVector... columns) {
        return new BaseMatrixImpl(DEFAULT_NAME, modelProvider, columns);
    }

    @Override
    public Matrix createMatrix(final Number[][] numberArray, final String... columnHeaders) {
        return new BaseMatrixImpl(DEFAULT_NAME, modelProvider, numberArray, columnHeaders);
    }
}
