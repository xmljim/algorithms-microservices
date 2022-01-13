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

import io.xmljim.algorithms.functions.common.provider.AbstractFunctionFactory;
import io.xmljim.algorithms.functions.statistics.StatisticsFunctions;
import io.xmljim.algorithms.functions.statistics.LinearRegressionModel;

import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

class StatisticsFunctionsImpl extends AbstractFunctionFactory implements StatisticsFunctions {
    @Override
    public String getFactoryName() {
        return "Statistics";
    }

    @Override
    public ScalarFunction sum(final ScalarVector vector) {
        return sum(vector, vector.getVariable());
    }

    @Override
    public ScalarFunction sum(final ScalarVector vector, final String variable) {
        ScalarVectorParameter parameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        return new SumFunction(parameter, variable);
    }

    @Override
    public ScalarFunction mean(final ScalarVector vector) {
        return mean(vector, vector.getVariable());
    }

    @Override
    public ScalarFunction mean(final ScalarVector vector, final String variable) {
        ScalarVectorParameter parameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        return new MeanFunction(parameter, variable);
    }

    @Override
    public ScalarFunction median(final ScalarVector vector) {
        return median(vector, vector.getVariable());
    }

    @Override
    public ScalarFunction median(final ScalarVector vector, final String variable) {
        ScalarVectorParameter parameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        return new MedianFunction(parameter, variable);
    }

    @Override
    public ScalarFunction variance(final ScalarVector vector) {
        return variance(vector, vector.getVariable());
    }

    @Override
    public ScalarFunction variance(final ScalarVector vector, final String variable) {
        ScalarVectorParameter parameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        return new VarianceFunction(parameter, variable);
    }

    @Override
    public ScalarFunction variance(final ScalarVector vector, final ScalarFunction meanFunction) {

        return variance(vector, meanFunction, meanFunction.getVariable());
    }

    @Override
    public ScalarFunction variance(final ScalarVector vector, final ScalarFunction meanFunction, final String variable) {
        ScalarVectorParameter vectorParameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        ScalarFunctionParameter functionParameter = getModelProvider().getParameterFactory().createParameter(meanFunction.getName(), variable, meanFunction);
        return new VarianceFunction(vectorParameter, functionParameter, variable);
    }

    @Override
    public ScalarFunction standardDeviation(final ScalarFunction varianceFunction) {
        return standardDeviation(varianceFunction, varianceFunction.getVariable());
    }

    @Override
    public ScalarFunction standardDeviation(final ScalarVector vectorParameter) {
        return standardDeviation(vectorParameter, vectorParameter.getVariable());
    }

    @Override
    public ScalarFunction standardDeviation(final ScalarFunction varianceFunction, final String variable) {
        ScalarFunctionParameter functionParameter = getModelProvider().getParameterFactory().createParameter(varianceFunction.getName(), variable, varianceFunction);
        return new StandardDeviationFunction(functionParameter, variable);
    }

    @Override
    public ScalarFunction standardDeviation(final ScalarVector vector, final String variable) {
        ScalarVectorParameter vectorParameter = getModelProvider().getParameterFactory().createParameter(vector.getName(), variable, vector);
        return new StandardDeviationFunction(vectorParameter, variable);
    }

    @Override
    public ScalarFunction covariance(final ScalarVector vectorX, final ScalarVector vectorY) {
        ScalarVectorParameter vectorXParam = getModelProvider().getParameterFactory().createParameter(vectorX.getName(), StatisticsNameConstants.X_VARIABLE, vectorX);
        ScalarVectorParameter vectorYParam = getModelProvider().getParameterFactory().createParameter(vectorY.getName(), StatisticsNameConstants.Y_VARIABLE, vectorY);
        return new CovarianceFunction(vectorXParam, vectorYParam);
    }

