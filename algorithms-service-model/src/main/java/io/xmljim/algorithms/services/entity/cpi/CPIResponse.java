package io.xmljim.algorithms.services.entity.cpi;

import io.xmljim.algorithms.services.entity.DefaultServiceResponse;
import io.xmljim.algorithms.services.entity.date.PeriodRange;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CPIResponse<T extends CPIResult<?>> extends DefaultServiceResponse<T> {
    private PeriodRange periodRange;

    public CPIResponse() {
    }

    public CPIResponse(final T serviceResult, PeriodRange periodRange) {
        super(serviceResult);
        this.periodRange = periodRange;
    }

    public CPIResponse(final List<T> serviceResults, PeriodRange periodRange) {
        super(serviceResults);
        this.periodRange = periodRange;
    }
}
