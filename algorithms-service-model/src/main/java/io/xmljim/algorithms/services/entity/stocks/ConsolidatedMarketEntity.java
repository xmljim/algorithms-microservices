package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidatedMarketEntity implements MarketRecord {

    private int year;
    private double open;
    private double close;
    private double netChange;
    private double pctChange;
    private double aggregateChange;
    private double adjustedAggregateChange;
    private double logAggregateChange;
    private double logAdjustedAggregateChange;
}
