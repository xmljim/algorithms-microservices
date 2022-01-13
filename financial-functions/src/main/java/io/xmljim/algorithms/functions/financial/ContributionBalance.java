package io.xmljim.algorithms.functions.financial;

public interface ContributionBalance extends Balance {

    double getSelfContributionPct();

    double getEmployerContributionPct();

    double getCurrentSalary();

    double getColaPct();

    double getEstimatedSelfContribution();

    double getEstimatedEmployerContribution();

    PaymentFrequency getContributionFrequency();
}
