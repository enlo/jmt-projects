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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class WaitForSetTest {

    /**
     * Test of doAwait method, of class WaitForSet.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testDoAwait_0args() throws InterruptedException {
        final AtomicInteger x = new AtomicInteger(0);
        final WaitForSet<String> value = new WaitForSet<>();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ex) {
                }
                x.set(1);
                value.set("ABC");
            }
        };
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(task);

        assertThat(x.get(), is(0));
        assertThat(value.doAwait(), is(true));
        assertThat(x.get(), is(1));
        assertThat(value.get(), is("ABC"));
    }

    /**
     * Test of doAwait method, of class WaitForSet.
     */
    @Test
    public void testDoAwait_long_TimeUnit() throws InterruptedException {
        final AtomicInteger x = new AtomicInteger(0);
        final WaitForSet<String> value = new WaitForSet<>();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                }
                catch (InterruptedException ex) {
                }
                x.set(1);
                value.set("ABC");
            }
        };
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(task);

        assertThat(x.get(), is(0));
        assertThat(value.doAwait(100, TimeUnit.MILLISECONDS), is(false));
        assertThat(x.get(), is(0));
        assertThat(value.doAwait(100, TimeUnit.MILLISECONDS), is(true));
        assertThat(x.get(), is(1));
        assertThat(value.get(), is("ABC"));
    }

    /**
     * Test of get method, of class WaitForSet.
     */
    @Test
    public void testGet() {
        final WaitForSet<String> value = new WaitForSet<>();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ex) {
                }
                value.set("ABC");
            }
        };
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(task);

        assertThat(value.get(), is("ABC"));
    }

}
