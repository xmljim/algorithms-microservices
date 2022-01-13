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

package io.xmljim.algorithms.functions.financial;

import io.xmljim.algorithms.model.Model;

import io.xmljim.algorithms.model.*;
import io.xmljim.algorithms.model.util.Scalar;

import java.util.List;

/**
 * A <em>metamodel</em> that encompasses the estimations from both the {@link RetirementContributionModel}
 * and {@link RetirementDistributionModel} into a single set of estimations for retirement starting from
 * now until all funds are depleted
 */
public interface RetirementModel extends Model {


    /**
     * Return the coefficient holding the entire contribution/distribution timeline balances over time
     * @return the coefficient holding the entire contribution/distribution timeline balances over time
     */
    public Coefficient<List<Balance>> getRetirementTimelineCoefficient();

    /**
     * Coefficient for the percentage of income the retirement account will replace at retirement
     * @return Coefficient for the percentage of income the retirement account will replace at retirement
     */
    public ScalarCoefficient getRetirementIncomePctCoefficient();

    /**
     * Coefficient estimating the year when all retirement funds will be depleted
     * @return Coefficient estimating the year when all retirement funds will be depleted
     */
    public ScalarCoefficient getRetirementBalanceDepletionYearCoefficient();

    public ScalarCoefficient getRetirementYearCoefficient();

    public ScalarCoefficient getBalanceAtRetirementCoefficient();

    public ScalarCoefficient getTotalInterestCoefficient();

    public ScalarCoefficient getTotalDistributionsCoefficient();

    public ScalarCoefficient getTotalSelfContributionsCoefficient();

    public ScalarCoefficient getTotalEmployerContributionsCoefficient();

    public ScalarCoefficient getTotalDistributionYearsCoefficient();

    public ScalarCoefficient getBaseAnnualDistributionCoefficient();

    RetirementContributionModel getContributionModel();

    RetirementDistributionModel getDistributionModel();



    default List<Balance> getRetirementTimeline() {
        return getRetirementTimelineCoefficient().getValue();
    }

    default Scalar getRetirementIncomePct() {
        return getRetirementIncomePctCoefficient().getValue();
    }

    default Scalar getRetirementBalanceDepletionYear() {
        return getRetirementBalanceDepletionYearCoefficient().getValue();
    }

    default Scalar getRetirementYear() {
        return getRetirementYearCoefficient().getValue();
    }

    default Scalar getBalanceAtRetirement() {
        return getBalanceAtRetirementCoefficient().getValue();
    }

    default Scalar getTotalInterest() {
        return getTotalInterestCoefficient().getValue();
    }

    default Scalar getTotalDistributions() {
        return getTotalDistributionsCoefficient().getValue();
    }

    default Scalar getTotalSelfContributions() {
        return getTotalSelfContributionsCoefficient().getValue();
    }

    default Scalar getTotalEmployerContributions() {
        return getTotalEmployerContributionsCoefficient().getValue();
    }

    default Scalar getTotalDistributionYears() {
        return getTotalDistributionYearsCoefficient().getValue();
    }

    default Scalar getBaseAnnualContribution() {
        return getBaseAnnualDistributionCoefficient().getValue();
    }
}
