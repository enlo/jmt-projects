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


public class StandardIterationUnits {


    public static final IterationUnit<Date> DATE = new AbstractIterationUnit<Date>() {

        @Override
        public Date advance(Date value, long n) {
            return new Date(value.getTime() + n);
        }

        @Override
        public long distance(Date lhs, Date rhs) {
            return rhs.getTime() - lhs.getTime();
        }

        @Override
        protected int doCompare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }

    };
    public static final IterationUnit<Integer> INTEGER = new AbstractIterationUnit<Integer>() {

        @Override
        public Integer advance(Integer value, long n) {
            return (int) (value + n);
        }

        @Override
        public long distance(Integer lhs, Integer rhs) {
            return rhs - lhs;
        }

        @Override
        protected int doCompare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }

    };

    private StandardIterationUnits() {
    }

    static abstract class AbstractIterationUnit<T> implements IterationUnit<T> {
        
        protected abstract int doCompare(T o1, T o2);
        
        @Override
        public T next(T value) {
            return advance(value, 1);
        }

        @Override
        public T prior(T value) {
            return advance(value, -1);
        }

        @Override
        public int compare(T o1, T o2) {
            o1 = truncate(o1);
            o2 = truncate(o2);
            return doCompare(o1, o2);
        }
        
        @Override
        public T truncate(T value) {
            return value;
        }
    }
}
