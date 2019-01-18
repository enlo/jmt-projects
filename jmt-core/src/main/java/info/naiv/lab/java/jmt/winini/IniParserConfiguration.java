/*
 * The MIT License
 *
 * Copyright 2018 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.winini;

import static info.naiv.lab.java.jmt.Strings.concatnate;
import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Value;

/**
 *
 * @author enlo
 */
@Builder
@Value
public class IniParserConfiguration implements Serializable {

    private static final long serialVersionUID = -4663454663499523139L;
    @Builder.Default
    boolean allowDuplicateKeys = true;
    @Builder.Default
    boolean allowDuplicateSections = false;
    @Builder.Default
    boolean allowKeysWithoutSection = true;
    @Builder.Default
    boolean caseSensitive = false;
    @Builder.Default
    String commentPrefix = ";";
    @Builder.Default
    boolean concatenateDuplicateKeys = false;
    @Builder.Default
    char keyValueAssigmentChar = '=';
    @Builder.Default
    char sectionEnd = ']';
    @Builder.Default
    char sectionStart = '[';
    @Builder.Default
    boolean skipInvalidLines = true;

    public Pattern createCommentPrefixPattern() {
        return Pattern.compile(concatnate("^", commentPrefix, "(.*)$"));
    }
}
