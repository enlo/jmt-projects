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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arraySort;
import info.naiv.lab.java.jmt.IterationUnit;

public class Ranges {

    public static <T extends Comparable<T>> Range<T> closedOpenRange(T first, T end) {
        return closedOpenRange(first, end, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Range<T> closedOpenRange(T first, T end, IterationUnit<? super T> unit) {
        T[] item = arraySort(first, end);
        return new Range(unit, new Bound.ClosedLowerBound<>(item[0]), new Bound.OpenUpperBound<>(item[1]));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Range<T> closedRange(T first, T end, IterationUnit<? super T> unit) {
        T[] item = arraySort(first, end);
        return new Range(unit, new Bound.ClosedLowerBound<>(item[0]), new Bound.ClosedUpperBound<>(item[1]));
    }

    public static <T extends Comparable<T>> Range<T> closedRange(T first, T end) {
        return closedRange(first, end, null);
    }

    public static <T extends Comparable<T>> Range<T> openClosedRange(T first, T end) {
        return openClosedRange(first, end, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Range<T> openClosedRange(T first, T end, IterationUnit<? super T> unit) {
        T[] item = arraySort(first, end);
        return new Range(unit, new Bound.OpenLowerBound<>(item[0]), new Bound.ClosedUpperBound<>(item[1]));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Range<T> openRange(T first, T end, IterationUnit<? super T> unit) {
        T[] item = arraySort(first, end);
        return new Range(unit, new Bound.OpenLowerBound<>(item[0]), new Bound.OpenUpperBound<>(item[1]));
    }

    public static <T extends Comparable<T>> Range<T> openRange(T first, T end) {
        return openRange(first, end, null);
    }

    private Ranges() {
    }

}
