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

import info.naiv.lab.java.jmt.fx.Supplier;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <T>
 */
@ThreadSafe
public class Lazy<T> implements Supplier<T> {

    /**
     *
     * @param <T>
     * @param initializer
     * @return
     */
    @Nonnull
    public static <T> Lazy<T> of(@NonNull final Supplier<? extends T> initializer) {
        return new Lazy<T>() {
            @Override
            public T initialValue() {
                return initializer.get();
            }
        };
    }

    private final Initializer initializer = new Initializer() {
        @Override
        protected void doInitialize() {
            value = initialValue();
        }
    };

    private T value;

    /**
     *
     */
    public Lazy() {
    }

    /**
     *
     * @param value
     */
    public Lazy(T value) {
        this.value = value;
        this.initializer.setInitialized();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Lazy) {
            final Lazy<?> other = (Lazy<?>) obj;
            return Objects.equals(this.get(), other.get());
        }
        return false;
    }

    @Override
    public final T get() {
        initializer.run();
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(get());
        return hash;
    }

    public final boolean isInitialized() {
        return initializer.isInitialized();
    }

    @Override
    public final String toString() {
        return Objects.toString(get());
    }

    /**
     *
     * @return
     */
    protected T initialValue() {
        return null;
    }

    protected T rawValue() {
        return value;
    }
}
