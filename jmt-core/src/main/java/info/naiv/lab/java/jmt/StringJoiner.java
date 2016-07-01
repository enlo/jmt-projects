/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 文字列連結.
 *
 * @author enlo
 *
 */
@ThreadSafe
public class StringJoiner extends AbstractStringBuilderJoiner<Object> {

    /**
     *
     */
    public static final StringJoiner COMMADELIMITED = new StringJoiner(",");

    /**
     *
     */
    public static final StringJoiner DOTTED = new StringJoiner(".");

    /**
     *
     */
    public static final StringJoiner HYPHENATED = new StringJoiner("-");

    /**
     *
     */
    public static final StringJoiner SIMPLE = new StringJoiner();

    /**
     *
     */
    public static final StringJoiner SLASHED = new StringJoiner("/");

    /**
     *
     * @param delim
     * @return
     */
    @Nonnull
    public static StringJoiner valueOf(String delim) {
        if (delim == null) {
            return SIMPLE;
        }
        switch (delim) {
            case ",":
                return COMMADELIMITED;
            case ".":
                return DOTTED;
            case "-":
                return HYPHENATED;
            case "/":
                return SLASHED;
            default:
                return new StringJoiner(delim);
        }
    }

    /**
     *
     * @param delim
     */
    public StringJoiner(final String delim) {
        super(new SimpleAdder(), new Adder<Object, StringBuilder>() {
            @Override
            public StringBuilder add(StringBuilder obj, Object value, int idx) {
                return obj.append(delim).append(value);
            }
        });
    }

    /**
     *
     */
    public StringJoiner() {
        super(new SimpleAdder());
    }

    private static class SimpleAdder implements Adder<Object, StringBuilder> {

        @Override
        public StringBuilder add(StringBuilder obj, Object value, int idx) {
            return obj.append(value);
        }
    }
}
