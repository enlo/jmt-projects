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

import info.naiv.lab.java.jmt.concurrent.LockStrategies;
import info.naiv.lab.java.jmt.concurrent.LockStrategy;
import info.naiv.lab.java.jmt.fx.Supplier;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <K>
 * @param <V>
 */
@EqualsAndHashCode
public abstract class AbstractMapDirectory<K, V>
        implements Lookup<K, V>, Map<K, V> {

    protected final Supplier<? extends Map> mapSuplier;
    protected final Map<K, V> impl;
    protected LockStrategy lockStrategy;
    protected Lock lock;

    public AbstractMapDirectory(@NonNull Supplier<? extends Map> mapSuplier,
                                @NonNull LockStrategy lockStrategy) {
        this.mapSuplier = mapSuplier;
        this.impl = mapSuplier.get();
        this.lockStrategy = lockStrategy;
        this.lock = lockStrategy.createLock();
    }

    public AbstractMapDirectory(@NonNull Supplier<? extends Map> mapSuplier,
                                @NonNull LockStrategy lockStrategy,
                                @NonNull Map<K, V> impl) {
        this.mapSuplier = mapSuplier;
        this.impl = impl;
        this.lockStrategy = lockStrategy;
        this.lock = lockStrategy.createLock();
    }

    public AbstractMapDirectory(@NonNull Supplier<? extends Map> mapSuplier) {
        this(mapSuplier, LockStrategies.noLock());
    }

    public AbstractMapDirectory(@NonNull Supplier<? extends Map> mapSuplier,
                                @NonNull Map<K, V> impl) {
        this(mapSuplier, LockStrategies.noLock(), impl);
    }

    @Override
    public void clear() {
        impl.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return impl.containsKey(key);
    }

    protected boolean containsKey(Object... keys) {
        int last = keys.length - 1;
        Map m = this;
        for (int i = 0; i < last; i++) {
            Object o = m.get(keys[i]);
            if (o instanceof Map) {
                m = (Map) o;
            }
            else {
                return false;
            }
        }
        return m.containsKey(keys[last]);
    }

    @Override
    public boolean containsValue(Object value) {
        return impl.containsValue(value);
    }

    public boolean containsValue(Object... values) {
        int last = values.length - 1;
        Map m = this;
        for (int i = 0; i < last; i++) {
            Object o = m.get(values[i]);
            if (o instanceof Map) {
                m = (Map) o;
            }
            else {
                return false;
            }
        }
        return m.containsValue(values[last]);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return impl.entrySet();
    }

    @Override
    public V get(Object key) {
        return impl.get(key);
    }

    @Override
    public boolean isEmpty() {
        return impl.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return impl.keySet();
    }

    @Override
    public V put(K key, V value) {
        return impl.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        impl.putAll(m);
    }

    @Override
    public V remove(Object key) {
        return impl.remove(key);
    }

    @Override
    public int size() {
        return impl.size();
    }

    @Override
    public Collection<V> values() {
        return impl.values();
    }

    public void setLockStrategy(LockStrategy lockStrategy) {
        this.lockStrategy = lockStrategy;
        this.lock = lockStrategy.createLock();
    }

}
