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

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import lombok.ToString;

/**
 *
 * @author enlo
 * @param <T>
 */
@ToString
public class Repeater<T> implements Iterable<T> {

    final int repeatMax;
    final T value;

    /**
     *
     * @param repeatMax
     * @param value
     */
    public Repeater(int repeatMax, T value) {
        this.repeatMax = repeatMax;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public T getValue() {
        return value;
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new RepeatIterator();
    }

    class RepeatIterator implements Iterator<T> {

        int count = 0;

        @Override
        public boolean hasNext() {
            return count < repeatMax;
        }

        @Override
        public T next() {
            if (repeatMax <= count) {
                throw new NoSuchElementException();
            }
            count++;
            return getValue();
        }

        @Override
        public void remove() {
        }
    }

}
