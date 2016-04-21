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
 * iterables copies or substantial portions of the Software.
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

import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.EqualsAndHashCode;

/**
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode
public final class FlatIterableIterator<T> implements Iterator<T> {

    Iterator<T> currentIterator;
    final Iterator<? extends Iterable<T>> iterables;

    public FlatIterableIterator(Iterable<? extends Iterable<T>> iterators) {
        this.iterables = iterators.iterator();
        nextIterator();
    }

    @Override
    public boolean hasNext() {
        Iterator<T> it = nextIterator();
        return it != null;
    }

    @Override
    public T next() {
        Iterator<T> it = currentIterator;
        if (it == null) {
            throw new NoSuchElementException();
        }
        return it.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Iterator<T> nextIterator() {
        if (currentIterator != null && currentIterator.hasNext()) {
            return currentIterator;
        }
        while (iterables.hasNext()) {
            Iterable<T> e = iterables.next();
            if (e != null) {
                this.currentIterator = e.iterator();
                if (this.currentIterator.hasNext()) {
                    return currentIterator;
                }
            }
        }
        return null;
    }
}
