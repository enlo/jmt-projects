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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.iteration.IterationUtils.getFirst;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 * @param <K>
 * @param <V>
 */
@Slf4j
@ThreadSafe
public abstract class AbstractLRUCache<K, V> {

    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;
    private int putCount;
    private int size;
    protected final LinkedHashMap<K, V> map;
    protected int maxSize;
    protected final ReadWriteLock rwl = new ReentrantReadWriteLock();
    protected final Lock rl = rwl.writeLock();
    protected final Lock wl = rwl.readLock();

    public AbstractLRUCache(int maxSize) {
        Arguments.nonNegative(maxSize, "maxSize");
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<>(16, 0.75f, true);
    }

    public AbstractLRUCache(float maxSizeFactor) {
        this((int) (Runtime.getRuntime().maxMemory()
                    * Arguments.between(maxSizeFactor, 0f, 0.5f, "maxSizeFactor")));
    }

    public V get(@NonNull K key) {
        return getOnly(key);
    }

    public int getCreateCount() {
        rl.lock();
        try {
            return createCount;
        }
        finally {
            rl.unlock();
        }
    }

    public int getEvictionCount() {
        rl.lock();
        try {
            return evictionCount;
        }
        finally {
            rl.unlock();
        }
    }

    public int getHitCount() {
        rl.lock();
        try {
            return hitCount;
        }
        finally {
            rl.unlock();
        }
    }

    public int getMaxSize() {
        rl.lock();
        try {
            return maxSize;
        }
        finally {
            rl.unlock();
        }
    }

    public int getMissCount() {
        rl.lock();
        try {
            return missCount;
        }
        finally {
            rl.unlock();
        }
    }

    public V getOrCreate(@NonNull K key) {
        return getOrCreate(key, null);
    }

    public int getPutCount() {
        rl.lock();
        try {
            return putCount;
        }
        finally {
            rl.unlock();
        }
    }

    public int getSize() {
        rl.lock();
        try {
            return size;
        }
        finally {
            rl.unlock();
        }
    }

    public final void setMaxSize(int maxSize) {
        Arguments.nonNegative(maxSize, "maxSize");
        wl.lock();
        try {
            this.maxSize = maxSize;
            trimToSize(wl, maxSize);
            logger.info("current stats is {}", currentStats());
        }
        finally {
            wl.unlock();
        }
    }

    public final void setMaxSizeFactor(float maxSizeFactor) {
        Arguments.between(maxSizeFactor, 0f, 0.5f, "maxSizeFactor");
        setMaxSize((int) (Runtime.getRuntime().maxMemory() * maxSizeFactor));
    }

    @Override
    public String toString() {
        rl.lock();
        try {
            return currentStats();
        }
        finally {
            rl.unlock();
        }
    }

    private String currentStats() {
        int accesses = hitCount + missCount;
        int hitPercent = accesses != 0 ? (int) (100f * hitCount / accesses) : 0;
        int usagePercent = maxSize != 0 ? (int) (100f * size / maxSize) : 0;
        return String.format("%s[entries=%d, size=%d, maxSize=%d, usage=%s%% hits=%d, misses=%d, hitRate=%d%%]",
                             getClass().getSimpleName(), map.size(), size,
                             maxSize, usagePercent, hitCount, missCount, hitPercent);
    }

    private void trimToSize(Lock lock, int targetSize) {
        while (targetSize <= size && !map.isEmpty()) {
            // accessOrder が true なので、一番古いエントリが選出される.
            Entry<K, V> toEvict = getFirst(map.entrySet());
            if (toEvict == null) {
                break;
            }
            K key = toEvict.getKey();
            V value = toEvict.getValue();
            map.remove(key);
            size -= computeSize(key, value);
            evictionCount++;
            lock.unlock();
            try {
                onEntryEvicted(key, value);
            }
            finally {
                lock.lock();
            }
        }
        if (map.isEmpty()) {
            size = 0;
        }
    }

    @Nonnegative
    protected int computeSize(K key, V value) {
        return 1;
    }

    protected final V createAndPut(@NonNull K key, V hint) {
        V value = createInstance(key, hint);
        wl.lock();
        try {
            if (value != null) {
                createCount++;
                size += computeSize(key, value);
                map.put(key, value);
                trimToSize(wl, maxSize);
            }
            return value;
        }
        finally {
            wl.unlock();
        }
    }

    protected abstract V createInstance(K key, V hint);

    protected V fallBack(Lock lock, K key) {
        return null;
    }

    protected final V getOnly(@NonNull K key) {
        wl.lock();
        V value;
        try {
            // accessOrder が true なので、get 時はロック必須.
            value = map.get(key);
            if (value != null) {
                hitCount++;
                return value;
            }
            else {
                missCount++;
            }
        }
        finally {
            wl.unlock();
        }
        return fallBack(wl, key);
    }

    protected final V getOrCreate(@NonNull K key, V hint) {
        V value = getOnly(key);
        if (value != null) {
            return value;
        }
        return createAndPut(key, hint);
    }

    protected void onEntryEvicted(K key, V value) {
    }

}
