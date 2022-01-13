package io.xmljim.algorithms.service.stocks.service;

import io.xmljim.algorithms.service.stocks.client.CPIClient;
import io.xmljim.algorithms.service.stocks.client.StatisticsClient;
import io.xmljim.algorithms.service.stocks.entity.MarketData;
import io.xmljim.algorithms.service.stocks.repository.StocksRepository;
import io.xmljim.algorithms.services.entity.DefaultServiceResult;
import io.xmljim.algorithms.services.entity.cpi.CPIComputedResult;
import io.xmljim.algorithms.services.entity.cpi.CPIResponse;
import io.xmljim.algorithms.services.entity.statistics.DataSet;
import io.xmljim.algorithms.services.entity.statistics.DataVector;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import io.xmljim.algorithms.services.entity.stocks.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class StocksService {

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private CPIClient cpiClient;

    @Autowired
    private StatisticsClient statisticsClient;

    /**
     * Return a list of stock markets
     * @return a list of stock markets
     */
    @Cacheable("stock-markets")
    public List<StockMarketResult> getDistinctMarkets() {
        List<String> distinctMarketList = stocksRepository.findDistinctMarkets();

        List<StockMarket> marketEntities = new ArrayList<>();

        distinctMarketList.forEach(market -> {
            List<MarketData> marketDataList = stocksRepository.findByMarket(market, Sort.by(Sort.Direction.ASC, "year"));
            marketEntities.add(new StockMarket(marketDataList.get(0).getYear(), market));
        });

        List<StockMarketResult> stockMarketResults = marketEntities.stream()
                .map(stockMarket -> new StockMarketResult("Stock Market: [" + stockMarket.getMarket() + "]", stockMarket))
                .collect(Collectors.toList());
        return stockMarketResults;
    }

    /**
     * Return a market's entire history
     * @param market the market name
     * @return a list of market history entities
     */
    @Cacheable("history-by-market")
    public List<MarketEntityResult> getHistoryByMarket(String market) {
        List<MarketData> marketData = stocksRepository.findByMarket(market, Sort.by(Sort.Direction.ASC, "year"));

        List<MarketEntity> marketEntities = marketData.stream()
                .map(md -> new MarketEntity(md.getMarket(), md.getYear(), md.getOpen(), md.getClose()))
                .collect(Collectors.toList());

        return marketEntities.stream()
                .map(marketEntity -> new MarketEntityResult(buildMarketDescription(marketEntity), marketEntity))
                .collect(Collectors.toList());
    }

    /**
     * Return a specific stock market record for a given year
     * @param market the market name
     * @param year the year
     * @return a MarketEntity if the record is found, null otherwise
     */
    @Cacheable("history-by-market-and-year")
    public MarketEntityResult getHistoryByMarketAndYear(String market, int year) {
        Optional<MarketData> marketData = stocksRepository.findByYearAndMarket(year, market);
        log.info("getHistoryByMarketAndYear: {} {} {}",market, year, marketData);
        if (marketData.isPresent()) {
            MarketEntity record = marketData.get().convert();
            return new MarketEntityResult(buildMarketDescription(record), new MarketEntity(record.getMarket(), record.getYear(), record.getOpen(), record.getClose()));
        }

        return null;
    }

    /**
     * Return all stock markets' history data, starting from a given year
     * @param year the start year, or, if the year precedes the first year, it will return first year onward
     * @return all stock markets' historical data from a given year onward. Data will be sorted by market and year
     */
    @Cacheable("market-history-from-year")
    public List<MarketEntityResult> getMarketHistoryFromYear(int year) {
        List<MarketData> marketData = stocksRepository.findAllFromYear(year, Sort.by(Sort.Direction.ASC, "market", "year"));

        return marketData.stream()
                .map(md -> new MarketEntityResult(buildMarketDescription(md.convert()), md.convert()))
                .collect(Collectors.toList());
    }

    /**
     * Return a given stock market's history data, starting from a given year
     * @param market the market
     * @param year the start year, or, if the year precedes the first year of the market, it will return the first year onward
     * @return a given stock market's historical data for a given year onward. Data will be sorted by year
     */
    @Cacheable("history-by-market-from-year")
    public List<MarketEntityResult> getHistoryByMarketFromYear(String market, int year) {
        List<MarketData> marketData = stocksRepository.findMarketFromYear(market, year);

        return marketData.stream()
                .map(md -> md.convert())
                .map(me -> new MarketEntityResult(buildMarketDescription(me), me))
                .collect(Collectors.toList());
    }

    @Cacheable("market-history-between")
    public List<MarketEntityResult> getMarketHistoryBetween(int startYear, int endYear) {
        List<MarketData> marketData = stocksRepository.findByYearBetween(startYear, endYear);

        return marketData.stream()
                .map(md -> md.convert())
                .map(me -> new MarketEntityResult(buildMarketDescription(me), me))
                .collect(Collectors.toList());
    }

    @Cacheable("history-by-market-between")
    public List<MarketEntityResult> getHistoryByMarketBetween(String market, int yearStart, int yearEnd) {
        List<MarketData> marketData = stocksRepository.findByMarketEqualsAndYearIsBetween(market, yearStart, yearEnd, Sort.by(Sort.Direction.ASC, "market", "year"));

        return marketData.stream()
                .map(md -> md.convert())
                .map(me -> new MarketEntityResult(buildMarketDescription(me), me))
                .collect(Collectors.toList());
    }

    /**
     * Return a consolidated market view from a given year.
     * @param fromYear the starting year
     * @return a list of consolidated market data.
     */
    @Cacheable("consolidated-market-history")
    public List<ConsolidatedMarketEntityResult> getConsolidatedMarketHistory(final int fromYear) {
        List<StockMarketResult> markets = getDistinctMarkets();

        List<List<MarketData>> marketDataList = markets.stream()
                .map(stockMarketResult -> stocksRepository.findMarketFromYear(stockMarketResult.getData().getMarket(), fromYear))
                .collect(Collectors.toList());

        List<ConsolidatedMarketEntity> consolidatedMarketEntities = getConsolidatedMarketData(marketDataList);

        return consolidatedMarketEntities.stream()
                .map(consolidatedMarketEntity -> new ConsolidatedMarketEntityResult("Consolidated Market Data [" + consolidatedMarketEntity.getYear() + "]",
                        consolidatedMarketEntity))
                .collect(Collectors.toList());
    }

    private List<ConsolidatedMarketEntity> getConsolidatedMarketData(List<List<MarketData>> markets) {
        //We'll make sure that we're only aggregating results for years that all markets have entries for
        //This ensures that we're creating consolidated history values that are comparable year over year
        //To do this, we need to find the earliest year value that are common to all of the markets
        //Similarly, we'll find the last year common to all of them.

        //short circuit if any of the markets is empty
        boolean isAnyMarketEmpty = markets.stream().filter(List::isEmpty).count() > 0;
        if (isAnyMarketEmpty) {
            return Collections.emptyList();
        }

        int minValue  = markets.stream()
                .mapToInt(market -> market.stream().mapToInt(m -> m.getYear()).min().orElse(0))
                .max().orElse(0);

        int maxValue = markets.stream()
                .mapToInt(market -> market.stream().mapToInt(m -> m.getYear()).max().orElse(0))
                .min().orElse(0);

        //filter the lists to only include market histories that have the same minimum year in
        //all lists. This will ensure that we're consolidating for each consistently
        List<List<MarketData>> listValues = markets.stream()
                .map(list -> {
                    return list.stream().sorted((a, b) -> a.getYear().compareTo(b.getYear()))
                            .filter(market -> market.getYear() >= minValue).collect(Collectors.toList());
                    }).collect(Collectors.toList());

        //now we'll create a map that is keyed by year, and contains a set of MarketHistories
        //that we can aggregate against
        Map<Integer, Set<MarketData>> yearMarketMap = new LinkedHashMap<>();

        //collect the market history from each market and collect to a set of values for the given year
        IntStream.range(0, maxValue - minValue + 1).forEach(i -> {
            Set<MarketData> set = listValues.stream().map(marketList -> marketList.get(i)).collect(Collectors.toSet());
            yearMarketMap.put(minValue + i, set);
        });

        //since we're handling creation and aggregation in a lambda,
        //variables have be intrinsically be final.  Since these values
        //are volatile, Atomics all allow us to update the variables
        //without violating the lambda requirements...
        final AtomicReference<Double> netChange = new AtomicReference<>(0.0);
        final AtomicReference<Double> previousClose = new AtomicReference<>(0.0);
        final AtomicReference<Double> baseStart = new AtomicReference<>(0.0);

        //create the final list of consolidated market histories. Each entry is keyed by
        //year, and the value is the set of market histories for each market for that year
        //With that we can create an aggregate of the values and calculate some additional
        //values needed for further analysis

        List<ConsolidatedMarketEntity> consolidated = yearMarketMap.entrySet().stream()
                .map(entry -> {
                    double yearOpen = entry.getValue().stream().mapToDouble(m -> m.getOpen()).sum();
                    double yearClose = entry.getValue().stream().mapToDouble(m -> m.getClose()).sum();
                    double yearNetChange = previousClose.get() == 0 ? yearClose - yearOpen : yearClose - previousClose.get();
                    double pctChange = previousClose.get() == 0 ? 100.0 : yearNetChange / previousClose.get();
                    //we'll aggregate the change in value using the first year's open as the base for everything
                    //if we're on the first entry, then we'll use the yearOpen, otherwise we'll calculate from
                    //the baseStart value
                    double aggregateChange = baseStart.get() == 0.0 ? yearClose - yearOpen : yearClose - baseStart.get();

                    //we'll adjust the aggregate change for inflation, using the last entry's year as the
                    //basis for evaluation.  So if the current year is 1995, and the last year in our list
                    //is 2020, then we'll adjust the value to 2020 dollars

                    //use the CPIClient to get the data for us:
                    CPIResponse<CPIComputedResult> inflationResponse = cpiClient.getInflationMultiplier(entry.getKey(), maxValue);
                    double cpiMultiplier = inflationResponse.getResults().get(0).getData();
                    double adjustedAggregateChange = aggregateChange * cpiMultiplier;

                    //calculate the log(base10) values of the aggregate change. This will allow
                    //for arriving at the _rate_ of change using a standard OLS regression.
                    //The rationale is that net change (growth) is exponential, i.e., the
                    //rate of change is a compound of previous change (like interest). So,
                    //using the log of the aggregate values gives us a set of points that
                    //can be evaluated in a linear equation like OLS. We can then use the
                    //slope coefficient to calculate the rate of change simply by converting
                    //it to 10^[slope] to get the best fit rate of change
                    double logAggregateChange = Math.log10(aggregateChange);
                    double logAdjustedAggregateChange = Math.log10(adjustedAggregateChange);

                    //update for the next iteration
                    previousClose.set(yearClose);
                    netChange.set(aggregateChange);
                    if (baseStart.get() == 0) {
                        baseStart.set(yearOpen);
                    }

                    //Create and return our results for the year
                    ConsolidatedMarketEntity cmh = new ConsolidatedMarketEntity();
                    cmh.setYear(entry.getKey());
                    cmh.setOpen(yearOpen);
                    cmh.setClose(yearClose);
                    cmh.setNetChange(yearNetChange);
                    cmh.setPctChange(pctChange);
                    cmh.setAggregateChange(aggregateChange);
                    cmh.setAdjustedAggregateChange(adjustedAggregateChange);
                    cmh.setLogAggregateChange(logAggregateChange);
                    cmh.setLogAdjustedAggregateChange(logAdjustedAggregateChange);

                    return cmh;
                }).collect(Collectors.toList());

        return consolidated;
    }

    public ComputedMarketResult getConsolidatedMarketGrowthRate(final int fromYear) {
        List<ConsolidatedMarketEntityResult> marketHistories = getConsolidatedMarketHistory(fromYear);

        List<Number> years = new ArrayList<>();
        List<Number> logNetChange = new ArrayList<>();

        marketHistories.forEach(cmer -> {
            if (cmer.getData().getLogAggregateChange() != Double.NaN) {
                years.add(cmer.getData().getYear());
                logNetChange.add((cmer.getData().getLogAggregateChange()));
            }
        });

        DataVector vectorX = new DataVector("x", "years", years);
        DataVector vectorY = new DataVector("y", "logNetChange", logNetChange);
        DataSet dataSet = new DataSet();
        dataSet.addDataVector(vectorX);
        dataSet.addDataVector(vectorY);

        log.info("DataSet: {}", dataSet);

        ModelResult modelResult = statisticsClient.getLinearRegressionModel(dataSet);

        log.info("ModelResult: {}", modelResult);

        double slopeCoefficient = modelResult.getValueByName("slope");
        return new ComputedMarketResult("Consolidated Market Growth Rate from " + fromYear, Math.pow(10, slopeCoefficient) - 1);
    }

    private String buildMarketDescription(MarketEntity marketData) {
        StringBuilder builder = new StringBuilder();
        builder.append("Market data for [")
                .append(marketData.getMarket())
                .append("] for year [")
                .append(marketData.getYear())
                .append("]");
        return builder.toString();
    }

}
