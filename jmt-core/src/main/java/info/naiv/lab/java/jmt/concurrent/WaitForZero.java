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
package info.naiv.lab.java.jmt.concurrent;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author enlo
 */
public class WaitForZero extends AbstractAwaitable {

    final Phaser count;

    public WaitForZero() {
        this(0);
    }

    public WaitForZero(int initialValue) {
        this.count = new Phaser(initialValue + 1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return false;
            }
        };
    }

    public void countDown() {
        this.count.arriveAndDeregister();
    }

    public void countUp() {
        this.count.register();
    }

    @Override
    protected boolean doAwait() throws Exception {
        count.awaitAdvanceInterruptibly(count.arrive());
        return true;
    }

    @Override
    protected boolean doAwait(long timeout, TimeUnit unit) throws Exception {
        count.awaitAdvanceInterruptibly(count.arrive(), timeout, unit);
        return true;
    }

}
