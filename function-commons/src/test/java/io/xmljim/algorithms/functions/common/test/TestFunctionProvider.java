package io.xmljim.algorithms.functions.common.test;

import io.xmljim.algorithms.functions.common.provider.FunctionProvider;
import io.xmljim.algorithms.model.util.Version;

public class TestFunctionProvider implements FunctionProvider<TestFunctionFactory> {

    @Override
    public TestFunctionFactory getFactory() {
        return new TestFunctionFactory();
    }

    @Override
    public Version getProviderVersion() {
        return new Version("1.0.0");
    }

}
