package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MarketEntity implements MarketRecord {
    private String market;
    private int year;
    private double open;
    private double close;
}
