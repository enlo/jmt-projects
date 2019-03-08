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

import java.util.Iterator;
import lombok.Value;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
@Value
public class KeyedValueLookup<TKey extends Comparable, TValue extends KeyedValue<TKey>> implements Lookup<TKey, TValue>, Iterable<TValue> {

    public static <TKey extends Comparable, TValue extends KeyedValue<TKey>> TValue lookup(TValue[] values, TKey key) {
        for (TValue v : values) {
            if (v.getKey().compareTo(key) == 0) {
                return v;
            }
        }
        return null;
    }

    public static <TKey extends Comparable, TValue extends KeyedValue<TKey>> TValue lookup(Iterable<TValue> values, TKey key) {
        for (TValue v : values) {
            if (v.getKey().compareTo(key) == 0) {
                return v;
            }
        }
        return null;
    }

    Iterable<TValue> values;

    @Override
    public boolean containsKey(TKey key) {
        return get(key) != null;
    }

    @Override
    public TValue get(TKey key) {
        return lookup(values, key);
    }

    @Override
    public Iterator<TValue> iterator() {
        return values.iterator();
    }
}