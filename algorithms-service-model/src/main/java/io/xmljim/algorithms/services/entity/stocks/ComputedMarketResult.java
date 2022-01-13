package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.DefaultServiceResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ComputedMarketResult extends DefaultServiceResult<Double> implements MarketResult<Double> {
    public ComputedMarketResult(final String description, final Double data) {
        super(description, data);
    }
}
