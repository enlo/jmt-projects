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
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import lombok.Setter;

/**
 * オブジェクト連結.
 *
 * @author enlo
 * @param <T>
 * @param <R>
 */
public abstract class Joiner<T, R> {

    @Setter
    private Predicate1<T> accepter = StandardFunctions.NON_NULL;

    /**
     * 連結処理.
     *
     * @param items
     * @return
     */
    public R join(Iterable<? extends T> items) {
        nonNull(items, "items");
        R r = createResult();
        Adder<T, R> adder = getFirst();
        final Adder<T, R> more = getMore();
        int i = 0;
        r = preLoop(r);
        for (T item : items) {
            if (accepter.test(item)) {
                r = adder.add(r, item, i);
                adder = more;
                i++;
            }
        }
        r = postLoop(r, i);
        return r;
    }

    /**
     * 連結処理.
     *
     * @param items
     * @return
     */
    public R join(T[] items) {
        nonNull(items, "items");
        R r = createResult();
        Adder<T, R> adder = getFirst();
        final Adder<T, R> more = getMore();
        final int size = items.length;
        r = preLoop(r);
        for (int i = 0; i < size; i++) {
            if (accepter.test(items[i])) {
                r = adder.add(r, items[i], i);
                adder = more;
            }
        }
        r = postLoop(r, size);
        return r;
    }

    /**
     * 連結処理.
     *
     * @param items アイテム.
     * @return
     */
    public R joinItems(T... items) {
        return join(items);
    }

    /**
     * @return 連結結果
     */
    protected abstract R createResult();

    /**
     *
     * @return 最初の連結器
     */
    @ReturnNonNull
    protected abstract Adder<T, R> getFirst();

    /**
     *
     * @return 2回以降の連結器
     */
    @ReturnNonNull
    protected abstract Adder<T, R> getMore();

    /**
     * ループ後処理.
     *
     * @param r
     * @param count
     * @return
     */
    protected R postLoop(R r, int count) {
        return r;
    }

    /**
     * ループ前処理.
     *
     * @param r
     * @return
     */
    protected R preLoop(R r) {
        return r;
    }

    /**
     * 連結器.
     *
     * @param <R>
     * @param <T>
     */
    public static interface Adder<T, R> {

        /**
         *
         * @param obj
         * @param value
         * @param idx
         * @return
         */
        R add(R obj, T value, int idx);
    }

}
