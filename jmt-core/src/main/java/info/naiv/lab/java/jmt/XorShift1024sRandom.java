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

import javax.annotation.concurrent.ThreadSafe;
import lombok.Synchronized;
import lombok.ToString;

/**
 * 1024bit XorShift.
 *
 *
 * @author enlo
 * @see <a href="http://xorshift.di.unimi.it/">XorShift</a>
 */
@ThreadSafe
@ToString
public class XorShift1024sRandom extends XorShiftRandom {

    private static final long serialVersionUID = 7300713716404967473L;

    @Override
    protected long[] createState() {
        return new long[16];
    }

    @Override
    @Synchronized
    protected long nextLongCore() {
        final long s0 = state[index];
        long s1 = state[index = (index + 1) & 15];
        s1 ^= s1 << 1;
        return (state[index] = s1 ^ s0 ^ (s1 >>> 13) ^ (s0 >>> 7)) * M8;
    }

}
