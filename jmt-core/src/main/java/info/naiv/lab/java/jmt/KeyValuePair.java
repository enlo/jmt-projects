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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author enlo
 * @param <TKey>
 * @param <TValue>
 */
@Getter
@EqualsAndHashCode
public class KeyValuePair<TKey, TValue> implements Map.Entry<TKey, TValue>, Serializable {

    private static final long serialVersionUID = 1L;

    public static <TKey extends Comparable> Comparator<KeyValuePair<TKey, ?>> getKeyComparator() {
        return new Comparator<KeyValuePair<TKey, ?>>() {
            @Override
            public int compare(KeyValuePair<TKey, ?> o1, KeyValuePair<TKey, ?> o2) {
                return o1.key.compareTo(o2.key);
            }
        };
    }

    public static <K, V> KeyValuePair<K, V> of(K key, V value) {
        return new KeyValuePair<>(key, value);
    }

    private final TKey key;
    private TValue value;

    protected KeyValuePair(@NonNull TKey key, TValue value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public TValue setValue(TValue value) {
        TValue oldVal = this.value;
        this.value = value;
        return oldVal;
    }

}
