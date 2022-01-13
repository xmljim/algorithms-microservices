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

package io.xmljim.algorithms.functions.statistics;

import io.xmljim.algorithms.model.Model;
import io.xmljim.algorithms.model.ScalarCoefficient;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.model.util.Scalar;

/**
 * A statistical model that solves for the linear relationship between two variables
 * with the equation:
 * <p>
 *     <pre>y = &#x03B1;x + &#x03B2</pre>
 * </p>
 * <p>Where:</p>
 * <p>y is the predicted value</p>
 * <p>x is the observed independent variable value</p>
 * <p>&#x03B1; is the slope (increase/decrease) of the relationship between x and y</p>
 * <p>&#x03B2; is the y-intercept constant</p>
 */
public interface LinearRegressionModel extends Model {

        /**
         * Return the slope coefficient (&#x03B2) computed from the function
         * {@link StatisticsFunctions#slope(ScalarFunction, ScalarFunction)}
         * @return the slope coefficient
         */
        ScalarCoefficient getSlopeCoefficient();

        ScalarCoefficient getInterceptCoefficient();

        ScalarCoefficient getRSquaredCoefficient();

        ScalarCoefficient getSlopeStandardErrorCoefficient();

        ScalarCoefficient getInterceptStandardErrorCoefficient();

        ScalarCoefficient getSlopeTStatisticCoefficient();

        ScalarCoefficient getPSlopeCoefficient();

        default Scalar getSlope() {
            ScalarCoefficient slopeCoefficient = getSlopeCoefficient();
            if (slopeCoefficient != null) {
                return slopeCoefficient.getValue();
            }
            return null;
        }

        default Scalar getIntercept() {
            ScalarCoefficient interceptCoefficient = getInterceptCoefficient();
            if (interceptCoefficient != null) {
                return interceptCoefficient.getValue();
            }
            return null;
        }

        default Scalar getRSquared() {
            ScalarCoefficient rsquaredCoefficient = getRSquaredCoefficient();
            if (rsquaredCoefficient != null) {
                return rsquaredCoefficient.getValue();
            }
            return null;
        }

        default Scalar getSlopeStandardError() {
            ScalarCoefficient slopeSECoefficient = getSlopeStandardErrorCoefficient();
            if (slopeSECoefficient != null) {
                return slopeSECoefficient.getValue();
            }
            return null;
        }

        default Scalar getInterceptStandardError() {
            ScalarCoefficient interceptSECoefficient = getInterceptStandardErrorCoefficient();
            if (interceptSECoefficient != null) {
                return interceptSECoefficient.getValue();
            }
            return null;
        }

        default Scalar getSlopeTStatistic() {
            ScalarCoefficient slopeTCoefficient = getSlopeTStatisticCoefficient();
            if (slopeTCoefficient != null) {
                return slopeTCoefficient.getValue();
            }
            return null;
        }

        default Scalar getPSlopeValue() {
            ScalarCoefficient pValueCoefficient = getPSlopeCoefficient();
            if (pValueCoefficient != null) {
                return pValueCoefficient.getValue();
            }
            return null;
        }

        default Scalar predict(Scalar independentValue) {
            return Scalar.of(independentValue.asDouble() * getSlope().asDouble() + getIntercept().asDouble());
        }
}
