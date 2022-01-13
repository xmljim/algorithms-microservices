package io.xmljim.algorithms.services.entity.statistics;

import io.xmljim.algorithms.model.Function;
import io.xmljim.algorithms.model.util.Scalar;

public class ScalarFunctionResult extends FunctionResult<Scalar> {
    public ScalarFunctionResult(final Function<Scalar> function) {
        super(function);
    }
}
