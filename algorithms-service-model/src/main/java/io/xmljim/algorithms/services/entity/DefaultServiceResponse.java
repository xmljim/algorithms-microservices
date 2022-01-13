package io.xmljim.algorithms.services.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class DefaultServiceResponse<T extends ServiceResult<?>> extends AbstractServiceResponse<T> {

    public DefaultServiceResponse(final T serviceResult) {
        super(serviceResult);
    }

    public DefaultServiceResponse(final List<T> serviceResults) {
        super(serviceResults);
    }
}
