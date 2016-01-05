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

import info.naiv.lab.java.jmt.range.Bound.NoBound;
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
@EqualsAndHashCode
public abstract class Bound<T extends Comparable<T>> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    protected final BoundType type;

    @Getter
    protected final T value;

    public Bound(T value, BoundType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public Bound<T> clone() {
        try {
            return (Bound<T>) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    public abstract Bound<T> construct(T newValue);

    public boolean isClosed() {
        return BoundType.CLOSED.equals(type);
    }

    public boolean isOpen() {
        return BoundType.OPEN.equals(type);
    }

    public abstract boolean on(T value, Comparator<? super T> c);

    @EqualsAndHashCode(callSuper = true)
    @Value
    @ToString(callSuper = true)
    public static class ClosedLowerBound<T extends Comparable<T>> extends Bound<T> {

        private static final long serialVersionUID = 1L;

        public ClosedLowerBound(T value) {
            super(value, BoundType.CLOSED);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new ClosedLowerBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<? super T> c) {
            return c.compare(this.value, value) <= 0;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @ToString(callSuper = true)
    public static class ClosedUpperBound<T extends Comparable<T>> extends Bound<T> {

        private static final long serialVersionUID = 1L;

        public ClosedUpperBound(T value) {
            super(value, BoundType.CLOSED);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new ClosedUpperBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<? super T> c) {
            return c.compare(value, this.value) <= 0;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @ToString(callSuper = true)
    public static class NoBound<T extends Comparable<T>> extends Bound<T> {

        private static final long serialVersionUID = 1L;

        public NoBound() {
            super(null, BoundType.CLOSED);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new NoBound<>();
        }

        @Override
        public boolean on(T value, Comparator<? super T> c) {
            return true;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @ToString(callSuper = true)
    public static class OpenLowerBound<T extends Comparable<T>> extends Bound<T> {

        private static final long serialVersionUID = 1L;

        public OpenLowerBound(T value) {
            super(value, BoundType.OPEN);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new OpenLowerBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<? super T> c) {
            return c.compare(this.value, value) < 0;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @ToString(callSuper = true)
    public static class OpenUpperBound<T extends Comparable<T>> extends Bound<T> {

        private static final long serialVersionUID = 1L;

        public OpenUpperBound(T value) {
            super(value, BoundType.OPEN);
        }

        @Override
        public Bound<T> construct(T newValue) {
            return new OpenUpperBound<>(newValue);
        }

        @Override
        public boolean on(T value, Comparator<? super T> c) {
            return c.compare(value, this.value) < 0;
        }

    }

}
