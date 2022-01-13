package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;

public class ConsolidatedMarketEntityResult extends AbstractServiceResult<ConsolidatedMarketEntity> implements MarketResult<ConsolidatedMarketEntity> {
    public ConsolidatedMarketEntityResult(final String description, final ConsolidatedMarketEntity data) {
        super(description, data);
    }
}
