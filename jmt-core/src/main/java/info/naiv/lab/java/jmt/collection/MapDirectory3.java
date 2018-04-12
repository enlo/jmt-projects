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
 * @param <TKey3>
 * @param <TValue>
 */
public class MapDirectory3<TKey1, TKey2, TKey3, TValue>
        extends AbstractMapDirectory<TKey1, Directory2<TKey2, TKey3, TValue>>
        implements Directory3<TKey1, TKey2, TKey3, TValue> {

    public MapDirectory3() {
        super(new Supplier<Map>() {
            @Override
            public Map get() {
                return new HashMap<>();
            }
        });
    }

    public MapDirectory3(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy) {
        super(mapSuplier, lockStrategy);
    }

    public MapDirectory3(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy, Map<TKey1, Directory2<TKey2, TKey3, TValue>> impl) {
        super(mapSuplier, lockStrategy, impl);
    }

    public MapDirectory3(Supplier<? extends Map> mapSuplier) {
        super(mapSuplier);
    }

    public MapDirectory3(Supplier<? extends Map> mapSuplier, Map<TKey1, Directory2<TKey2, TKey3, TValue>> impl) {
        super(mapSuplier, impl);
    }

    @Override
    public boolean containsKey(TKey1 key1, TKey2 key2) {
        return super.containsKey(key1, key2);
    }

    @Override
    public boolean containsKey(TKey1 key1, TKey2 key2, TKey3 key3) {
        return super.containsKey(key1, key2, key3);
    }

    @Override
    public Directory1<TKey3, TValue> get(TKey1 key1, TKey2 key2) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.get(key2);
        }
        return null;
    }

    @Override
    public TValue get(TKey1 key1, TKey2 key2, TKey3 key3) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.get(key2, key3);
        }
        return null;
    }

    @Override
    public Directory2<TKey2, TKey3, TValue> put(TKey1 key, Directory1<TKey2, Directory1<TKey3, TValue>> value) {
        Directory2<TKey2, TKey3, TValue> newVal = new MapDirectory2<>(mapSuplier, lockStrategy, value);
        return put(key, newVal);
    }

    @Override
    public Directory1<TKey3, TValue> put(TKey1 key1, TKey2 key2, Directory1<TKey3, TValue> value) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.put(key2, value);
        }
        lock.lock();
        try {
            found = get(key1);
            if (found == null) {
                found = new MapDirectory2<>(mapSuplier, lockStrategy);
                put(key1, found);
            }
            return found.put(key2, value);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public TValue put(TKey1 key1, TKey2 key2, TKey3 key3, TValue value) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.put(key2, key3, value);
        }
        lock.lock();
        try {
            found = get(key1);
            if (found == null) {
                found = new MapDirectory2<>(mapSuplier, lockStrategy);
                put(key1, found);
            }
            return found.put(key2, key3, value);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Directory1<TKey3, TValue> remove(TKey1 key1, TKey2 key2) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.remove(key2);
        }
        return null;
    }

    @Override
    public TValue remove(TKey1 key1, TKey2 key2, TKey3 key3) {
        Directory2<TKey2, TKey3, TValue> found = get(key1);
        if (found != null) {
            return found.remove(key2, key3);
        }
        return null;
    }

    @Override
    public Directory2<TKey2, TKey3, TValue> replace(TKey1 key, Directory2<TKey2, TKey3, TValue> value) {
        return impl.put(key, value);
    }

}
