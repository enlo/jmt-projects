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

import info.naiv.lab.java.jmt.fx.Function0;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.EqualsAndHashCode;

/**
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode(of = "value")
public class Lazy<T> {

    final AtomicBoolean initialized = new AtomicBoolean(false);

    T value;

    public Lazy() {
    }

    public Lazy(T value) {
        this.value = value;
        this.initialized.set(true);
    }

    public static <T> Lazy<T> of(final Function0<? extends T> initializer) {
        return new Lazy<T>() {
            @Override
            public T initialValue() {
                return initializer.apply();
            }
        };
    }

    public final T get() {
        if (initialized.compareAndSet(false, true)) {
            value = initialValue();
        }
        return value;
    }

    public T initialValue() {
        return null;
    }

    @Override
    public final String toString() {
        return Objects.toString(value);
    }
}
