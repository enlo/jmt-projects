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
package info.naiv.lab.java.jmt.monad;

import info.naiv.lab.java.jmt.FinalizerGuardian;
import info.naiv.lab.java.jmt.closeable.Closeables;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.Iterator;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <T>
 */
@Value
public class SingleMonad<T> implements AutoCloseable, Iterable<T> {

    final T value;

    final FinalizerGuardian fg = new FinalizerGuardian() {
        @Override
        protected void onFinalize() throws Exception {
            close();
        }
    };

    @Override
    public final void close() throws Exception {
        Closeables.of(value).close();
        fg.setClosed(true);
    }

    @Override
    public Iterator<T> iterator() {
        return new SingleIterator(true, get());
    }

    @ReturnNonNull
    public <U> SingleMonad<U> map(Function1<? super T, ? extends U> mapper) {
        return of(mapper.apply(get()));
    }

    public static <T> SingleMonad<T> of(T value) {
        return new SingleMonad<>(value);
    }

    /**
     * フィルター処理.
     *
     * @param predicate 述語オブジェクト
     * @return {@link #isPresent()} && predicate が true を戻せば自分自身. <br>
     * そうでなければ {@link #empty()}
     */
    @ReturnNonNull
    public Optional<T> filter(Predicate1<? super T> predicate) {
        return predicate.test(get()) ? Optional.of(get()) : Optional.<T>empty();
    }

    /**
     * フラットマップ.
     *
     * @param <U> 戻り値の型
     * @param mapper 変換オブジェクト
     * @return
     */
    @ReturnNonNull
    public <U> SingleMonad<U> flatMap(Function1<? super T, ? extends SingleMonad<U>> mapper) {
        return mapper.apply(get());
    }

    /**
     *
     * @return 値
     */
    public T get() {
        return value;
    }

    public SingleMonad<T> bind(Consumer1<? super T> consumer) {
        consumer.accept(get());
        return this;
    }

}
