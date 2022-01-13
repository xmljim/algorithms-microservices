package io.xmljim.algorithms.functions.financial;

public interface Balance {
    String getType();

    int getYear();

    double getBalance();

    double getInterestAccrued();

    double getWeightedGrowthRate();
}