    public ScalarFunction covariance(final ScalarVector vectorX, final ScalarVector vectorY, ScalarFunction meanX, ScalarFunction meanY) {
        ScalarVectorParameter vectorXParam = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.X_VARIABLE, vectorX);
        ScalarVectorParameter vectorYParam = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.Y_VARIABLE, vectorY);
        ScalarFunctionParameter meanXParam = getModelProvider().getParameterFactory().createParameter(meanX.getName(), StatisticsNameConstants.X_VARIABLE, meanX);
        ScalarFunctionParameter meanYParam = getModelProvider().getParameterFactory().createParameter(meanY.getName(), StatisticsNameConstants.Y_VARIABLE, meanY);

        return new CovarianceFunction(vectorXParam, vectorYParam, meanXParam, meanYParam);
    }

    @Override
    public ScalarFunction slope(final ScalarFunction varianceX, final ScalarFunction covariance) {
        ScalarFunctionParameter varianceXParameter = getModelProvider().getParameterFactory().createParameter(varianceX.getName(), StatisticsNameConstants.X_VARIABLE, varianceX);
        ScalarFunctionParameter covarianceParameter = getModelProvider().getParameterFactory().createParameter(covariance);

        return new SlopeFunction(covarianceParameter, varianceXParameter);
    }

    @Override
    public ScalarFunction intercept(final ScalarFunction meanX, final ScalarFunction meanY, final ScalarFunction slopeFunction) {
        ScalarFunctionParameter meanXParam = getModelProvider().getParameterFactory().createParameter(meanX.getName(),  StatisticsNameConstants.X_VARIABLE, meanX);
        ScalarFunctionParameter meanYParam = getModelProvider().getParameterFactory().createParameter(meanY.getName(),  StatisticsNameConstants.Y_VARIABLE, meanY);
        ScalarFunctionParameter slopeParam = getModelProvider().getParameterFactory().createParameter(slopeFunction);

        return new InterceptFunction(meanXParam, meanYParam, slopeParam);
    }

    @Override
    public ScalarFunction residualSumOfSquares(final ScalarVector vectorX, final ScalarVector vectorY, final ScalarFunction slopeFunction, final ScalarFunction interceptFunction) {
        ScalarVectorParameter vectorXParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.X_VARIABLE, vectorX);
        ScalarVectorParameter vectorYParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.Y_VARIABLE, vectorY);
        ScalarFunctionParameter slopeParameter = getModelProvider().getParameterFactory().createParameter(slopeFunction);
        ScalarFunctionParameter interceptParameter = getModelProvider().getParameterFactory().createParameter(interceptFunction);

        return new ResidualSumOfSquaresFunction(vectorXParameter, vectorYParameter, slopeParameter, interceptParameter);
    }

    @Override
    public ScalarFunction totalSumOfSquares(final ScalarVector vector, final ScalarFunction mean) {
        ScalarVectorParameter vectorXParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, vector);
        ScalarFunctionParameter meanYParameter = getModelProvider().getParameterFactory().createParameter(mean.getName(), mean);

        return new TotalSumOfSquaresFunction(vectorXParameter, meanYParameter);
    }

    @Override
    public ScalarFunction rSquared(final ScalarFunction residualVarianceFunction, final ScalarFunction totalVarianceFunction) {
        ScalarFunctionParameter residualVarianceParameter = getModelProvider().getParameterFactory().createParameter(residualVarianceFunction);
        ScalarFunctionParameter totalVarianceParameter = getModelProvider().getParameterFactory().createParameter(totalVarianceFunction);

        return new RSquaredFunction(residualVarianceParameter, totalVarianceParameter);
    }

    @Override
    public ScalarFunction meanSquaredError(final ScalarVector vectorX, final ScalarVector vectorY, final ScalarFunction slopeFunction, final ScalarFunction interceptFunction) {
        ScalarVectorParameter vectorXParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.X_VARIABLE, vectorX);
        ScalarVectorParameter vectorYParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.VECTOR, StatisticsNameConstants.Y_VARIABLE, vectorY);
        ScalarFunctionParameter slopeParameter = getModelProvider().getParameterFactory().createParameter(slopeFunction.getName(), slopeFunction);
        ScalarFunctionParameter interceptParameter = getModelProvider().getParameterFactory().createParameter(interceptFunction.getName(), interceptFunction);

        return new MeanSquaredErrorFunction(vectorXParameter, vectorYParameter, slopeParameter, interceptParameter);
    }

    @Override
    public ScalarFunction slopeStandardError(final ScalarFunction sumOfSquaresX, final ScalarFunction meanSquaredError) {
        ScalarFunctionParameter sumSquaresXParameter = getModelProvider().getParameterFactory().createParameter(sumOfSquaresX.getName(), StatisticsNameConstants.X_VARIABLE, sumOfSquaresX);
        ScalarFunctionParameter meanSquareErrorParameter = getModelProvider().getParameterFactory().createParameter(meanSquaredError.getName(), meanSquaredError);

        return new SlopeStandardErrorFunction(sumSquaresXParameter, meanSquareErrorParameter);
    }

    @Override
    public ScalarFunction interceptStandardError(final ScalarFunction meanSquaredErrorFunction, final ScalarFunction meanXFunction, final ScalarFunction sumOfSquaresXFunction, final Scalar count) {
        ScalarFunctionParameter meanSquaredErrorParameter = getModelProvider().getParameterFactory().createParameter(meanSquaredErrorFunction.getName(), meanSquaredErrorFunction);
        ScalarFunctionParameter meanXParameter = getModelProvider().getParameterFactory().createParameter(meanXFunction.getName(), StatisticsNameConstants.X_VARIABLE, meanXFunction);
        ScalarFunctionParameter sumOfSquaresXParameter = getModelProvider().getParameterFactory().createParameter(sumOfSquaresXFunction.getName(), StatisticsNameConstants.X_VARIABLE, sumOfSquaresXFunction);
        ScalarParameter countParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.COUNT, count);

        return new InterceptStandardErrorFunction(meanSquaredErrorParameter, meanXParameter, sumOfSquaresXParameter, countParameter);
    }

    @Override
    public ScalarFunction slopeTStatistic(final ScalarFunction slopeFunction, final ScalarFunction slopeStandardError) {
        ScalarFunctionParameter slopeParameter = getModelProvider().getParameterFactory().createParameter(slopeFunction);
        ScalarFunctionParameter slopeSEParameter = getModelProvider().getParameterFactory().createParameter(slopeStandardError);

        return new TStatisticSlopeFunction(slopeParameter, slopeSEParameter);
    }

    @Override
    public ScalarFunction slopePValue(final int degreesOfFreedom, final ScalarFunction slopeTStatistic) {
        ScalarParameter dfParam = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.DEGREES_OF_FREEDOM, Scalar.of(degreesOfFreedom));
        ScalarFunctionParameter slopeTParam = getModelProvider().getParameterFactory().createParameter(slopeTStatistic);
        return new PSlopeFunction(dfParam, slopeTParam);
    }

    @Override
    public LinearRegressionModel linearRegression(final Matrix matrix) {
        MatrixParameter matrixParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.MATRIX, matrix);
        return new LinearRegressionModelImpl(matrixParameter);
    }

    @Override
    public LinearRegressionModel linearRegression(final Matrix matrix, final int setX, final int setY) {
        MatrixParameter matrixParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.MATRIX, matrix);
        ScalarParameter columnXParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.COLUMN, StatisticsNameConstants.X_VARIABLE, setX);
        ScalarParameter columnYParameter = getModelProvider().getParameterFactory().createParameter(StatisticsNameConstants.COLUMN, StatisticsNameConstants.Y_VARIABLE, setY);
        return new LinearRegressionModelImpl(matrixParameter, columnXParameter, columnYParameter);
    }

    @Override
    public LinearRegressionModel linearRegression(final ScalarVector vectorX, final ScalarVector vectorY) {
        ScalarVectorParameter vectorXParameter = getModelProvider().getParameterFactory().createParameter(vectorX.getName() == null ? StatisticsNameConstants.VECTOR: vectorX.getName(),
                StatisticsNameConstants.X_VARIABLE, vectorX);
        ScalarVectorParameter vectorYParameter = getModelProvider().getParameterFactory().createParameter(vectorY.getName() == null ? StatisticsNameConstants.VECTOR: vectorY.getName(),
                StatisticsNameConstants.Y_VARIABLE, vectorY);
        return new LinearRegressionModelImpl(vectorXParameter, vectorYParameter);
    }
}
