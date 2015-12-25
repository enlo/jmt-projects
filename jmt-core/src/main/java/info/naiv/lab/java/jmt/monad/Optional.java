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
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Function0;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import java.io.Serializable;
import lombok.Value;

/**
 * Java7向け Optional monad.
 *
 * @author enlo
 * @param <T>
 */
@Value
public class Optional<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Optional<?> EMPTY = new Optional<>(null);

    T value;

    /**
     *
     * @param <T> 型T
     * @return 空のOptional
     */
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public Optional<T> filter(Predicate1<? super T> predicate) {
        return isPresent() && predicate.test(value) ? this : Optional.<T>empty();
    }

    public <U> Optional<U> flatMap(Function1<? super T, Optional<U>> mapper) {
        return isPresent() ? mapper.apply(value) : Optional.<U>empty();
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }

    public void ifPresent(Consumer1<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    public <U> Optional<U> map(Function1<? super T, ? extends U> mapper) {
        if (isPresent()) {
            return Optional.ofNullable(mapper.apply(value));
        }
        else {
            return Optional.<U>empty();
        }
    }

    public static <T> Optional<T> of(T value) {
        nonNull(value, "value");
        return new Optional<>(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return new Optional<>(value);
    }

    public T orElse(T other) {
        return isPresent() ? value : other;
    }

    public T orElseGet(Function0<? extends T> other) {
        return isPresent() ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Function0<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw exceptionSupplier.get();
    }
}
