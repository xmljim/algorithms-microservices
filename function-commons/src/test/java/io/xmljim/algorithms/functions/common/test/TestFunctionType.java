package io.xmljim.algorithms.functions.common.test;

import io.xmljim.algorithms.functions.common.FunctionType;

public class TestFunctionType implements FunctionType {
    @Override
    public String getName() {
        return "TestFunction";
    }

    @Override
    public String getLabel() {
        return null;
    }
}
