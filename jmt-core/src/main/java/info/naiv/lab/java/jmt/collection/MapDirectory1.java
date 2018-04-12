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
 * @param <TKey>
 * @param <TValue>
 */
public class MapDirectory1<TKey, TValue>
        extends AbstractMapDirectory<TKey, TValue>
        implements Directory1<TKey, TValue> {

    public MapDirectory1() {
        super(new Supplier<Map>() {
            @Override
            public Map get() {
                return new HashMap<>();
            }
        });
    }

    public MapDirectory1(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy) {
        super(mapSuplier, lockStrategy);
    }

    public MapDirectory1(Supplier<? extends Map> mapSuplier, LockStrategy lockStrategy, Map<TKey, TValue> impl) {
        super(mapSuplier, lockStrategy, impl);
    }

    public MapDirectory1(Supplier<? extends Map> mapSuplier) {
        super(mapSuplier);
    }

    public MapDirectory1(Supplier<? extends Map> mapSuplier, Map<TKey, TValue> impl) {
        super(mapSuplier, impl);
    }

}
