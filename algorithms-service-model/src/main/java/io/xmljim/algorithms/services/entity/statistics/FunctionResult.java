package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.model.Function;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class FunctionResult<T> extends ParameterizedServiceResult {
    private T value;
    
    public FunctionResult(final Function<T> function) {
        super(function.getName(), function);
        this.value = function.compute();
    }
}
