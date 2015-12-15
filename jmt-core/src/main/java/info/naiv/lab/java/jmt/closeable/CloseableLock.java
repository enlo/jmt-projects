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
package info.naiv.lab.java.jmt.closeable;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.ImmutableHolder;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author enlo
 */
public class CloseableLock extends ImmutableHolder<Lock> implements Lock, ACS<Lock> {

    /**
     * インスタンスを生成してロックする
     *
     * @param lock ロック
     * @return CloseableLock インスタンス
     * @throws IllegalArgumentException lock が null.
     */
    @ReturnNonNull
    public static CloseableLock lock(Lock lock) throws IllegalArgumentException {
        CloseableLock lck = new CloseableLock(lock);
        lck.lock();
        return lck;
    }

    public CloseableLock(Lock lock) {
        super(nonNull(lock, "lock"), Lock.class);
    }

    @Override
    public void close() {
        getContent().unlock();
    }

    @Override
    public void lock() {
        getContent().lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        getContent().lockInterruptibly();
    }

    @Override
    public Condition newCondition() {
        return getContent().newCondition();
    }

    @Override
    public boolean tryLock() {
        return getContent().tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return getContent().tryLock(time, unit);
    }

    @Override
    public void unlock() {
        getContent().unlock();
    }

}
