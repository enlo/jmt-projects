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

import static info.naiv.lab.java.jmt.Misc.newArrayList;
import info.naiv.lab.java.jmt.iteration.IterationUtils;
import info.naiv.lab.java.jmt.monad.Iteratee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
public class ImmutableMutiValueLookup<TKey extends Comparable, TValue>
        implements MultiValueLookup<TKey, TValue>, Iterable<Grouping<TKey, TValue>> {

    /**
     *
     * @param <K>
     * @param <V>
     * @param entries
     * @return
     */
    @Nonnull
    public static <K extends Comparable, V>
            MultiValueLookup<K, V> fromEntries(@Nonnull Collection<Entry<K, V>> entries) {
        GroupBuilder gb = new GroupBuilder(entries.size());
        for (Entry<K, V> e : entries) {
            gb.add(e.getKey(), e.getValue());
        }
        return new ImmutableMutiValueLookup<>(gb.build());
    }

    /**
     *
     * @param <K>
     * @param entries
     * @return
     */
    @Nonnull
    public static <K extends Comparable>
            MultiValueLookup<K, KeyedValue<K>> fromKeyedValues(@Nonnull Collection<? extends KeyedValue<K>> entries) {
        GroupBuilder gb = new GroupBuilder(entries.size());
        for (KeyedValue<K> e : entries) {
            gb.add(e.getKey(), e);
        }
        return new ImmutableMutiValueLookup<>(gb.build());
    }

    private final List<Grouping<TKey, TValue>> values;

    private ImmutableMutiValueLookup(@NonNull List<Grouping<TKey, TValue>> values) {
        this.values = values;
    }

    @Override
    public boolean containsKey(@Nonnull TKey key) {
        for (Grouping<TKey, TValue> g : values) {
            if (g.getKey().compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public Iterable<TValue> get(@Nonnull TKey key) {
        for (Grouping<TKey, TValue> g : values) {
            if (g.getKey().compareTo(key) == 0) {
                return g;
            }
        }
        return Iteratee.<TValue>empty();
    }

    @Override
    public TValue getFirst(@Nonnull TKey key) {
        return IterationUtils.getFirst(get(key));
    }

    @Override
    public Iterator<Grouping<TKey, TValue>> iterator() {
        return values.iterator();
    }

    /**
     * 要素数を取得する.
     *
     * @return
     */
    public int size() {
        return values.size();
    }

    /**
     *
     * @param <TKey>
     * @param <TValue>
     */
    public static class GroupBuilder<TKey, TValue> {

        final Map<TKey, List<TValue>> values;

        /**
         *
         * @param capacity
         */
        public GroupBuilder(int capacity) {
            values = new HashMap<>(capacity);
        }

        /**
         *
         */
        public GroupBuilder() {
            values = new HashMap<>();
        }

        /**
         *
         * @param key
         * @param value
         */
        public void add(@Nonnull TKey key, TValue value) {
            if (values.containsKey(key)) {
                values.get(key).add(value);
            }
            else {
                values.put(key, newArrayList(value));
            }
        }

        /**
         *
         * @return
         */
        @Nonnull
        public List<Grouping<TKey, TValue>> build() {
            List<Grouping<TKey, TValue>> result = new ArrayList<>(values.size());
            for (Entry<TKey, List<TValue>> kv : values.entrySet()) {
                result.add(new ImmutableGrouping<>(kv.getKey(), kv.getValue()));
            }
            return result;
        }

        /**
         *
         * @param key
         * @return
         */
        @Nonnull
        public Grouping<TKey, TValue> get(@Nonnull TKey key) {
            List<TValue> list = values.get(key);
            return new ImmutableGrouping<>(key, list);
        }
    }
}
