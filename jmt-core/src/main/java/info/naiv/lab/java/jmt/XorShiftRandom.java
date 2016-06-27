/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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

import static info.naiv.lab.java.jmt.Arguments.nonNegative;
import static java.lang.System.currentTimeMillis;
import java.util.Random;
import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;
import lombok.Synchronized;

/**
 * XorShift Random
 *
 * @author enlo
 * @see <a href="http://xorshift.di.unimi.it/">XorShift</a>
 */
@ThreadSafe
public abstract class XorShiftRandom extends Random {

    private static final long serialVersionUID = -2899992351727546151L;

    /**
     *
     */
    protected static final long M32 = 2685821657736338717L;

    /**
     *
     */
    protected static final long M8 = 1181783497276652981L;

    /**
     *
     */
    protected int index;

    /**
     *
     */
    protected long[] state;

    /**
     * コンストラクター. 現在時刻で初期化を行う.
     */
    public XorShiftRandom() {
        this(currentTimeMillis());
    }

    /**
     * 種を指定して初期化.
     *
     * @param seed 種
     */
    public XorShiftRandom(long seed) {
        super(seed);
    }

    @Override
    public boolean nextBoolean() {
        return (nextLongCore() & 1) != 0;
    }

    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len;) {
            for (long rnd = nextLongCore(),
                    n = Math.min(len - i, Long.SIZE / Byte.SIZE);
                    n-- > 0; rnd >>= Byte.SIZE) {
                bytes[i++] = (byte) rnd;
            }
        }
    }

    @Override
    public double nextDouble() {
        return nextLongCore() / (double) (1L << 53);
    }

    /**
     * [0,n)の範囲で乱数を生成する.
     *
     * @param n 値の上限.
     * @return
     */
    public long nextLong(@Nonnegative long n) {
        n = nonNegative(n, "n");
        for (;;) {
            final long bits = nextLongCore() >>> 1;
            final long value = bits % n;
            if (bits - value + (n - 1) >= 0) {
                return value;
            }
        }
    }

    @Override    
    public synchronized void setSeed(long seed) {
        super.setSeed(seed);
        if (state == null) {
            state = createState();
        }
        for (int i = state.length; i-- != 0;) {
            state[i] = seed == 0 ? M32 : seed;
        }
        for (int i = state.length * 4; i-- != 0;) {
            nextLongCore();
        }
    }

    /**
     * 状態バッファ作成
     *
     * @return 状態バッファ.
     */
    protected abstract long[] createState();

    @Override
    protected int next(int bits) {
        return (int) (nextLongCore() >>> 64 - bits);
    }

    /**
     * XorShift 乱数生成.
     *
     * @return XorShift により生成された 64bit 乱数
     */
    protected abstract long nextLongCore();

}
