package io.xmljim.algorithms.service.stocks.controller;

import io.xmljim.algorithms.service.stocks.service.StocksService;
import io.xmljim.algorithms.services.entity.date.PeriodRange;
import io.xmljim.algorithms.services.entity.stocks.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${services.dependencies.stocks.path}")
public class StocksController {

    @Autowired
    private StocksService stocksService;

    @GetMapping("${services.dependencies.stocks.endpoints.get-markets.path}")
    public MarketResponse<StockMarketResult> getMarkets() {
        List<StockMarketResult> results = stocksService.getDistinctMarkets();
        return new MarketResponse<>(results, null);
    }

    @GetMapping("${services.dependencies.stocks.endpoints.market-history.path}")
    public MarketResponse<MarketEntityResult> getMarketHistory(@PathVariable("market") String market) {
        List<MarketEntityResult> results = stocksService.getHistoryByMarket(market);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();

        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));
    }

    @GetMapping("/markets/{market}/{year}")
    public MarketResponse<MarketEntityResult> getMarketHistoryForYear(@PathVariable("market") String market, @PathVariable("year") int year) {
        MarketEntityResult results = stocksService.getHistoryByMarketAndYear(market, year);
        return new MarketResponse<>(results, results != null ? PeriodRange.forYear(year): null);
    }

    @GetMapping("/markets/{market}/history/{year}")
    public MarketResponse<MarketEntityResult> getMarketHistoryFromYear(@PathVariable("market") String market, @PathVariable("year") int year) {

        List<MarketEntityResult> results = stocksService.getHistoryByMarketFromYear(market, year);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();
        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));
    }

    @GetMapping("/markets/{market}/history/{yearStart}/{yearEnd}")
    public MarketResponse<MarketEntityResult> getHistoryByMarketAndBetweenYears(@PathVariable("market") String market,
                                                                                @PathVariable("yearStart") int yearStart,
                                                                                @PathVariable("yearEnd") int yearEnd) {
        List<MarketEntityResult> results = stocksService.getHistoryByMarketBetween(market, yearStart, yearEnd);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();
        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));
    }

    @GetMapping("/markets/history/{year}")
    public MarketResponse<MarketEntityResult> getAllMarketHistoriesFromYear(@PathVariable("year") int year) {

        List<MarketEntityResult> results = stocksService.getMarketHistoryFromYear(year);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();
        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));
    }

    @GetMapping("/markets/history/{yearStart}/{yearEnd}")
    public MarketResponse<MarketEntityResult> getAllMarketHistoriesBetweenYears(@PathVariable("yearStart") int yearStart,
                                                                               @PathVariable("yearEnd") int yearEnd) {

        List<MarketEntityResult> results = stocksService.getMarketHistoryBetween(yearStart, yearEnd);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();
        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));

    }

    @GetMapping("/markets/consolidated/{fromYear}")
    public MarketResponse<ConsolidatedMarketEntityResult> getConsolidatedMarketHistory(@PathVariable("fromYear") int fromYear) {
        List<ConsolidatedMarketEntityResult> results = stocksService.getConsolidatedMarketHistory(fromYear);
        int startYear = results.size() == 0 ? -1 : results.get(0).getData().getYear();
        int endYear = results.size() == 0 ? -1 : results.get(results.size() - 1).getData().getYear();
        return new MarketResponse<>(results, PeriodRange.betweenYears(startYear, endYear));
    }

    @GetMapping("/markets/consolidated/growth/{fromYear}")
    @Cacheable("consolidatedGrowthRate")
    public MarketResponse<ComputedMarketResult> getConsolidateMarketGrowthRate(@PathVariable("fromYear") int fromYear) {
        ComputedMarketResult result = stocksService.getConsolidatedMarketGrowthRate(fromYear);
        return new MarketResponse<>(result, PeriodRange.fromYear(fromYear));
    }
}


