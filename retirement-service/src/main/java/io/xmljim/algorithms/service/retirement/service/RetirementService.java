package io.xmljim.algorithms.service.retirement.service;

import io.xmljim.algorithms.functions.financial.PaymentFrequency;
import io.xmljim.algorithms.functions.financial.RetirementContributionModel;
import io.xmljim.algorithms.functions.financial.RetirementDistributionModel;
import io.xmljim.algorithms.functions.financial.RetirementModel;
import io.xmljim.algorithms.functions.financial.provider.FinancialProvider;
import io.xmljim.algorithms.model.ScalarFunction;
import io.xmljim.algorithms.service.retirement.client.CPIClient;
import io.xmljim.algorithms.service.retirement.client.StocksClient;
import io.xmljim.algorithms.service.retirement.entity.RetirementInput;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import io.xmljim.algorithms.services.entity.stocks.ComputedMarketResult;
import io.xmljim.algorithms.services.entity.stocks.MarketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class RetirementService {

    @Autowired
    FinancialProvider financialProvider;

    @Autowired
    StocksClient stocksClient;

    @Autowired
    CPIClient cpiClient;

    @Value("${retirement.growthRate.year}")
    private Integer growthRateYear;

    @Value("${retirement.treasury.yield}")
    private Double treasuryYield;

    private ScalarFunction getWeightedGrowthRate(final double riskLevel) {
        MarketResponse<ComputedMarketResult> stockGrowthResult = stocksClient.getConsolidatedMarketGrowthRate(growthRateYear);
        double stockRate = stockGrowthResult.getResults().get(0).getData();

        return financialProvider.getFactory().weightedGrowth(stockRate, treasuryYield, riskLevel);
    }

    public ModelResult getContributionModel(final RetirementInput input) {
        double weightedGrowth = getWeightedGrowthRate(input.getInvestmentStyle()).compute().asDouble();
        PaymentFrequency contributionFrequency = input.getContributionFrequency() != null ?
                PaymentFrequency.valueOf(input.getContributionFrequency().toUpperCase()) : PaymentFrequency.SEMI_MONTHLY;

        RetirementContributionModel model = financialProvider.getFactory().retirementContributionModel(input.getAge(), input.getRetirementAge(), input.getCurrentSalary(),
                input.getSelfContributionPct(), input.getEmployerContributionPct(), input.getCurrentRetirementBalance(), input.getColaPct(),
                weightedGrowth, contributionFrequency);

        return new ModelResult(model);
    }

    public ModelResult getDistributionModel(final RetirementInput input) {
        double inflationRate = cpiClient.getAverageInflationRate(20).getResults().get(0).getData();
        int retirementYear = LocalDate.now().getYear() - (input.getRetirementAge() - input.getAge());
        PaymentFrequency distributionFrequency = PaymentFrequency.valueOf(input.getDistributionFrequency().toUpperCase());

        RetirementDistributionModel model = financialProvider.getFactory().retirementDistributionModel(input.getCurrentRetirementBalance(), retirementYear,
                input.getPostRetirementInterestRate(), distributionFrequency, inflationRate, input.getRetirementDuration(), input.getAnnualizedDistribution());

        return new ModelResult(model);
    }

    public ModelResult getRetirementModel(final RetirementInput input) {
        log.info("Service#getRetirementModel RetirementInput: {}", input);
        double weightedGrowth = getWeightedGrowthRate(input.getInvestmentStyle()).compute().asDouble();
        log.info("weightedGrowth: {}", weightedGrowth);
        double inflationRate = cpiClient.getAverageInflationRate(20).getResults().get(0).getData();
        log.info("inflationRate: {}", inflationRate);

        PaymentFrequency contributionFrequency = input.getContributionFrequency() != null ?
                PaymentFrequency.valueOf(input.getContributionFrequency().toUpperCase()) : PaymentFrequency.SEMI_MONTHLY;
        log.info("contributionFrequency: {}", contributionFrequency);
        PaymentFrequency distributionFrequency = input.getDistributionFrequency() != null ?
                PaymentFrequency.valueOf(input.getDistributionFrequency().toUpperCase()) : PaymentFrequency.MONTHLY;
        log.info("distributionFrequency: {}", distributionFrequency);

        RetirementModel model = financialProvider.getFactory().retirementModel(input.getAge(), input.getRetirementAge(), input.getCurrentSalary(),
                input.getCurrentRetirementBalance(), input.getSelfContributionPct(), input.getEmployerContributionPct(), input.getColaPct(),
                weightedGrowth, contributionFrequency, input.getPostRetirementInterestRate(), distributionFrequency, inflationRate,
                input.getRetirementDuration(), input.getIncomeReplacementPct());
        log.info("RetirementModel: {}", model);

        return new ModelResult(model);

    }
}
