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
package info.naiv.lab.java.jmt.fx;

import info.naiv.lab.java.jmt.Holder;
import info.naiv.lab.java.jmt.Misc;
import static java.lang.Integer.parseInt;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class StandardFunctions {

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

    public static Predicate1<Annotation> annotationOf(final Class<? extends Annotation> clazz) {
        return new Predicate1<Annotation>() {
            @Override
            public boolean test(Annotation obj) {
                return obj != null && clazz.isAssignableFrom(obj.getClass());
            }
        };
    }

    /**
     *
     * @param pattern
     * @return
     */
    @Nonnull
    public static StringPredicate byRegex(final String pattern) {
        final Pattern p = Pattern.compile(pattern);
        return new StringPredicate() {
            @Override
            public boolean test(String obj) {
                return p.matcher(obj).find();
            }
        };
    }

    /**
     *
     * @param <T>
     * @param value
     * @return
     */
    @Nonnull
    public static <T extends Comparable<T>> Predicate1<T> compareEqual(final T value) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return Misc.compareEqual(value, obj);
            }
        };
    }

    @Nonnull
    public static <T> Supplier<T> constantSuplier(final T value) {
        return new Supplier<T>() {
            @Override
            public T get() {
                return value;
            }
        };
    }

    /**
     *
     * @param defaultValue
     * @return
     */
    @Nonnull
    public static Function1<String, Integer> convertStringToInt(final int defaultValue) {
        return new Function1<String, Integer>() {

            @Override
            public Integer apply(String a1) {
                if (a1 == null || a1.isEmpty()) {
                    return defaultValue;
                }
                try {
                    return parseInt(a1);
                }
                catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        };
    }

    @Nonnull
    public static Supplier<Integer> counter(final int initialValue) {
        return new Supplier<Integer>() {
            private int count = initialValue;

            @Override
            public Integer get() {
                return count++;
            }
        };
    }

    /**
     *
     * @param suffix
     * @return
     */
    @Nonnull
    public static StringPredicate endsWith(final String suffix) {
        return new StringPredicate() {
            @Override
            public boolean test(String obj) {
                return obj.endsWith(suffix);
            }
        };
    }

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
     * @param clazz
     * @return
     */
    @Nonnull
    public static <T> Function1<Holder<T>, T> getContent(Class<T> clazz) {
        return new Function1<Holder<T>, T>() {

            @Override
            public T apply(Holder<T> a1) {
                return a1.getContent();
            }
        };
    }

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
     * @param clazz
     * @return
     */
    @Nonnull
    public static <T> Function1<Class<? extends T>, T> newInstance(Class<? extends T> clazz) {
        return new Function1<Class<? extends T>, T>() {
            @Override
            public T apply(Class<? extends T> a1) {
                try {
                    return a1.newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex) {
                    throw new UnsupportedOperationException(ex);
                }
            }
        };
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param initialValue
     * @return
     */
    @Nonnull
    public static <T> Function1<Class<? extends T>, T> newInstance(Class<? extends T> clazz, final T initialValue) {
        return new Function1<Class<? extends T>, T>() {
            @Override
            public T apply(Class<? extends T> a1) {
                return initialValue;
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

    private StandardFunctions() {
    }

}
