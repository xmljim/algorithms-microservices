package io.xmljim.algorithms.functions.common;

import io.xmljim.algorithms.functions.common.test.TestFunction;
import io.xmljim.algorithms.functions.common.test.TestFunctionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFunctionTest {

    @Test
    void testFunctionWithProvider() {
        TestFunction function = new TestFunction(new TestFunctionType());

        String value = function.compute();
        assertEquals("TEST", value);
    }
}