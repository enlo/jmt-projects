/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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

import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <T>
 * @param <R>
 */
public abstract class AbstractJoiner<T, R> extends Joiner<T, R> {

    /**
     * 最初に1回だけ実行される Adder.
     */
    protected final Adder<T, R> first;

    /**
     * 2回目以降に実行される Adder.
     */
    protected final Adder<T, R> more;

    /**
     * 2つの {@link Adder} で初期化する.
     *
     * @param first １回目の Adder.
     * @param more 2回目以降の Adder.
     */
    public AbstractJoiner(@NonNull Adder<T, R> first, @NonNull Adder<T, R> more) {
        this.first = first;
        this.more = more;
    }

    /**
     * 1つの {@link Adder } で初期化する. first, more ともに同じ Adder を使用する.
     *
     * @param adder Adder.
     */
    public AbstractJoiner(@NonNull Adder<T, R> adder) {
        this.first = adder;
        this.more = adder;
    }

    @Override
    protected Adder<T, R> getFirst() {
        return first;
    }

    @Override
    protected Adder<T, R> getMore() {
        return more;
    }

}
