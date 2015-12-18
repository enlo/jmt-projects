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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import static java.lang.System.arraycopy;
import java.lang.reflect.Array;
import static java.util.Arrays.sort;
import java.util.Iterator;
import static java.util.Objects.deepEquals;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Stream が使用できない Java7 で配列を扱うためのユーティリティ.
 *
 * @author enlo
 */
public class ClassicArrayUtils {

    public static <T> ArrayIterable<T> arrayAsIterable(T[] array) {
        return new ArrayIterable<>(array);
    }

    public static <T extends Comparable<T>> int arrayCompareTo(T[] arr1, T[] arr2) {
        return arrayCompareTo(arr1, 0, arr2, 0);
    }

    public static <T extends Comparable<T>> int arrayCompareTo(T[] arr1, int pos1, T[] arr2, int pos2) {
        nonNull(arr1, "arr1");
        nonNull(arr2, "arr2");
        int len1 = arr1.length - pos1;
        int len2 = arr2.length - pos2;
        return arrayCompareTo(arr1, pos1, len1, arr2, pos2, len2);
    }

    public static <T extends Comparable<T>> int arrayCompareTo(T[] arr1, int pos1, int len1, T[] arr2, int pos2, int len2) {
        int lim1 = pos1 + len1;
        int lim2 = pos2 + len2;
        nonNull(arr1, "arr1");
        nonNull(arr2, "arr2");
        Arguments.between(pos1, 0, arr1.length - 1, "pos1");
        Arguments.between(pos2, 0, arr2.length - 1, "pos2");
        for (int i1 = pos1, i2 = pos2; i1 < lim1 && i2 < lim2; i1++, i2++) {
            int diff = arr1[i1].compareTo(arr2[i2]);
            if (diff != 0) {
                return diff;
            }
        }
        return len1 - len2;
    }

    /**
     * コレクションから受け入れ可能なものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param predicate 検索
     * @return 等価な項目があれば true.
     */
    public static <T> boolean arrayContains(T[] items, Predicate1<? super T> predicate) {
        nonNull(predicate, "predicate");
        if (items == null) {
            return false;
        }
        for (T item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * コレクションから等価であるものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param valueToFind 検索する値
     * @return 等価な項目があれば true.
     */
    public static  <T extends Comparable<T>> boolean arrayContainsCompareEquals(T[] items, T valueToFind) {
        if (items == null) {
            return false;
        }
        for (T item : items) {
            if (item.compareTo(valueToFind) == 0) {
                return true;
            }
        }
        return false;
    }

    public static  boolean arrayEqualsInRange(byte[] arr1, int pos1, byte[] arr2, int pos2, int len) {
        nonNull(arr1, "arr1");
        nonNull(arr2, "arr2");
        Arguments.between(pos1, 0, arr1.length - 1, "pos1");
        Arguments.between(pos2, 0, arr2.length - 1, "pos2");
        Arguments.between(pos1 + len, 0, arr1.length, "len");
        Arguments.between(pos2 + len, 0, arr2.length, "len");

        int to = pos1 + len;
        for (int i1 = pos1, i2 = pos2; i1 < to; i1++, i2++) {
            if (arr1[i1] != arr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static  boolean arrayEqualsInRange(int[] arr1, int pos1, int[] arr2, int pos2, int len) {
        nonNull(arr1, "arr1");
        nonNull(arr2, "arr2");
        Arguments.between(pos1, 0, arr1.length - 1, "pos1");
        Arguments.between(pos2, 0, arr2.length - 1, "pos2");
        Arguments.between(pos1 + len, 0, arr1.length, "len");
        Arguments.between(pos2 + len, 0, arr2.length, "len");

        int to = pos1 + len;
        for (int i1 = pos1, i2 = pos2; i1 < to; i1++, i2++) {
            if (arr1[i1] != arr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean arrayEqualsInRange(T[] arr1, int pos1, T[] arr2, int pos2, int len) {
        nonNull(arr1, "arr1");
        nonNull(arr2, "arr2");
        Arguments.between(pos1, 0, arr1.length - 1, "pos1");
        Arguments.between(pos2, 0, arr2.length - 1, "pos2");
        Arguments.between(pos1 + len, 0, arr1.length, "len");
        Arguments.between(pos2 + len, 0, arr2.length, "len");
        int to = pos1 + len;
        for (int i1 = pos1, i2 = pos2; i1 < to; i1++, i2++) {
            if (!deepEquals(arr1[i1], arr2[i2])) {
                return false;
            }
        }
        return true;
    }
    

    /**
     *
     * @param <T>
     * @param items
     * @return
     */
    @SafeVarargs
    public static <T extends Comparable<T>> T[] arraySort(T... items) {
        sort(items, new ComparableComparator<T>());
        return items;
    }

    /**
     * 文字列連結.
     *
     * @param <T>
     * @param items 連結する項目
     * @return 連結された文字列.
     */
    @ReturnNonNull
    public static <T> String arrayToString(T[] items) {
        return (StringJoiner.SIMPLE).join(items).toString();
    }

    /**
     * arrayOfFirstAndMore
     *
     * @param <T>
     * @param first
     * @param more 追加
     * @return
     */
    @ReturnNonNull
    @SuppressWarnings(value = "unchecked")
    public static <T> T[] createArray(T first, T... more) {
        assert more != null;
        T[] array = (T[]) Array.newInstance(more.getClass().getComponentType(), more.length + 1);
        Array.set(array, 0, first);
        arraycopy(more, 0, array, 1, more.length);
        return array;
    }

    
    @Value
    public static final class ArrayIterable<T> implements Iterable<T> {

        final T[] array;

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<>(array);
        }

    }

    /**
     *
     * @author enlo
     * @param <T>
     */
    @RequiredArgsConstructor
    public static final class ArrayIterator<T> implements Iterator<T> {

        @NonNull
        final T[] array;
        int i = 0;

        @Override
        public boolean hasNext() {
            return i < array.length;
        }

        @Override
        public T next() {
            return array[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }


}
