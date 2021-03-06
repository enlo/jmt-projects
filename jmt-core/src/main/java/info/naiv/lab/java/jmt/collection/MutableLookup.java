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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
public class MutableLookup<TKey, TValue> implements Lookup<TKey, TValue>, Iterable<Map.Entry<TKey, TValue>> {

    final Map<TKey, TValue> map;

    /**
     *
     * @param map
     */
    public MutableLookup(@NonNull Map<TKey, TValue> map) {
        this.map = map;
    }

    /**
     *
     */
    public MutableLookup() {
        this(new HashMap<TKey, TValue>());
    }

    @Override
    public boolean containsKey(TKey key) {
        return map.containsKey(key);
    }

    @Override
    public TValue get(TKey key) {
        return map.get(key);
    }

    @Override
    public Iterator<Map.Entry<TKey, TValue>> iterator() {
        return map.entrySet().iterator();
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    @Nonnull
    public MutableLookup<TKey, TValue> put(TKey key, TValue value) {
        map.put(key, value);
        return this;
    }

    /**
     * 要素数を取得する.
     *
     * @return
     */
    public int size() {
        return map.size();
    }

}
