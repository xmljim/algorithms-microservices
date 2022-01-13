package io.xmljim.algorithms.functions.common.test;

import io.xmljim.algorithms.functions.common.provider.FunctionFactory;
import io.xmljim.algorithms.model.provider.ModelProvider;

public class TestFunctionFactory implements FunctionFactory {
    @Override
    public String getFactoryName() {
        return "TEST";
    }

    @Override
    public ModelProvider getModelProvider() {
        return null;
    }


}
