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
package info.naiv.lab.java.jmt.collection;

import info.naiv.lab.java.jmt.concurrent.LockStrategy;
import info.naiv.lab.java.jmt.fx.Supplier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author enlo
 * @param <TKey1>
 * @param <TKey2>
 * @param <TValue>
 */
public class MapDirectory2<TKey1, TKey2, TValue>
        extends AbstractMapDirectory<TKey1, Directory1<TKey2, TValue>>
        implements Directory2<TKey1, TKey2, TValue> {

    public MapDirectory2() {
        super(new Supplier<Map>() {
            @Override
            public Map get() {
                return new HashMap<>();
            }
        });
    }

    public MapDirectory2(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy) {
        super(mapSuplier, lockStrategy);
    }

    public MapDirectory2(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy, Map<TKey1, Directory1<TKey2, TValue>> impl) {
        super(mapSuplier, lockStrategy, impl);
    }

    public MapDirectory2(Supplier<? extends Map> mapSuplier) {
        super(mapSuplier);
    }

    public MapDirectory2(Supplier<? extends Map> mapSuplier, Map<TKey1, Directory1<TKey2, TValue>> impl) {
        super(mapSuplier, impl);
    }

    @Override
    public boolean containsKey(TKey1 key1, TKey2 key2) {
        Directory1<TKey2, TValue> found = get(key1);
        if (found != null) {
            return found.containsKey(key2);
        }
        return false;
    }

    @Override
    public TValue get(TKey1 key1, TKey2 key2) {
        Directory1<TKey2, TValue> found = get(key1);
        if (found != null) {
            return found.get(key2);
        }
        return null;
    }

    @Override
    public TValue put(TKey1 key1, TKey2 key2, TValue value) {
        Directory1<TKey2, TValue> found = get(key1);
        if (found != null) {
            return found.put(key2, value);
        }
        lock.lock();
        try {
            found = get(key1);
            if (found == null) {
                found = new MapDirectory1<>(mapSuplier, lockStrategy);
                super.put(key1, found);
            }
            return found.put(key2, value);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public TValue remove(TKey1 key1, TKey2 key2) {
        Directory1<TKey2, TValue> found = get(key1);
        if (found != null) {
            return found.remove(key2);
        }
        return null;
    }

    @Override
    public Directory1<TKey2, TValue> put(TKey1 key, Directory1<TKey2, TValue> value) {
        Directory1<TKey2, TValue> found = get(key);
        if (found != null) {
            found.putAll(value);
            return found;
        }
        lock.lock();
        try {
            found = get(key);
            if (found == null) {
                found = new MapDirectory1<>(mapSuplier, lockStrategy);
                super.put(key, found);
            }
            found.putAll(value);
            return found;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends TKey1, ? extends Directory1<TKey2, TValue>> m) {
        for (Entry<? extends TKey1, ? extends Directory1<TKey2, TValue>> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Directory1<TKey2, TValue> replace(TKey1 key, Directory1<TKey2, TValue> value) {
        return impl.put(key, value);
    }

}
