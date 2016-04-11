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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.iterator.MappingIterator;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
public class KeyedCollection<TKey extends Comparable, TValue extends KeyedValue<TKey>>
        extends AbstractList<TValue> implements List<TValue>, Lookup<TKey, TValue> {

    private final List<TValue> values;

    KeyedCollection() {
        values = new ArrayList<>();
    }

    KeyedCollection(Collection<? extends TValue> other) {
        values = new ArrayList<>(other);
    }

    KeyedCollection(int capacity) {
        values = new ArrayList<>(capacity);
    }

    @Override
    public boolean containsKey(TKey key) {
        for (TValue v : values) {
            if (v.getKey().compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(TValue value) {
        return values.contains(value);
    }

    @Override
    public Iterable<Entry<TKey, TValue>> entries() {
        final Iterator<TValue> it = values.iterator();
        return new Iterable<Entry<TKey, TValue>>() {
            @Override
            public Iterator<Entry<TKey, TValue>> iterator() {
                return new MappingIterator<>(it, new Function1<TValue, Entry<TKey, TValue>>() {
                    @Override
                    public Entry<TKey, TValue> apply(TValue a1) {
                        return new ImmutableEntry(a1.getKey(), a1);
                    }
                });
            }
        };
    }

    @Override
    public TValue get(int index) {
        return values.get(index);
    }

    @Override
    public Iterable<TValue> get(TKey key) {
        List<TValue> result = new ArrayList<>();
        for (TValue v : values) {
            if (v.getKey().compareTo(key) == 0) {
                result.add(v);
            }
        }
        return result;
    }

    @Override
    public TValue getFirst(TKey key) {
        Iterator<TValue> it = get(key).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Value
    public static class ImmutableEntry<TKey, TValue> implements Entry<TKey, TValue> {

        TKey key;
        TValue value;

        @Override
        public TValue setValue(TValue value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
