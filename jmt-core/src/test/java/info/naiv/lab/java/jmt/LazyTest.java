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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.fx.Supplier;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class LazyTest {

    public LazyTest() {
    }

    /**
     * Test of equals method, of class Lazy.
     */
    @Test
    public void testEquals() {
        Lazy<Integer> lhs = Lazy.of(StandardFunctions.constantSuplier(1));
        Lazy<Integer> rhs1 = new Lazy(1);
        Lazy<Integer> rhs2 = new Lazy(2);
        assertThat(lhs, is(rhs1));
        assertThat(lhs, is(not(rhs2)));
    }

    /**
     * Test of get method, of class Lazy.
     */
    @Test
    public void testGet() {
        Supplier<Integer> sp = StandardFunctions.counter(1);
        Lazy<Integer> lv1 = Lazy.of(sp);
        Lazy<Integer> lv2 = Lazy.of(sp);
        assertThat(lv1.get(), is(1));
        assertThat(lv1.get(), is(1));
        assertThat(lv2.get(), is(2));
        assertThat(lv2.get(), is(2));
    }

    /**
     * Test of hashCode method, of class Lazy.
     */
    @Test
    public void testHashCode() {
        Lazy<Integer> lhs = Lazy.of(StandardFunctions.constantSuplier(1));
        Lazy<Integer> rhs1 = new Lazy(1);
        Lazy<Integer> rhs2 = new Lazy(2);
        assertThat(lhs.hashCode(), is(rhs1.hashCode()));
        assertThat(lhs.hashCode(), is(not(rhs2.hashCode())));

    }

    /**
     * Test of isInitialized method, of class Lazy.
     */
    @Test
    public void testIsInitialized() {
        Lazy<Integer> lv = Lazy.of(StandardFunctions.counter(1));
        lv.get();
        assertThat(lv.isInitialized(), is(true));
    }

    @Test
    public void testMultiThread() throws InterruptedException, ExecutionException {
        ExecutorService ex = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 1000; i++) {
            final Lazy<Integer> lv = Lazy.of(StandardFunctions.counter(1));
            Callable<Integer> c = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return lv.get();
                }
            };
            Callable<Integer>[] arr = new Callable[10];
            Arrays.fill(arr, c);
            List<Future<Integer>> res = ex.invokeAll(asList(arr));
            for (Future<Integer> f : res) {
                assertThat("" + i, f.get(), is(1));
            }
        }
    }

    /**
     * Test of of method, of class Lazy.
     */
    @Test
    public void testOf() {
        Lazy<Integer> lv = Lazy.of(StandardFunctions.counter(1));
        assertThat(lv.isInitialized(), is(false));
        assertThat(lv.rawValue(), is(nullValue()));
    }

    /**
     * Test of toString method, of class Lazy.
     */
    @Test
    public void testToString() {
        Lazy<Integer> lv1 = new Lazy<>(1);
        Lazy<Integer> lv2 = new Lazy<>(10);
        assertThat(lv1.toString(), is("1"));
        assertThat(lv2.toString(), is("10"));
    }

}
