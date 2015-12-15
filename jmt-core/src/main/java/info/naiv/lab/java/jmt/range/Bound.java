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
package info.naiv.lab.java.jmt.range;

import java.io.Serializable;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <T>
 */
@AllArgsConstructor
@ToString
public abstract class Bound<T extends Comparable<T>> implements Cloneable, Serializable {

    public boolean isOpen() {
        return BoundType.OPEN.equals(type);
    }

    public boolean isClosed() {
        return BoundType.CLOSED.equals(type);
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    @ToString(callSuper = true)
    public static class ClosedLowerBound<T extends Comparable<T>> extends Bound<T> {

        public ClosedLowerBound(T value) {
            super(value, BoundType.CLOSED);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new ClosedLowerBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<T> c) {
            return c.compare(this.value, value) < 0;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    @ToString(callSuper = true)
    public static class OpenLowerBound<T extends Comparable<T>> extends Bound<T> {

        public OpenLowerBound(T value) {
            super(value, BoundType.OPEN);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new OpenLowerBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<T> c) {
            return c.compare(this.value, value) <= 0;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    @ToString(callSuper = true)
    public static class ClosedUpperBound<T extends Comparable<T>> extends Bound<T> {

        public ClosedUpperBound(T value) {
            super(value, BoundType.CLOSED);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new ClosedUpperBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<T> c) {
            return c.compare(value, this.value) < 0;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    @ToString(callSuper = true)
    public static class OpenUpperBound<T extends Comparable<T>> extends Bound<T> {

        public OpenUpperBound(T value) {
            super(value, BoundType.OPEN);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new OpenUpperBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<T> c) {
            return c.compare(value, this.value) <= 0;
        }

    }

    @Getter
    protected final T value;

    @Getter
    protected final BoundType type;

    public abstract boolean on(T value, Comparator<T> c);

    public abstract Bound<T> construct(T newValue);

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public Bound<T> clone() {
        try {
            return (Bound<T>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

}
