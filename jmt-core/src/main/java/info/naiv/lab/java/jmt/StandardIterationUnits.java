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

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author enlo
 */
public class StandardIterationUnits {

    /**
     *
     */
    public static final IterationUnit<Date> DATE = new AbstractIterationUnit<Date>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Date advance(Date value, long n) {
            return new Date(value.getTime() + n);
        }

        @Override
        public long doDistance(Date lhs, Date rhs) {
            return rhs.getTime() - lhs.getTime();
        }

        @Override
        protected int doCompare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }

    };

    /**
     *
     */
    public static final IterationUnit<Date> DATE_DAY_NOTRUNC = new DateDayIterationUnit();

    /**
     *
     */
    public static final IterationUnit<Date> DATE_DAY_TRUNCATE = new DateDayIterationUnit() {
        private static final long serialVersionUID = 1L;

        @Override
        public Date truncate(Date value) {
            long days = TimeUnit.MILLISECONDS.toDays(value.getTime());
            long mills = TimeUnit.DAYS.toMillis(days);
            return new Date(mills);
        }
    };

    /**
     *
     */
    public static final IterationUnit<Double> DOUBLE = new AbstractIterationUnit<Double>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Double advance(Double value, long n) {
            if (n == 0) {
                return value;
            }
            double dir = n < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            long c = Math.abs(n);
            double d = value;
            for (int i = 0; i < c; i++) {
                d = Math.nextAfter(d, dir);
            }
            return d;
        }

        @Override
        protected int doCompare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }

        @Override
        protected long doDistance(Double o1, Double o2) {
            if (o1.isInfinite() || o1.isNaN() || o2.isInfinite() || o2.isNaN()) {
                throw new IllegalArgumentException("o1 or o2 is invalid number.");
            }
            else if (o1.equals(o2)) {
                return 0;
            }
            long sign;
            double fr;
            double to;
            if (o1 < o2) {
                sign = 1;
                fr = o1;
                to = o2;
            }
            else {
                sign = -1;
                fr = o2;
                to = o1;
            }
            long n = 0;
            while (fr < to) {
                n++;
                fr = Math.nextUp(fr);
            }
            return sign * n;
        }
    };

    /**
     *
     */
    public static final IterationUnit<Integer> INTEGER = new AbstractIterationUnit<Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Integer advance(Integer value, long n) {
            return (int) (value + n);
        }

        @Override
        public long doDistance(Integer lhs, Integer rhs) {
            return rhs - lhs;
        }

        @Override
        protected int doCompare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }

    };

    /**
     *
     */
    public static final IterationUnit<Number> NUMBER_TO_LONG = new AbstractIterationUnit<Number>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Number advance(Number value, long n) {
            return value.longValue() + n;
        }

        @Override
        public Number truncate(Number value) {
            return value.longValue();
        }

        @Override
        protected int doCompare(Number o1, Number o2) {
            return Long.compare(o1.longValue(), o2.longValue());
        }

        @Override
        protected long doDistance(Number o1, Number o2) {
            return o2.longValue() - o1.longValue();
        }
    };

    /**
     *
     * @param step
     * @return
     */
    public static final IterationUnit<Integer> step(final int step) {
        return new AbstractIterationUnit<Integer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Integer advance(Integer value, long n) {
                return (int) (value + (n * step));
            }

            @Override
            protected int doCompare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }

            @Override
            protected long doDistance(Integer lhs, Integer rhs) {
                return (rhs - lhs) / step;
            }

        };
    }

    private StandardIterationUnits() {
    }

    static abstract class AbstractIterationUnit<T> implements IterationUnit<T> {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(T o1, T o2) {
            o1 = truncate(o1);
            o2 = truncate(o2);
            return doCompare(o1, o2);
        }

        @Override
        public long distance(T lhs, T rhs) {
            lhs = truncate(lhs);
            rhs = truncate(rhs);
            return doDistance(lhs, rhs);
        }

        @Override
        public T next(T value) {
            return advance(value, 1);
        }

        @Override
        public T prior(T value) {
            return advance(value, -1);
        }

        @Override
        public T truncate(T value) {
            return value;
        }

        protected abstract int doCompare(T o1, T o2);

        protected abstract long doDistance(T o1, T o2);

    }

    static class DateDayIterationUnit extends AbstractIterationUnit<Date> {

        private static final long serialVersionUID = 1L;

        @Override
        public Date advance(Date value, long n) {
            return new Date(value.getTime() + TimeUnit.DAYS.toMillis(n));
        }

        @Override
        public long doDistance(Date lhs, Date rhs) {
            return TimeUnit.MILLISECONDS.toDays(rhs.getTime() - lhs.getTime());
        }

        @Override
        protected int doCompare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }

    }
}
