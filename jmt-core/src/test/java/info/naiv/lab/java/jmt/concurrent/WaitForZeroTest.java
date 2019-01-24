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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Value;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class WaitForZeroTest {

    /**
     *
     */
    public WaitForZeroTest() {
    }

    /**
     * Test of countDown method, of class WaitForZero.
     */
    @Test
    public void testCountDown() {
        WaitForZero w4z = new WaitForZero();
        w4z.count.register();
        w4z.countDown();
        assertThat(w4z.await(), is(true));
        assertThat(w4z.count.getUnarrivedParties(), is(1));
    }

    /**
     * Test of countUp method, of class WaitForZero.
     */
    @Test
    public void testCountUp() {
        WaitForZero w4z = new WaitForZero();
        w4z.countUp();
        w4z.count.arriveAndDeregister();
        assertThat(w4z.await(), is(true));
        assertThat(w4z.count.getUnarrivedParties(), is(1));
    }

    /**
     *
     */
    @Test
    public void testCtor() {
        WaitForZero w4z = new WaitForZero();
        assertThat(w4z.await(), is(true));
        assertThat(w4z.count.getUnarrivedParties(), is(1));
    }

    @Test
    public void testDoAwait() throws Exception {

        final List<Integer> numbers = new CopyOnWriteArrayList<>();
        final WaitForZero value = new WaitForZero(5);
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(new TestRunnable(1, numbers, value));
        pool.submit(new TestRunnable(2, numbers, value));
        pool.submit(new TestRunnable(3, numbers, value));
        pool.submit(new TestRunnable(4, numbers, value));
        pool.submit(new TestRunnable(5, numbers, value));
        assertThat(value.await(), is(true));
        numbers.add(6);
        assertThat(numbers, hasSize(6));
        assertThat(numbers, containsInAnyOrder(1, 2, 3, 4, 5, 6));
        assertThat(numbers.get(5), is(6));
    }

    /**
     * Test of doAwait method, of class WaitForZero.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDoAwait_0args() throws Exception {
        final AtomicInteger x = new AtomicInteger(0);
        final WaitForZero value = new WaitForZero();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                x.incrementAndGet();
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ex) {
                }
                value.countDown();
            }
        };
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 4; i++) {
            value.countUp();
            assertThat("Post count up", value.count.getUnarrivedParties(), is(2 + i));
            pool.submit(task);
        }
        assertThat(x.get(), is(0));
        assertThat(value.doAwait(), is(true));
        assertThat("getUnarrivedParties", value.count.getUnarrivedParties(), is(1));
        assertThat(x.get(), is(4));
        // もう一回カウントダウン.
        assertThat(value.doAwait(), is(true));
        assertThat("getUnarrivedParties", value.count.getUnarrivedParties(), is(1));
    }

    /**
     * Test of doAwait method, of class WaitForZero.
     *
     * @throws java.lang.Exception
     */
    @Test(expected = TimeoutException.class)
    public void testDoAwait_long_TimeUnit() throws Exception {
        final AtomicInteger x = new AtomicInteger(0);
        final WaitForZero value = new WaitForZero();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(320);
                }
                catch (InterruptedException ex) {
                }
                x.getAndIncrement();
                value.countDown();
            }
        };
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 4; i++) {
            value.countUp();
            pool.submit(task);
        }

        value.doAwait(160, TimeUnit.MILLISECONDS);
        assertThat("getUnarrivedParties", value.count.getUnarrivedParties(), is(1));
    }

    /**
     * Test of doAwait method, of class WaitForZero.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDoAwait_long_TimeUnit_noTimeout() throws Exception {
        final AtomicInteger x = new AtomicInteger(0);
        final WaitForZero value = new WaitForZero();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                }
                catch (InterruptedException ex) {
                }
                x.getAndIncrement();
                value.countDown();
            }
        };
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 4; i++) {
            value.countUp();
            pool.submit(task);
        }

        assertThat(x.get(), is(0));
        assertThat(value.doAwait(200, TimeUnit.MILLISECONDS), is(true));
        assertThat("getUnarrivedParties", value.count.getUnarrivedParties(), is(1));
        assertThat(x.get(), is(4));
    }

    @Value
    static class TestRunnable implements Runnable {

        int n;
        List<Integer> numbers;
        WaitForZero w4z;

        @Override
        public void run() {
            try {
                Thread.sleep(100);
                numbers.add(n);
            }
            catch (InterruptedException ex) {
            }
            w4z.countDown();
        }
    }
}
