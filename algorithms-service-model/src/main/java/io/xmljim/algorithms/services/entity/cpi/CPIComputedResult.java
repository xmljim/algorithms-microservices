package io.xmljim.algorithms.services.entity.cpi;

import io.xmljim.algorithms.services.entity.AbstractServiceResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.StringJoiner;

@Data
@NoArgsConstructor
public class CPIComputedResult extends AbstractServiceResult<Double> implements CPIResult<Double> {
    public CPIComputedResult(final String description, final Double data) {
        super(description, data);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CPIComputedResult{");
        sb.append("data = ").append(super.getData());
        sb.append(", description = ").append(super.getDescription());
        sb.append(", type = ").append(super.getType());
        sb.append('}');
        return sb.toString();
    }
}
