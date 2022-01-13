package io.xmljim.algorithms.service.retirement.client;

import io.xmljim.algorithms.services.entity.cpi.CPIComputedResponse;
import io.xmljim.algorithms.services.entity.date.PeriodRange;
import io.xmljim.algorithms.services.entity.stocks.ComputedMarketResult;
import io.xmljim.algorithms.services.entity.stocks.MarketResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class StocksClient {

    @Autowired
    private RestTemplate stocksRestTemplate;

    @Value("${services.dependencies.stocks.name}")
    private String STOCK_SERVICE_NAME;

    @Value("${services.dependencies.stocks.path}")
    private String STOCK_SERVICE_PATH;

    @Value("${services.dependencies.stocks.endpoints.consolidatedMarketGrowth.path}")
    private String CONSOLIDATED_GROWTH_PATH;

    @SuppressWarnings("unchecked")
    @Cacheable("consolidated-market-growth")
    public MarketResponse<ComputedMarketResult> getConsolidatedMarketGrowthRate(int fromYear) {
        String url = "http://" + STOCK_SERVICE_NAME + STOCK_SERVICE_PATH + CONSOLIDATED_GROWTH_PATH;
        log.info("URL: {} [{}]", url, fromYear);
        return stocksRestTemplate.getForObject(url, ComputedMarketResponse.class, fromYear);
    }

    @Data
    private static class ComputedMarketResponse extends MarketResponse<ComputedMarketResult> {
        private List<ComputedMarketResult> results;
        private PeriodRange periodRange;

        public ComputedMarketResponse() {
        }

        public ComputedMarketResponse(final ComputedMarketResult serviceResult, final PeriodRange periodRange) {
            super(serviceResult, periodRange);
        }

        public ComputedMarketResponse(final List<ComputedMarketResult> serviceResults, final PeriodRange periodRange) {
            super(serviceResults, periodRange);
        }
    }
}
