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
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import static java.lang.Integer.parseInt;
import java.util.Objects;
import java.util.regex.Pattern;

public class StandardFunctions {

    public static final Predicate1 NON_NULL = new Predicate1<Object>() {
        @Override
        public boolean test(Object obj) {
            return obj != null;
        }
    };

    public static Predicate1 NO_CHECK = new Predicate1() {
        @Override
        public boolean test(Object obj) {
            return true;
        }
    };

    @ReturnNonNull
    public static StringPredicate byRegex(final String pattern) {
        final Pattern p = Pattern.compile(pattern);
        return new StringPredicate() {
            @Override
            public boolean test(String obj) {
                return p.matcher(obj).find();
            }
        };
    }

    @ReturnNonNull
    public static <T extends Comparable<T>> Predicate1<T> compareEqual(final T value) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return Misc.compareEqual(value, obj);
            }
        };
    }

    @ReturnNonNull
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

    @ReturnNonNull
    public static StringPredicate endsWith(final String suffix) {
        return new StringPredicate() {
            @Override
            public boolean test(String obj) {
                return obj.endsWith(suffix);
            }
        };
    }

    @ReturnNonNull
    public static <T> Predicate1<T> equal(final T value) {
        return new Predicate1<T>() {
            @Override
            public boolean test(T obj) {
                return Objects.equals(value, obj);
            }
        };
    }

    @ReturnNonNull
    public static <T> Function1<Holder<T>, T> getContent(Class<T> clazz) {
        return new Function1<Holder<T>, T>() {

            @Override
            public T apply(Holder<T> a1) {
                return a1.getContent();
            }
        };
    }

    @ReturnNonNull
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

    @ReturnNonNull
    public static <T> Function1<Class<? extends T>, T> newInstance(Class<? extends T> clazz, final T initialValue) {
        return new Function1<Class<? extends T>, T>() {
            @Override
            public T apply(Class<? extends T> a1) {
                return initialValue;
            }
        };
    }

    @ReturnNonNull
    public static <T> Predicate1<T> noCheck() {
        return NO_CHECK;
    }

    @ReturnNonNull
    public static <T> Predicate1<T> nonNull() {
        return NON_NULL;
    }

    private StandardFunctions() {
    }

}
