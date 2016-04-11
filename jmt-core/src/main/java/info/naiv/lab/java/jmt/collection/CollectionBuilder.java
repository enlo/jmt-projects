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

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class CollectionBuilder<T> {

    protected final Collection<T> collection;

    /**
     *
     * @param collection
     */
    public CollectionBuilder(Collection<T> collection) {
        this.collection = collection;
    }

    /**
     * コレクションに値を追加.
     *
     * @param value 値.
     * @return コレクションビルダー
     */
    public CollectionBuilder<T> add(T value) {
        collection.add(value);
        return this;
    }

    /**
     * コレクションに値を追加.
     *
     * @param values
     * @return
     */
    public CollectionBuilder<T> addAll(T... values) {
        collection.addAll(Arrays.asList(values));
        return this;
    }

    public abstract Collection<T> build();
}
