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
package info.naiv.lab.java.jmt.text;

import info.naiv.lab.java.jmt.AbstractLRUCache;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author enlo
 */
public abstract class UnicodeVectorCache extends AbstractLRUCache<String, UnicodeVector> {

    public static final UnicodeVectorCache DECOMP = new UnicodeVectorCache() {
        @Override
        protected UnicodeVector createInstance(String key, UnicodeVector hint) {
            if (hint != null) {
                return hint.decompose();
            }
            return NORMAL.getOrCreate(key).decompose();
        }
    };

    public static final UnicodeVectorCache NORMAL = new UnicodeVectorCache() {
        @Override
        protected UnicodeVector createInstance(String key, UnicodeVector hint) {
            if (hint != null) {
                return hint;
            }
            return new UnicodeVector(key);
        }

        @Override
        protected UnicodeVector fallBack(Lock lock, String key) {
            return DECOMP.get(key);
        }

    };
    static final float INITIAL_CACHE_SIZE = 1 / 64f;

    public static UnicodeVector getDecomposed(UnicodeVector uv) {
        return DECOMP.getOrCreate(uv);
    }

    public static UnicodeVector getDecomposed(String str) {
        return DECOMP.getOrCreate(str);
    }

    public UnicodeVectorCache() {
        super(INITIAL_CACHE_SIZE);
    }

    public UnicodeVector getOrCreate(UnicodeVector uv) {
        return getOrCreate(uv.getSource(), uv);
    }

    @Override
    protected int computeSize(String key, UnicodeVector value) {
        return key.length() * 4;
    }
}
