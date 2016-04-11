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
package info.naiv.lab.java.jmt.iterator;

import info.naiv.lab.java.jmt.fx.Function1;
import java.util.Iterator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author enlo
 * @param <T>
 * @param <R>
 */
@RequiredArgsConstructor
public class MappingIterator<T, R> implements Iterator<R> {

    @NonNull
    final Iterator<T> baseIterator;
    @NonNull
    final Function1<? super T, ? extends R> mapper;

    @Override
    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(baseIterator.next());
    }

    @Override
    public void remove() {
        baseIterator.remove();
    }

}