package io.xmljim.algorithms.functions.common.test;

import io.xmljim.algorithms.functions.common.AbstractFunction;
import io.xmljim.algorithms.functions.common.FunctionType;
import io.xmljim.algorithms.model.Parameter;

import java.util.List;

public class TestFunction extends AbstractFunction<String> {
    public TestFunction(final FunctionType functionType, final List<Parameter<?>> parameterList) {
        super(functionType, parameterList);
    }

    public TestFunction(final FunctionType functionType, final Parameter<?>... parameters) {
        super(functionType, parameters);
    }

    public TestFunction(final FunctionType functionType, final String variable, final List<Parameter<?>> parameterList) {
        super(functionType, variable, parameterList);
    }

    public TestFunction(final FunctionType functionType, final String variable, final Parameter<?>... parameters) {
        super(functionType, variable, parameters);
    }

    @Override
    public String compute() {
        TestFunctionProvider provider = getFunctionProvider("TEST");

        return provider.getProviderName();
    }
}
