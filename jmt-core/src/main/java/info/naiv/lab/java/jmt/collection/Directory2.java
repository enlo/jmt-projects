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

import java.util.Map;

/**
 *
 * @author enlo
 * @param <TKey1>
 * @param <TKey2>
 * @param <TValue>
 */
public interface Directory2<TKey1, TKey2, TValue>
        extends Lookup<TKey1, Directory1<TKey2, TValue>>,
                Map<TKey1, Directory1<TKey2, TValue>> {

    TValue get(TKey1 key1, TKey2 key2);

    TValue put(TKey1 key1, TKey2 key2, TValue value);

    boolean containsKey(TKey1 key1, TKey2 key2);

    TValue remove(TKey1 key1, TKey2 key2);
    
    Directory1<TKey2, TValue> replace(TKey1 key, Directory1<TKey2, TValue> value);
}
