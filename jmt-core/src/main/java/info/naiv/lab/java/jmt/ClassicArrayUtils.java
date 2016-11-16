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

import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import static java.lang.System.arraycopy;
import java.lang.reflect.Array;
import static java.util.Arrays.sort;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import static java.util.Objects.deepEquals;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author enlo
 */
public class ClassicArrayUtils {

    /**
     * 配列を {@link Iterable} に変換する.
     *
     * @param <T>
     * @param array
     * @return
     */
    @Nonnull
    public static <T> Iterable<T> arrayAsIterable(@Nonnull T[] array) {
        return new ArrayIterable<>(array);
    }

    /**
     * 配列の中身が等しいかチェック.
     *
     * @param <T>
     * @param arr1
     * @param arr2
     * @return
     */
    public static <T extends Comparable<T>> int arrayCompareTo(@Nonnull T[] arr1,
                                                               @Nonnull T[] arr2) {
        return arrayCompareTo(arr1, 0, arr2, 0);
    }

    /**
     * 開始位置からの中身を比較する.
     *
     * @param <T>
     * @param arr1
     * @param pos1
     * @param arr2
     * @param pos2
     * @return
     */
    public static <T extends Comparable<T>>
            int arrayCompareTo(@NonNull T[] arr1, int pos1,
                               @NonNull T[] arr2, int pos2) {
        int len1 = arr1.length - pos1;
        int len2 = arr2.length - pos2;
        return arrayCompareTo(arr1, pos1, len1, arr2, pos2, len2);
    }

    /**
     * 部分配列の中身を比較する.
     *
     * @param <T>
     * @param arr1
     * @param pos1
     * @param len1
     * @param arr2
     * @param pos2
     * @param len2
     * @return
     */
    public static <T extends Comparable<T>>
            int arrayCompareTo(@NonNull T[] arr1, int pos1, int len1,
                               @NonNull T[] arr2, int pos2, int len2) {
        int lim1 = pos1 + len1;
        int lim2 = pos2 + len2;
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
     * 配列内に要素が存在するかどうか.
     *
     * @param <T> 要素の型
     * @param array 配列
     * @param search 探す要素.
     * @return
     */
    public static <T> boolean arrayContains(T[] array, T search) {
        if (array != null) {
            for (T v : array) {
                if (Objects.equals(v, search)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * コレクションに項目があるかどうか
     *
     * @param <T> 要素の型
     * @param array コレクションから等価であるものを取得
     * @param predicate 検索
     * @return 等価な項目があれば true.
     */
    public static <T> boolean arrayContains(T[] array, Predicate1<? super T> predicate) {
        return 0 <= arrayIndexOf(array, predicate);
    }

    /**
     * コレクションから等価であるものを取得
     *
     * @param <T> 要素の型
     * @param array コレクションから等価であるものを取得
     * @param search 検索する値
     * @return 等価な項目があれば true.
     */
    public static <T extends Comparable<T>> boolean arrayContainsCompareEquals(T[] array, T search) {
        return 0 <= arrayIndexOf(array, StandardFunctions.compareEqual(search));
    }

    /**
     * 部分配列の中身が等しいかチェックする.
     *
     * @param arr1
     * @param pos1
     * @param arr2
     * @param pos2
     * @param len
     * @return
     */
    public static boolean arrayEqualsInRange(@NonNull byte[] arr1, int pos1,
                                             @NonNull byte[] arr2, int pos2, int len) {
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

    /**
     * 部分配列の中身が等しいかチェックする.
     *
     * @param arr1
     * @param pos1
     * @param arr2
     * @param pos2
     * @param len
     * @return
     */
    public static boolean arrayEqualsInRange(@NonNull int[] arr1, int pos1,
                                             @NonNull int[] arr2, int pos2, int len) {
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

    /**
     * 部分配列の中身が等しいかチェックする.
     *
     * @param <T>
     * @param arr1
     * @param pos1
     * @param arr2
     * @param pos2
     * @param len
     * @return
     */
    public static <T> boolean arrayEqualsInRange(@NonNull T[] arr1, int pos1,
                                                 @NonNull T[] arr2, int pos2, int len) {
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
     * 条件を満たす要素のインデックスを取得する.
     *
     * @param <T> 値の型
     * @param array 配列
     * @param predicate 条件
     * @return 条件に一致する要素のインデックス. 存在しない場合、-1
     */
    public static <T> int arrayIndexOf(T[] array, @NonNull Predicate1<? super T> predicate) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 一致する要素のインデックスを取得する.
     *
     * @param <T> 値の型
     * @param array 配列
     * @param search 要素
     * @return 一致する要素のインデックス. 存在しない場合、-1
     */
    public static <T> int arrayIndexOf(T[] array, T search) {
        return arrayIndexOf(array, StandardFunctions.equal(search));
    }

    /**
     * 配列を並び替える.
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
    @Nonnull
    public static <T> String arrayToString(T... items) {
        return (StringJoiner.SIMPLE).join(items).toString();
    }

    /**
     * 引数 array が配列だった場合、Object[] を戻す. <br>
     * 引数が基本型の配列だった場合、Object[] にコピーする.<br>
     * 引数が配列でない場合、null を戻す.
     *
     * @param array 引数
     * @return array が配列ならObject[]、そうでなければ null.
     */
    @CheckForNull
    public static Object[] asObjectArray(Object array) {
        Object[] result = null;
        if (array instanceof Object[]) {
            return (Object[]) array;
        }
        else if (array != null) {
            Class<?> clazz = array.getClass();
            if (clazz.isArray()) {
                int length = Array.getLength(array);
                result = new Object[length];
                for (int i = 0; i < length; i++) {
                    result[i] = Array.get(array, i);
                }
            }
        }
        return result;
    }

    /**
     * 配列を作成する.
     *
     * @param <T>
     * @param first
     * @param more 追加
     * @return
     */
    @Nonnull
    @SuppressWarnings(value = "unchecked")
    public static <T> T[] createArray(T first, T... more) {
        assert more != null;
        T[] array = (T[]) Array.newInstance(more.getClass().getComponentType(), more.length + 1);
        Array.set(array, 0, first);
        arraycopy(more, 0, array, 1, more.length);
        return array;
    }

    /**
     * オブジェクトが指定された型の配列かどうかをチェックする.
     *
     * @param <T> 型パラメータT
     * @param array 配列オブジェクト
     * @param clazz 型情報.
     * @return array が T[] なら true.
     */
    public static <T> boolean isArrayOf(Object array, @Nonnull Class<T> clazz) {
        if (array != null) {
            Class<?> aclz = array.getClass();
            if (aclz.isArray()) {
                Class ofArray = aclz.getComponentType();
                return ofArray.equals(clazz);
            }
        }
        return false;
    }

    /**
     * 配列用 Iterator.
     *
     * @author enlo
     * @param <T>
     */
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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
            if (array.length <= i) {
                throw new NoSuchElementException();
            }
            return array[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    /**
     * 配列用 Iterable.
     *
     * @param <T>
     */
    static final class ArrayIterable<T> implements Iterable<T> {

        final T[] array;

        ArrayIterable(T[] array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<>(array);
        }

    }

    private ClassicArrayUtils() {
    }

}
