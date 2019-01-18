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
package info.naiv.lab.java.jmt.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class LockStrategies {

    public static class NoLock implements Lock {

        @Override
        public void lock() {
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
        }
    }

    public static final Lock NOLOCK = new NoLock();

    public static final ReadWriteLock NORWLOCK = new ReadWriteLock() {
        @Override
        public Lock readLock() {
            return NOLOCK;
        }

        @Override
        public Lock writeLock() {
            return NOLOCK;
        }
    };

    public static class NoLockStrategy implements LockStrategy {

        @Override
        public Lock createLock() {
            return NOLOCK;
        }

        @Override
        public ReadWriteLock createReadWriteLock() {
            return NORWLOCK;
        }
    }

    public static class ReentrantLockStrategy implements LockStrategy {

        @Override
        public Lock createLock() {
            return new ReentrantLock();
        }

        @Override
        public ReadWriteLock createReadWriteLock() {
            return new ReentrantReadWriteLock();
        }
    }

    @Nonnull
    public static LockStrategy noLock() {
        return new NoLockStrategy();
    }

    public static LockStrategy reentrantLock() {
        return new ReentrantLockStrategy();
    }
}
