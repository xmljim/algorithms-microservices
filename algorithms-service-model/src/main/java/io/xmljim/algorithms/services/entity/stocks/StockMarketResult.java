package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;

public class StockMarketResult extends AbstractServiceResult<StockMarket> implements MarketResult<StockMarket> {
    public StockMarketResult(final String description, final StockMarket data) {
        super(description, data);
    }
}
