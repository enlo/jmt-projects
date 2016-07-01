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

import java.io.Serializable;
import java.util.Comparator;

/**
 * 比較可能な型Tのオブジェクトの{@link Comparator}.
 * <ol>
 * <li>o1 == o2 なら 0.
 * <li>o1 == null なら -1.
 * <li>o2 == null なら 1.
 * <li>それ以外なら o1.compareTo(o2) を戻す.
 * </ol>
 *
 * @author enlo
 * @param <T> 型T
 *
 */
public class ComparableComparator<T extends Comparable<T>> implements Comparator<T>, Serializable {

    /**
     *
     */
    public static final ComparableComparator INSTANCE = new ComparableComparator();
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        }
        else if (o1 == null) {
            return -1;
        }
        else if (o2 == null) {
            return 1;
        }
        else {
            return doCompare(o1, o2);
        }
    }

    /**
     * 値の小さなほうを取得する.
     *
     * @param o1
     * @param o2
     * @return
     */
    public final T min(T o1, T o2) {
        if (o1 == o2) {
            return o1;
        }
        else if (o1 == null) {
            return o2;
        }
        else if (o2 == null) {
            return o1;
        }
        else {
            return doCompare(o1, o2) < 0 ? o1 : o2;
        }
    }

    /**
     * 比較メソッド.
     *
     * @param o1
     * @param o2
     * @return
     */
    protected int doCompare(T o1, T o2) {
        return o1.compareTo(o2);
    }

}
