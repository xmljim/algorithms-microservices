package io.xmljim.algorithms.services.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class DefaultServiceResult<T> extends AbstractServiceResult<T> {
    public DefaultServiceResult(final String description, final T data) {
        super(description, data);
    }
}
