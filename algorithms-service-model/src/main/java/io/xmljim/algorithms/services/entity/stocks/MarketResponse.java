package io.xmljim.algorithms.services.entity.stocks;

import io.xmljim.algorithms.services.entity.AbstractServiceResponse;
import io.xmljim.algorithms.services.entity.ServiceResult;
import io.xmljim.algorithms.services.entity.date.PeriodRange;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@ToString
public class MarketResponse<T extends MarketResult<?>> extends AbstractServiceResponse<T> {
    private PeriodRange periodRange;

    public MarketResponse(final T serviceResult, final PeriodRange periodRange) {
        super(serviceResult);
        this.periodRange = periodRange;
    }

    public MarketResponse(final List<T> serviceResults, final PeriodRange periodRange) {
        super(serviceResults);
        this.periodRange = periodRange;
    }
}
