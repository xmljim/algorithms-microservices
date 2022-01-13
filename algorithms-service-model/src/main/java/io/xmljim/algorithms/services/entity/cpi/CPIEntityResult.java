package io.xmljim.algorithms.services.entity.cpi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CPIEntityResult extends CPIComputedResult {
    private int year;

    public CPIEntityResult(final String description, final Double data, int year) {
        super(description, data);
        this.year = year;
    }
}

