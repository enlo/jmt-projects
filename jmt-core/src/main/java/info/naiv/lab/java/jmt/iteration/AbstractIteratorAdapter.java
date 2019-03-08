/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public abstract class AbstractIteratorAdapter<Src, Dst> implements Iterator<Dst> {

    private boolean hasNext;
    private boolean initialized;
    private Dst nextElement;

    @Override
    public final boolean hasNext() {
        if (!initialized) {
            init();
            fetch();
            initialized = true;
        }
        return hasNext;
    }

    @Override
    public final Dst next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return fetch();
    }

    @Override
    public void remove() {
        getBaseIterator().remove();
    }

    protected Dst fetch() {
        Dst result = nextElement;
        Iterator<Src> i = getBaseIterator();
        while (i.hasNext()) {
            Src v = i.next();
            if (filter(v)) {
                hasNext = true;
                nextElement = map(v);
                return result;
            }
        }
        hasNext = false;
        return result;
    }

    protected boolean filter(Src value) {
        return true;
    }

    @Nonnull
    protected abstract Iterator<Src> getBaseIterator();

    protected void init() {

    }

    protected Dst map(Src value) {
        return (Dst) value;
    }

}