/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.tuple;

import java.io.Serializable;
import java.util.Comparator;
import lombok.Value;

public class Tuples {

    public static <T1 extends Comparable>
            Comparator<Tuple1<T1>> getTuple1Comparator() {
        return new Comparator<Tuple1<T1>>() {
            @Override
            public int compare(Tuple1<T1> o1, Tuple1<T1> o2) {
                return o1.getValue1().compareTo(o2.getValue1());
            }
        };
    }

    public static <T1 extends Comparable, T2 extends Comparable>
            Comparator<Tuple2<T1, T2>> getTuple2Comparator() {
        return new Comparator<Tuple2<T1, T2>>() {
            @Override
            public int compare(Tuple2<T1, T2> o1, Tuple2<T1, T2> o2) {
                int c = o1.getValue1().compareTo(o2.getValue1());
                if (c == 0) {
                    c = o1.getValue2().compareTo(o2.getValue2());
                }
                return c;
            }
        };
    }

    public static <T1 extends Comparable, T2 extends Comparable, T3 extends Comparable>
            Comparator<Tuple3<T1, T2, T3>> getTuple3Comparator() {
        return new Comparator<Tuple3<T1, T2, T3>>() {
            @Override
            public int compare(Tuple3<T1, T2, T3> o1, Tuple3<T1, T2, T3> o2) {
                int c = o1.getValue1().compareTo(o2.getValue1());
                if (c == 0) {
                    c = o1.getValue2().compareTo(o2.getValue2());
                    if (c == 0) {
                        c = o1.getValue3().compareTo(o2.getValue3());
                    }
                }
                return c;
            }
        };
    }

    public static <T1 extends Comparable, T2 extends Comparable, T3 extends Comparable, T4 extends Comparable>
            Comparator<Tuple4<T1, T2, T3, T4>> getTuple4Comparator() {
        return new Comparator<Tuple4<T1, T2, T3, T4>>() {
            @Override
            public int compare(Tuple4<T1, T2, T3, T4> o1, Tuple4<T1, T2, T3, T4> o2) {
                int c = o1.getValue1().compareTo(o2.getValue1());
                if (c == 0) {
                    c = o1.getValue2().compareTo(o2.getValue2());
                    if (c == 0) {
                        c = o1.getValue3().compareTo(o2.getValue3());
                        if (c == 0) {
                            c = o1.getValue4().compareTo(o2.getValue4());
                        }
                    }
                }
                return c;
            }
        };
    }

    public static <T> Tuple1<T> tie(T value) {
        return new Tuple1Impl(value);
    }

    public static <T1, T2> Tuple2<T1, T2> tie(T1 value1, T2 value2) {
        return new Tuple2Impl(value1, value2);
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> tie(T1 value1, T2 value2, T3 value3) {
        return new Tuple3Impl(value1, value2, value3);
    }

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> tie(T1 value1, T2 value2, T3 value3, T4 value4) {
        return new Tuple4Impl(value1, value2, value3, value4);
    }

    private Tuples() {
    }

    @Value
    public static class Tuple1Impl<T> implements Tuple1<T>, Serializable {

        private static final long serialVersionUID = 1544940561288300623L;

        T value1;
    }

    @Value
    public static class Tuple2Impl<T1, T2> implements Tuple2<T1, T2>, Serializable {

        private static final long serialVersionUID = -3173491363515736637L;

        T1 value1;
        T2 value2;
    }

    @Value
    public static class Tuple3Impl<T1, T2, T3> implements Tuple3<T1, T2, T3>, Serializable {

        private static final long serialVersionUID = 8669142330621944242L;

        T1 value1;
        T2 value2;
        T3 value3;
    }

    @Value
    public static class Tuple4Impl<T1, T2, T3, T4> implements Tuple4<T1, T2, T3, T4>, Serializable {

        private static final long serialVersionUID = 3698906526876032138L;

        T1 value1;
        T2 value2;
        T3 value3;
        T4 value4;
    }

}
