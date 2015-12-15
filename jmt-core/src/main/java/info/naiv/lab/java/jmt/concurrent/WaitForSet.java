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

import info.naiv.lab.java.jmt.mark.ThreadSafety;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * スレッド間で共有する値. <br>
 * どこかのスレッドで最初の値が設定されるまで、ほかのスレッドは値取得を待つ.<br>
 * 一度初期値が設定されれば、ほかのスレッドは値を待たない.
 *
 * @author enlo
 * @param <T>
 */
@ThreadSafety
public class WaitForSet<T> extends AbstractAwaitable {

    
    private final CountDownLatch cdl = new CountDownLatch(1);
    private T value;

    public WaitForSet() {
    }

    @Override
    public boolean doAwait() throws InterruptedException {
        cdl.await();
        return true;
    }

    @Override
    public boolean doAwait(long timeout, TimeUnit unit) throws InterruptedException {
        return cdl.await(timeout, unit);
    }

    public T get() {
        await();
        return value;
    }

    public void set(T value) {
        this.value = value;
        cdl.countDown();
    }

}
