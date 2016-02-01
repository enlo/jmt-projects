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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <T>
 */
public final class Iteratee<T> implements Iterable<T>, Serializable {

    private static final Iteratee EMPTY = new Iteratee();

    private static final long serialVersionUID = 1L;

    /**
     * 空の Optional
     *
     * @param <T> 型T
     * @return 空のOptional
     */
    @ReturnNonNull
    public static <T> Iteratee<T> empty() {
        return EMPTY;
    }

    @ReturnNonNull
    public static <T> Iteratee<T> of(Iterable<T> value) {
        return new Iteratee<>(value);
    }

    @ReturnNonNull
    public static <T> Iteratee<T> of(T... value) {
        return new Iteratee<>(value);
    }

    @NonNull
    Predicate1<? super T> predicate;

    @NonNull
    Iterable<T> value;

    public Iteratee(Iterable<T> value, Predicate1<? super T> predicate) {
        this.predicate = predicate;
        this.value = value;
    }

    public Iteratee(Iterable<T> value) {
        this(value, StandardFunctions.NO_CHECK);
    }

    public Iteratee(T[] value) {
        this(Arrays.asList(value), StandardFunctions.NO_CHECK);
    }

    Iteratee() {
        this(Collections.EMPTY_LIST, StandardFunctions.NO_CHECK);
    }

    public Iteratee<T> bind(Consumer1<? super T> consumer) {
        for (T v : this) {
            consumer.accept(v);
        }
        return this;
    }

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return predicate が true を戻したオブジェクトのみを戻す.
     */
    @ReturnNonNull
    public Iteratee<T> filter(Predicate1<? super T> predicate) {
        return new Iteratee<>(this, predicate);
    }

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    public <U> Iteratee<U> flatMap(Function1<? super T, Iteratee<U>> mapper) {
        Iterable<U> it = Misc.flat(Misc.map(this, mapper));
        return new Iteratee(it);
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(value.iterator(), predicate);
    }

    @ReturnNonNull
    public <U> Iteratee<U> map(Function1<? super T, ? extends U> mapper) {
        Iterable<U> it = Misc.map(this, mapper);
        return new Iteratee<>(it);
    }

    @ReturnNonNull
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (T v : this) {
            result.add(v);
        }
        return result;
    }

    @ReturnNonNull
    public <K> Map<K, T> toMap(Function1<T, K> keyResolver) {
        nonNull(keyResolver, "keyResolver");
        final Map<K, T> result = new HashMap<>();
        for (T v : this) {
            result.put(keyResolver.apply(v), v);
        }
        return result;
    }
}
