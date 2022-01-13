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

class VersionPatterns {
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String PATCH = "patch";
    public static final String PRERELEASE = "prerelease";
    public static final String BUILDMETADATA = "buildmetadata";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";

    private static final String numericPattern = "0|[1-9]\\d";
    private static final String alphaNumericPattern = "[0-9a-zA-Z-]";
    private static final String alphaPattern = "[a-zA-Z-]";
    private static final String numericAlphaPattern = "\\d*" + alphaPattern + alphaNumericPattern + "*";
    private static final String dot = "\\.";
    private static final String majorPattern = "(?<" + MAJOR + ">" + numericPattern + "*)";
    private static final String minorPattern = "(" + dot + "(?<" + MINOR + ">" + numericPattern + "*))?";
    private static final String patchPattern = "(" + dot + "(?<" + PATCH + ">" + numericPattern + "*))?";
    private static final String preReleaseBase = "(?:" + numericPattern + "*|" + numericAlphaPattern + ")(?:" + dot + "(?:" + numericPattern + "*|" + numericAlphaPattern +"))*";
    private static final String preReleasePattern = "(?:-(?<" + PRERELEASE + ">" + preReleaseBase + "))?";
    private static final String buildMetadataBase = alphaNumericPattern + "+(?:" + dot + alphaNumericPattern + "+)*)";
    private static final String buildMetadataPattern = "(?:\\+(?<" + BUILDMETADATA + ">" + buildMetadataBase + ")?";
    private static final String operatorPattern = "(?<" + OPERATOR + ">[\\^~=><]{1,2})";
    public static final String suffixPattern = "(?:-?(?<" + PRERELEASE + ">" + preReleaseBase + "))?" + buildMetadataPattern;
    public static final String regex = "^" + majorPattern + minorPattern + patchPattern + preReleasePattern + buildMetadataPattern + "$";
    public static final String evalPattern = operatorPattern + "(?<" + VERSION + ">" + majorPattern + minorPattern + patchPattern + preReleasePattern + buildMetadataPattern + ")";




    private VersionPatterns() {
        //disable constructor
    }



}
