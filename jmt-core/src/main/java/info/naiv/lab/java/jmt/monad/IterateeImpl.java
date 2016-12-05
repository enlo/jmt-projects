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
import info.naiv.lab.java.jmt.iteration.BreakException;
import info.naiv.lab.java.jmt.iteration.ContinueException;
import info.naiv.lab.java.jmt.iteration.LoopCondition;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Consumer2;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.iteration.FilteringIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <T>
 */
@Value
public final class IterateeImpl<T> implements Iteratee<T> {

    private static final IterateeImpl EMPTY = new IterateeImpl();

    private static final long serialVersionUID = 1L;

    /**
     * 空の Optional
     *
     * @param <T> 型T
     * @return 空のOptional
     */
    @Nonnull
    public static <T> IterateeImpl<T> empty() {
        return EMPTY;
    }

    /**
     *
     * @param <T>
     * @param value
     * @return
     */
    @Nonnull
    public static <T> IterateeImpl<T> of(Iterable<T> value) {
        return new IterateeImpl<>(value);
    }

    /**
     *
     * @param <T>
     * @param value
     * @return
     */
    @Nonnull
    public static <T> IterateeImpl<T> of(T... value) {
        return new IterateeImpl<>(value);
    }

    Predicate1<? super T> predicate;
    Iterable<T> value;

    /**
     *
     * @param value
     * @param predicate
     */
    public IterateeImpl(Iterable<T> value, Predicate1<? super T> predicate) {
        this.predicate = predicate != null ? predicate : StandardFunctions.NO_CHECK;
        this.value = value != null ? value : Collections.EMPTY_LIST;
    }

    /**
     *
     * @param value
     */
    public IterateeImpl(Iterable<T> value) {
        this(value, null);
    }

    /**
     *
     * @param value
     */
    public IterateeImpl(T[] value) {
        this(Arrays.asList(value), StandardFunctions.NO_CHECK);
    }

    IterateeImpl() {
        this(null, null);
    }

    /**
     *
     * @param consumer
     * @return
     */
    @Override
    public IterateeImpl<T> bind(@NonNull Consumer1<? super T> consumer) {
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
    @Nonnull
    @Override
    public IterateeImpl<T> filter(Predicate1<? super T> predicate) {
        return new IterateeImpl<>(this, predicate);
    }

    /**
     * 最初の値を戻す. 値がなければエラー.
     *
     * @return 最初の値.
     */
    @Override
    public T first() {
        return value.iterator().next();
    }

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @Nonnull
    @Override
    public <U> IterateeImpl<U> flatMap(Function1<? super T, IterateeImpl<U>> mapper) {
        Iterable<U> it = Misc.flat(Misc.map(this, mapper));
        return new IterateeImpl(it);
    }

    /**
     *
     * @return 値が空でなければ true.
     */
    @Override
    public boolean isPresent() {
        return value != null && value.iterator().hasNext();
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(value.iterator(), predicate);
    }

    /**
     *
     * @param <U>
     * @param mapper
     * @return
     */
    @Nonnull
    @Override
    public <U> IterateeImpl<U> map(Function1<? super T, ? extends U> mapper) {
        Iterable<U> it = Misc.map(this, mapper);
        return new IterateeImpl<>(it);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (T v : this) {
            result.add(v);
        }
        return result;
    }

    /**
     *
     * @param <K>
     * @param keyResolver
     * @return
     */
    @Nonnull
    @Override
    public <K> Map<K, T> toMap(@NonNull Function1<T, K> keyResolver) {
        final Map<K, T> result = new HashMap<>();
        for (T v : this) {
            result.put(keyResolver.apply(v), v);
        }
        return result;
    }

    /**
     * ForEach.  <br>
     * 処理の途中で{@link BreakException}を投げると break と同じ効果があり、<br>
     * 処理の途中で{@link ContinueException}を投げると break と同じ効果.
     *
     * @param action
     */
    public void each(Consumer1<? super T> action) {
        Misc.forEach(value, action);
    }

    /**
     * ForEach.
     *
     * @param action
     */
    public void each(Consumer2<? super T, LoopCondition> action) {
        Misc.forEach(value, action);
    }

}