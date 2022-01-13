package io.xmljim.algorithms.service.retirement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RetirementInput implements Serializable {
    private int age;
    private int retirementAge;
    private double currentSalary;
    private double colaPct;
    private double currentRetirementBalance;
    private double selfContributionPct;
    private double employerContributionPct;
    private double investmentStyle;
    private String contributionFrequency;
    private double postRetirementInterestRate;
    private String distributionFrequency;
    private int retirementDuration;
    private double incomeReplacementPct;
    private double annualizedDistribution;
}
