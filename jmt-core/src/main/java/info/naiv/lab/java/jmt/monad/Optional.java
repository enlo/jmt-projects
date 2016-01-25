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

import info.naiv.lab.java.jmt.iterator.SingleIterator;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Supplier;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import lombok.Value;

/**
 * Java7向け Optional monad.
 *
 * @author enlo
 * @param <T>
 */
@Value
public class Optional<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final Optional EMPTY = new Optional(null);

    T value;

    /**
     * 空の Optional
     *
     * @param <T> 型T
     * @return 空のOptional
     */
    @ReturnNonNull
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return {@link #isPresent()} &amp;&amp; predicate が true を戻せば自分自身. <br>
     * そうでなければ {@link #empty()}
     */
    @ReturnNonNull
    public Optional<T> filter(Predicate1<? super T> predicate) {
        return isPresent() && predicate.test(value) ? this : Optional.<T>empty();
    }

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    public <U> Optional<U> flatMap(Function1<? super T, Optional<U>> mapper) {
        if (isPresent()) {
            Optional<U> result = mapper.apply(value);
            if (result != null) {
                return result;
            }
        }
        return Optional.<U>empty();
    }

    /**
     *
     * @return 値が null でなければ true.
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     *
     * @return 値
     */
    public T get() {
        return value;
    }

    public Optional<T> bind(Consumer1<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
        return this;
    }

    public void ifPresent(Consumer1<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    @Override
    @ReturnNonNull
    public Iterator<T> iterator() {
        return new SingleIterator<>(isPresent(), value);
    }

    @ReturnNonNull
    public <U> Optional<U> map(Function1<? super T, ? extends U> mapper) {
        if (isPresent()) {
            return Optional.ofNullable(mapper.apply(value));
        }
        else {
            return Optional.<U>empty();
        }
    }

    @ReturnNonNull
    public static <T> Optional<T> of(T value) {
        nonNull(value, "value");
        return new Optional<>(value);
    }

    @ReturnNonNull
    public static <T> Optional<T> ofNullable(T value) {
        return new Optional<>(value);
    }

    @ReturnNonNull
    public T orElse(T other) {
        return isPresent() ? value : other;
    }

    @ReturnNonNull
    public T orElseGet(Supplier<? extends T> other) {
        return isPresent() ? value : other.get();
    }

    @ReturnNonNull
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    @ReturnNonNull
    public Set<T> toSet() {
        Set<T> result = new HashSet<>();
        if (isPresent()) {
            result.add(value);
        }
        return result;
    }

}
