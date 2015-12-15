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

import static info.naiv.lab.java.jmt.Arguments.nonNull;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class AbstractStringJoiner<T> extends Joiner<StringBuilder, T> {

    /**
     *
     */
    protected final Adder<StringBuilder, T> first;

    /**
     *
     */
    protected final Adder<StringBuilder, T> more;

    /**
     *
     * @param first
     * @param more
     */
    public AbstractStringJoiner(Adder<StringBuilder, T> first, Adder<StringBuilder, T> more) {
        this.first = nonNull(first, "first");
        this.more = nonNull(more, "more");
    }

    /**
     *
     * @param adder
     */
    public AbstractStringJoiner(Adder<StringBuilder, T> adder) {
        this.first = nonNull(adder, "adder");
        this.more = adder;
    }
    
    @Override
    protected final StringBuilder createResult() {
        return new StringBuilder();
    }

    @Override
    protected final Adder<StringBuilder, T> getFirst() {
        return first;
    }

    @Override
    protected final Adder<StringBuilder, T> getMore() {
        return more;
    }

}
