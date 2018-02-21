/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.collection;

import info.naiv.lab.java.jmt.Arguments;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author enlo
 */
public class LimitedLinkedList<T> extends LinkedList<T> {

    private static final long serialVersionUID = 1L;
    private int maxSize;

    public LimitedLinkedList() {
        this(Integer.MAX_VALUE);
    }

    public LimitedLinkedList(int maxSize) {
        Arguments.nonNegative(maxSize, "maxSize");
        this.maxSize = maxSize;
    }

    public LimitedLinkedList(int maxSize, Collection<? extends T> c) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T e) {
        if (size() >= maxSize) {
            remove(0);
        }
        return super.add(e);
    }

    @Override
    public void add(int index, T element) {
        if (size() >= maxSize) {
            remove(0);
        }
        super.add(index, element); 
    }

}
