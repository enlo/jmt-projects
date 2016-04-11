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
package info.naiv.lab.java.jmt.collection;

import info.naiv.lab.java.jmt.mark.ThreadSafety;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class ListBuilder<T> extends CollectionBuilder<T> {

    /**
     * {@link ArrayList } builder
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> ListBuilder<T> arrayList(T... values) {
        return new ArrayListBuilder(values);
    }

    /**
     * {@link CopyOnWriteArrayList } builder. <br>
     * Thread safety.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> ListBuilder<T> cowArrayList(T... values) {
        return new CopyOnWriteArrayListBuilder(values);
    }

    /**
     * {@link LinkedList} builder.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> ListBuilder<T> linkedList(T... values) {
        return new CopyOnWriteArrayListBuilder(values);
    }

    public ListBuilder(Collection<T> collection) {
        super(collection);
    }

    @Override
    public abstract List<T> build();

    public List<T> toList() {
        return build();
    }

    static class ArrayListBuilder<T> extends ListBuilder<T> {

        ArrayListBuilder(T... values) {
            super(new ArrayList<>(Arrays.asList(values)));
        }

        @Override
        public List<T> build() {
            return new ArrayList<>(collection);
        }
    }

    @ThreadSafety
    static class CopyOnWriteArrayListBuilder<T> extends ListBuilder<T> {

        CopyOnWriteArrayListBuilder(T... values) {
            super(new CopyOnWriteArrayList<>(values));
        }

        @Override
        public List<T> build() {
            return new CopyOnWriteArrayList<>(collection);
        }

    }

    static class LinkedListBuilder<T> extends ListBuilder<T> {

        LinkedListBuilder(T... values) {
            super(Arrays.asList(values));
        }

        @Override
        public List<T> build() {
            return new LinkedList<>(collection);
        }

    }
}
