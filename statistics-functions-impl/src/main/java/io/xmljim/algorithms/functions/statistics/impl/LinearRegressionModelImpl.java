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

import io.xmljim.algorithms.functions.common.AbstractModel;
import io.xmljim.algorithms.functions.statistics.LinearRegressionModel;
import io.xmljim.algorithms.functions.statistics.provider.StatisticsProvider;
import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

class LinearRegressionModelImpl extends AbstractModel implements LinearRegressionModel {
    public LinearRegressionModelImpl(MatrixParameter matrixParameter) {
        super(StatisticsFunctionTypes.LINEAR_REGRESSION_MODEL.getName(), matrixParameter);

    }

    public LinearRegressionModelImpl(MatrixParameter matrixParameter, ScalarParameter setX, ScalarParameter setY) {
        super(StatisticsFunctionTypes.LINEAR_REGRESSION_MODEL.getName(), matrixParameter, setX, setY);
    }

    public LinearRegressionModelImpl(ScalarVectorParameter vectorX, ScalarVectorParameter vectorY) {
        super(StatisticsFunctionTypes.LINEAR_REGRESSION_MODEL.getName(), vectorX, vectorY);
    }

    private StatisticsProvider getFunctionProvider() {
        StatisticsProvider provider = getFunctionProvider("Statistics");
        return provider;
    }

    @Override
    public ScalarCoefficient getSlopeCoefficient() {
        Coefficient<?> coeff = getCoefficient(StatisticsFunctionTypes.SLOPE.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getInterceptCoefficient() {
        Coefficient<?> coeff = getCoefficient(StatisticsFunctionTypes.INTERCEPT.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getRSquaredCoefficient() {
        Coefficient<?> coeff = getCoefficient(StatisticsFunctionTypes.R_SQUARED.getName());
        return (ScalarCoefficient) coeff;
    }

    @Override
    public ScalarCoefficient getSlopeStandardErrorCoefficient() {
        Coefficient<?> coefficient = getCoefficient(StatisticsFunctionTypes.SLOPE_STD_ERROR.getName());
        return (ScalarCoefficient) coefficient;
    }

    @Override
    public ScalarCoefficient getInterceptStandardErrorCoefficient() {
        Coefficient<?> coefficient = getCoefficient(StatisticsFunctionTypes.INTERCEPT_STD_ERROR.getName());
        return (ScalarCoefficient) coefficient;
    }

    @Override
    public ScalarCoefficient getSlopeTStatisticCoefficient() {
        Coefficient<?> coefficient = getCoefficient(StatisticsFunctionTypes.P_SLOPE.getName());
        return (ScalarCoefficient) coefficient;
    }

    @Override
    public ScalarCoefficient getPSlopeCoefficient() {
        return null;
    }

    @Override
    public void solve() {
        ScalarVector vectorX = getVector(StatisticsNameConstants.X_VARIABLE);
        ScalarVector vectorY = getVector(StatisticsNameConstants.Y_VARIABLE);

        ScalarFunction meanX = getFunctionProvider().getFactory().mean(vectorX, StatisticsNameConstants.X_VARIABLE);
        ScalarFunction meanY = getFunctionProvider().getFactory().mean(vectorY, StatisticsNameConstants.Y_VARIABLE);
        ScalarFunction varianceX = getFunctionProvider().getFactory().variance(vectorX, meanX);
        ScalarFunction covariance = getFunctionProvider().getFactory().covariance(vectorX, vectorY);
        ScalarFunction slope = getFunctionProvider().getFactory().slope(varianceX, covariance);
        ScalarFunction intercept = getFunctionProvider().getFactory().intercept(meanX, meanY, slope);
        ScalarFunction residualSumSquares = getFunctionProvider().getFactory().residualSumOfSquares(vectorX, vectorY, slope, intercept);
        ScalarFunction totalSumSquaresY = getFunctionProvider().getFactory().totalSumOfSquares(vectorY, meanY);
        ScalarFunction totalSumSquaresX = getFunctionProvider().getFactory().totalSumOfSquares(vectorX, meanX);
        ScalarFunction rSquared = getFunctionProvider().getFactory().rSquared(residualSumSquares, totalSumSquaresY);
        ScalarFunction meanSquaredError = getFunctionProvider().getFactory().meanSquaredError(vectorX, vectorY, slope, intercept);
        ScalarFunction slopeStandardError = getFunctionProvider().getFactory().slopeStandardError(totalSumSquaresX, meanSquaredError);
        ScalarFunction interceptStandardError = getFunctionProvider().getFactory().interceptStandardError(meanSquaredError, meanX, totalSumSquaresX, Scalar.of(vectorX.length()));
        ScalarFunction slopeTStatistic = getFunctionProvider().getFactory().slopeTStatistic(slope, slopeStandardError);
        ScalarFunction slopePValue = getFunctionProvider().getFactory().slopePValue(vectorX.length() - 2, slopeTStatistic);

        setCoefficient(StatisticsFunctionTypes.SLOPE, slope);
        setCoefficient(StatisticsFunctionTypes.INTERCEPT, intercept);
        setCoefficient(StatisticsFunctionTypes.R_SQUARED, rSquared);
        setCoefficient(StatisticsFunctionTypes.SLOPE_STD_ERROR, slopeStandardError);
        setCoefficient(StatisticsFunctionTypes.INTERCEPT_STD_ERROR, interceptStandardError);
        setCoefficient(StatisticsFunctionTypes.T_SLOPE, slopeTStatistic);
        setCoefficient(StatisticsFunctionTypes.P_SLOPE, slopePValue);

    }

    private ScalarVector getVector(String variable) {
        ScalarVector vector;

        if (hasParameter(StatisticsNameConstants.MATRIX)) {
            Matrix matrix = getValue(StatisticsNameConstants.MATRIX);
            int defaultColumn = variable.equals(StatisticsNameConstants.X_VARIABLE) ? 0 : 1;
            int column = -1;

            if (hasParameter(StatisticsNameConstants.COLUMN, variable)) {
                column = getValue(StatisticsNameConstants.COLUMN, variable);
            } else {
                column = defaultColumn;
            }

            vector = matrix.getColumn(column);

        } else {
            ScalarVectorParameter vectorParameter = (ScalarVectorParameter) getParameterFromVariable(variable).orElse(null);
            vector = vectorParameter.getValue();
        }

        return vector;
    }
}
