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

import info.naiv.lab.java.jmt.mark.ThreadSafety;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.ToString;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
@ThreadSafety
@ToString
public abstract class SingletonMapSupport<TKey, TValue> {

    final ConcurrentMap<TKey, TValue> map;

    /**
     *
     */
    public SingletonMapSupport() {
        this(new ConcurrentHashMap<TKey, TValue>());
    }

    /**
     *
     * @param map
     */
    public SingletonMapSupport(ConcurrentMap<TKey, TValue> map) {
        assert map != null;
        this.map = map;
    }

    /**
     *
     * @param key
     * @return
     */
    protected TValue internalGet(TKey key) {
        TValue value = map.get(key);
        if (value == null) {
            TValue newValue = newValue(key);
            value = map.putIfAbsent(key, newValue);
            if (value == null) {
                value = newValue;
            }
        }
        return value;
    }

    /**
     *
     * @param key
     * @return
     */
    protected abstract TValue newValue(TKey key);
}
