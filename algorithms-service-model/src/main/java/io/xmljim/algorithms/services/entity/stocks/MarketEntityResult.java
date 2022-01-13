package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;

public class MarketEntityResult extends AbstractServiceResult<MarketEntity> implements MarketResult<MarketEntity> {
    public MarketEntityResult(final String description, final MarketEntity data) {
        super(description, data);
    }
}
