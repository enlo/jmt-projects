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

import info.naiv.lab.java.jmt.Arguments;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * @author enlo
 * @param <K>
 * @param <V>
 */
@EqualsAndHashCode(callSuper = true)
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 7637972897340195829L;

    protected static final int MAX_INITIAL_CAPACITY = 1000;
    protected static final int DEFAULT_LIMIT_SIZE = 100;
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @Getter
    protected int maxCapacity;

    public LRUHashMap() {
        this(DEFAULT_LIMIT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public LRUHashMap(int maxCapcity) {
        this(maxCapcity, DEFAULT_LOAD_FACTOR);
    }

    public LRUHashMap(int maxCapcity, float loadFactor) {
        super(Math.min(maxCapcity, MAX_INITIAL_CAPACITY), loadFactor, true);
        this.maxCapacity = maxCapcity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return maxCapacity <= size();
    }

    public void setMaxCapacity(int maxCapacity) {
        Arguments.nonMinus(maxCapacity, "maxCapacity");
        this.maxCapacity = maxCapacity;
    }

}
