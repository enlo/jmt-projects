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
import info.naiv.lab.java.jmt.fx.Supplier;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author enlo
 * @param <T>
 */
public interface Optional<T> extends Iterable<T>, Serializable {

    Optional EMPTY = new OptionalImpl(null);

    Optional<T> bind(Consumer1<? super T> consumer);

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return {@link #isPresent()} &amp;&amp; predicate が true を戻せば自分自身. <br>
     * そうでなければ {@link #empty()}
     */
    @ReturnNonNull
    Optional<T> filter(Predicate1<? super T> predicate);

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    <U> Optional<U> flatMap(Function1<? super T, ? extends Optional<U>> mapper);

    /**
     *
     * @return 値
     */
    T get();

    void ifPresent(Consumer1<? super T> consumer);

    /**
     *
     * @return 値が null でなければ true.
     */
    boolean isPresent();

    @ReturnNonNull
    @Override
    Iterator<T> iterator();

    @ReturnNonNull
    <U> Optional<U> map(Function1<? super T, ? extends U> mapper);

    @ReturnNonNull
    T orElse(T other);

    @ReturnNonNull
    T orElseGet(Supplier<? extends T> other);

    @ReturnNonNull
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    @ReturnNonNull
    Set<T> toSet();

}
