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
package info.naiv.lab.java.jmt.monad;

import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.iterator.FilteringIterator;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <T>
 */
@AllArgsConstructor
public final class IterableMonad<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final IterableMonad EMPTY = new IterableMonad();

    @NonNull
    Iterable<T> value;

    @NonNull
    Predicate1<? super T> predicate;

    IterableMonad() {
        this(Collections.EMPTY_LIST, StandardFunctions.NO_CHECK);
    }

    public IterableMonad(Iterable<T> value) {
        this(value, StandardFunctions.NO_CHECK);
    }

    public IterableMonad(T[] value) {
        this(Arrays.asList(value), StandardFunctions.NO_CHECK);
    }

    /**
     * 空の Optional
     *
     * @param <T> 型T
     * @return 空のOptional
     */
    @ReturnNonNull
    public static <T> IterableMonad<T> empty() {
        return EMPTY;
    }

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return {@link #isPresent()} && predicate が true を戻せば自分自身. <br>
     * そうでなければ {@link #empty()}
     */
    @ReturnNonNull
    public IterableMonad<T> filter(Predicate1<? super T> predicate) {
        return new IterableMonad<>(this, predicate);
    }

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    public <U> IterableMonad<U> flatMap(Function1<? super T, IterableMonad<U>> mapper) {
        Iterable<U> it = Misc.flat(Misc.map(this, mapper));
        return new IterableMonad(it);
    }

    public IterableMonad<T> bind(Consumer1<? super T> consumer) {
        for (T v : this) {
            consumer.accept(v);
        }
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(value.iterator(), predicate);
    }

    @ReturnNonNull
    public <U> IterableMonad<U> map(Function1<? super T, ? extends U> mapper) {
        Iterable<U> it = Misc.map(this, mapper);
        return new IterableMonad<>(it);
    }

    @ReturnNonNull
    public static <T> IterableMonad<T> of(Iterable<T> value) {
        return new IterableMonad<>(value);
    }

    @ReturnNonNull
    public static <T> IterableMonad<T> of(T... value) {
        return new IterableMonad<>(value);
    }

    @ReturnNonNull
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (T v : this) {
            result.add(v);
        }
        return result;
    }

}
