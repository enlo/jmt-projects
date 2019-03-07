/*
 * The MIT License
 *
 * Copyright 2019 enlo.
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
package info.naiv.lab.java.jmt.fx;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class Predicates {

    /**
     *
     */
    public static final Predicate1 NON_NULL = new Predicate1<Object>() {
        @Override
        public boolean test(Object obj) {
            return obj != null;
        }
    };

    /**
     *
     */
    public static Predicate1 NO_CHECK = new Predicate1() {
        @Override
        public boolean test(Object obj) {
            return true;
        }
    };

    /**
     *
     * @param <T>
     * @param value
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> equal(final T value) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return Objects.equals(value, obj);
            }
        };
    }

    /**
     *
     * @param <T>
     * @param p1
     * @param p2
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> and(final Predicate1<T> p1, final Predicate1<T> p2) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return p1.test(obj) && p2.test(obj);
            }
        };
    }

    /**
     *
     * @param <T>
     * @param p1
     * @param p2
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> or(final Predicate1<T> p1, final Predicate1<T> p2) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return p1.test(obj) || p2.test(obj);
            }
        };
    }

    /**
     *
     * @param <T>
     * @param p1
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> not(final Predicate1<T> p1) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return p1.test(obj) == false;
            }
        };
    }

    @Nonnull
    public static <T> Predicate1<? super T> isAssignableTo(final Class<? extends T> clazz) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return obj != null && clazz.isAssignableFrom(obj.getClass());
            }
        };
    }

    /**
     *
     * @param <T>
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> noCheck() {
        return NO_CHECK;
    }

    /**
     *
     * @param <T>
     * @return
     */
    @Nonnull
    public static <T> Predicate1<T> nonNull() {
        return NON_NULL;
    }

}
