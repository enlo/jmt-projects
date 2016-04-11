/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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
package info.naiv.lab.java.jmt.monad;

import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author enlo
 */
public interface Iteratee<T> extends Iterable<T>, Serializable {

    IterateeImpl<T> bind(Consumer1<? super T> consumer);

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return predicate が true を戻したオブジェクトのみを戻す.
     */
    @ReturnNonNull
    IterateeImpl<T> filter(Predicate1<? super T> predicate);

    /**
     * 最初の値を戻す. 値がなければエラー.
     *
     * @return 最初の値.
     */
    T first();

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    <U> IterateeImpl<U> flatMap(Function1<? super T, IterateeImpl<U>> mapper);

    /**
     *
     * @return 値が空でなければ true.
     */
    boolean isPresent();

    @Override
    Iterator<T> iterator();

    @ReturnNonNull
    <U> IterateeImpl<U> map(Function1<? super T, ? extends U> mapper);

    @ReturnNonNull
    List<T> toList();

    @ReturnNonNull
    <K> Map<K, T> toMap(Function1<T, K> keyResolver);
    
}
