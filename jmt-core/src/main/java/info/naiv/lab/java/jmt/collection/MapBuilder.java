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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;

/**
 * マップビルダー.
 *
 * @author enlo
 * @param <K> キーの型
 * @param <V> 値の型
 */
public abstract class MapBuilder<K, V> {

    /**
     * ハッシュマップの作成.
     *
     * @param <K> キーの型
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return マップビルダー
     */
    @Nonnull
    public static <K, V> MapBuilder<K, V> begin(K key, V value) {
        return new HashMapBuilder<K, V>().put(key, value);
    }

    /**
     * 並行ハッシュマップの作成.
     *
     * @param <K> キーの型
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return マップビルダー
     */
    @Nonnull
    public static <K, V> MapBuilder<K, V> concurrentHashMap(K key, V value) {
        return new ConcurrentHashMapBuilder<K, V>().put(key, value);
    }

    /**
     * ハッシュマップの作成.
     *
     * @param <K> キーの型
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return マップビルダー
     */
    @Nonnull
    public static <K, V> MapBuilder<K, V> hashMap(K key, V value) {
        return new HashMapBuilder<K, V>().put(key, value);
    }

    /**
     * ツリーマップの作成.
     *
     * @param <K> キーの型
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return マップビルダー
     */
    @Nonnull
    public static <K, V> MapBuilder<K, V> treeMap(K key, V value) {
        return new TreeMapBuilder<K, V>().put(key, value);
    }

    /**
     * ツリーマップの作成.
     *
     * @param <K> キーの型
     * @param <V> 値の型
     * @param comp キー比較用
     * @param key キー
     * @param value 値
     * @return マップビルダー
     */
    @Nonnull
    public static <K, V> MapBuilder<K, V> treeMap(Comparator<K> comp, K key, V value) {
        return new TreeMapBuilder<K, V>(comp).put(key, value);
    }

    /**
     *
     */
    protected final Map<K, V> map;

    /**
     * コンストラクタ.
     *
     * @param map 初期値.
     */
    protected MapBuilder(@Nonnull Map<K, V> map) {
        this.map = map;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public abstract Map<K, V> build();

    /**
     * マップに値を追加.
     *
     * @param key キー
     * @param value 値.
     * @return マップビルダー
     */
    @Nonnull
    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    /**
     * マップに値を追加.
     *
     * @param other 別のマップ
     * @return マップビルダー
     */
    @Nonnull
    public MapBuilder<K, V> putAll(Map<K, V> other) {
        map.putAll(other);
        return this;
    }

    /**
     *
     * @return 作成したマップ.
     */
    @Nonnull
    public Map<K, V> toMap() {
        return build();
    }

    static class ConcurrentHashMapBuilder<K, V> extends MapBuilder<K, V> {

        ConcurrentHashMapBuilder() {
            super(new ConcurrentHashMap<K, V>());
        }

        @Override
        public Map<K, V> build() {
            return new ConcurrentHashMap<>(map);
        }

    }

    static class HashMapBuilder<K, V> extends MapBuilder<K, V> {

        HashMapBuilder() {
            super(new HashMap<K, V>());
        }

        @Override
        public Map<K, V> build() {
            return new HashMap<>(map);
        }

    }

    static class TreeMapBuilder<K, V> extends MapBuilder<K, V> {

        TreeMapBuilder() {
            super(new TreeMap<K, V>());
        }

        TreeMapBuilder(Comparator<K> comparator) {
            super(new TreeMap<K, V>(comparator));
        }

        @Override
        public Map<K, V> build() {
            return new TreeMap<>((SortedMap<K, V>) map);
        }
    }
}
