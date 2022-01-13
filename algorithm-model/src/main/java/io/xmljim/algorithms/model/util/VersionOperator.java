/*
 * Copyright 2021-2022 Jim Earley (xml.jim@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.xmljim.algorithms.model.util;

import java.util.Arrays;

public enum VersionOperator {
    /**
     * Equivalent to "^", e.g., "^1.0.0". <b>Cannot</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    COMPATIBLE("^", false),
    /**
     * Equivalent to "~", e.g., "~1.0.0". <b>Cannot</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    CLOSE_TO("~", false),
    /**
     * Equivalent to "=", e.g., "=1.0.0". <b>Cannot</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    EQUAL_TO("=", false),
    /**
     * Equivalent to "&gt;", e.g., "&gt;1.0.0". <b>Can</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    GREATER_THAN(">", true),
    /**
     * Equivalent to "&lt;", e.g., "&lt;1.0.0". <b>Can</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    LESS_THAN("<", true),
    /**
     * Equivalent to "&gt;=", e.g., "&gt;=;1.0.0". <b>Can</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    GREATER_THAN_EQUAL_TO(">=", true),
    /**
     * Equivalent to "&lt;=", e.g., "&lt;=1.0.0". <b>Can</b> be used with {@link Version#evaluateVersion(VersionExpression)}
     */
    LESS_THAN_EQUAL_TO("<=", true)
    ;

    private String symbol;
    private boolean compoundRangeEnabled;

    /**
     * Return the symbol for the given enum value
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns whether the enum value is enabled for compound ranges
     * @return true if enabled, false otherwise
     */
    public boolean isCompoundRangeEnabled() {
        return compoundRangeEnabled;
    }


    private VersionOperator(String symbol, boolean compoundRangeEnabled) {
        this.symbol = symbol;
        this.compoundRangeEnabled = compoundRangeEnabled;
    }

    /**
     * Return the enum value for a given symbol
     * @param symbol the version expression symbol associated with a given enum
     * @return the enum value, or null if no matching enum value exists
     */
    public static VersionOperator fromSymbol(String symbol) {

        return Arrays.asList(values())
                .stream()
                .filter(value -> value.getSymbol().contentEquals(symbol))
                .findFirst()
                .orElse(null);
    }
}