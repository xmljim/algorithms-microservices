package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.services.entity.DefaultServiceResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ModelResponse extends DefaultServiceResponse<ModelResult> {
    public ModelResponse(final ModelResult serviceResult) {
        super(serviceResult);
    }

    public ModelResponse(final List<ModelResult> serviceResults) {
        super(serviceResults);
    }
}
