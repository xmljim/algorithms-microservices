package io.xmljim.algorithms.services.entity.cpi;

import io.xmljim.algorithms.services.entity.date.PeriodRange;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
public class CPIComputedResponse extends CPIResponse<CPIComputedResult> {
    private List<CPIComputedResult> results;

    public CPIComputedResponse(final CPIComputedResult serviceResult, final PeriodRange periodRange) {
        super(serviceResult, periodRange);
    }

    public CPIComputedResponse(final List<CPIComputedResult> serviceResults, final PeriodRange periodRange) {
        super(serviceResults, periodRange);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CPIComputedResponse{");
        sb.append("results=").append(results);
        sb.append(", count=").append(getCount());
        sb.append('}');
        return sb.toString();
    }
}
