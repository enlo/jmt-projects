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

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
public class KeyedValueCollection<TKey extends Comparable, TValue extends KeyedValue<TKey>>
        extends AbstractCollection<TValue> implements Lookup<TKey, TValue>, Serializable {

    private static final long serialVersionUID = 1921610722686903046L;

    private final ArrayList<TValue> values;

    public KeyedValueCollection() {
        values = new ArrayList<>();
    }

    public KeyedValueCollection(int capacity) {
        values = new ArrayList<>(capacity);
    }

    public KeyedValueCollection(Collection<? extends TValue> initialValues) {
        values = new ArrayList<>(initialValues.size());
        addAll(initialValues);
    }

    @Override
    public boolean add(@Nonnull TValue e) {
        int index = indexOf(e.getKey());
        if (0 <= index) {
            return false;
        }
        return values.add(e);
    }

    @Override
    public boolean containsKey(TKey key) {
        return get(key) != null;
    }

    public TValue get(int index) {
        return values.get(index);
    }

    @Override
    public TValue get(TKey key) {
        for (TValue v : values) {
            if (v.getKey().compareTo(key) == 0) {
                return v;
            }
        }
        return null;
    }

    public int indexOf(TKey key) {
        int sz = values.size();
        for (int i = 0; i < sz; i++) {
            TValue v = values.get(i);
            if (v.getKey().compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public Iterator<TValue> iterator() {
        return values.iterator();
    }

    public TValue put(@Nonnull TValue e) {
        int index = indexOf(e.getKey());
        if (0 <= index) {
            return values.set(index, e);
        }
        values.add(e);
        return null;
    }

    public void putAll(Collection<? extends TValue> values) {
        this.values.ensureCapacity(values.size());
        for (TValue val : values) {
            put(val);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (values.remove(o)) {
            return true;
        }
        if (o instanceof Comparable) {
            try {
                return remove((TKey) o) != null;
            }
            catch (ClassCastException ex) {
                return false;
            }
        }
        return false;
    }

    public TValue remove(int index) {
        return values.remove(index);
    }

    public TValue remove(TKey key) {
        int index = indexOf(key);
        if (0 <= index) {
            return values.remove(index);
        }
        return null;
    }

    @Override
    public int size() {
        return values.size();
    }

}
