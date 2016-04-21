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
package info.naiv.lab.java.jmt.iteration;

import info.naiv.lab.java.jmt.fx.Predicate1;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <T>
 */
public final class FilteringIterator<T> implements Iterator<T> {

    @NonNull
    private final Iterator<T> baseIterator;
    private boolean cont;
    @NonNull
    private final Predicate1<? super T> filter;

    private T nextElement;

    /**
     * コンストラクター.
     *
     * @param iterator 基本となる反復子
     * @param filter フィルター
     */
    public FilteringIterator(Iterator<T> iterator, Predicate1<? super T> filter) {
        this.baseIterator = iterator;
        this.filter = filter;
        doNext();
    }

    @Override
    public boolean hasNext() {
        return cont;
    }

    @Override
    public T next() {
        if (!cont) {
            throw new NoSuchElementException();
        }
        return doNext();
    }

    @Override
    public void remove() {
        baseIterator.remove();
    }

    private T doNext() {
        T result = nextElement;
        while (baseIterator.hasNext()) {
            T v = baseIterator.next();
            if (filter.test(v)) {
                cont = true;
                nextElement = v;
                return result;
            }
        }
        cont = false;
        return result;
    }
}
