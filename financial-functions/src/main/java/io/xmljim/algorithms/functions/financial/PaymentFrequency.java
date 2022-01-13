package io.xmljim.algorithms.functions.financial;

public enum PaymentFrequency {

    ANNUAL(1),
    SEMI_ANNUAL(2),
    QUARTERLY(4),
    MONTHLY(12),
    SEMI_MONTHLY(24),
    BI_WEEKLY(26),
    WEEKLY(52)
    ;

    private int annualFrequency;

    public int getAnnualFrequency() {
        return annualFrequency;
    }

    PaymentFrequency(int annualFrequency) {
        this.annualFrequency = annualFrequency;
    }
}
