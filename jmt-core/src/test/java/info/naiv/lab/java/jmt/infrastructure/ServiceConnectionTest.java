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
package info.naiv.lab.java.jmt.infrastructure;

import static info.naiv.lab.java.jmt.Misc.repeat;
import static info.naiv.lab.java.jmt.Misc.toList;
import java.util.List;
import java.util.concurrent.*;
import lombok.Value;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author enlo
 */
public class ServiceConnectionTest {

    public ServiceConnectionTest() {
    }

    protected ServiceContainer container;

    @Before
    public void initContainer() {
        container = ServiceProviders.newServiceContainer();
    }

    @Value
    protected static class TestObj {

        int value;
    }

    /**
     * Test of close method, of class AbstractServiceConnection.
     */
    @Test
    public void testClose() {
        System.out.println("close");

        ServiceConnection instance = container.registerService(new TestObj(1));
        TestObj obj = container.resolveService(TestObj.class);
        assertThat("pre-close", obj, is(not(nullValue())));
        assertThat("pre-close", obj.value, is(1));
        assertThat("pre-close", instance.isClosed(), is(false));

        instance.close();

        obj = container.resolveService(TestObj.class);
        assertThat("closed", obj, is(nullValue()));
        assertThat("closed", instance.isClosed(), is(true));
    }

    /**
     * Test of getContainer method, of class AbstractServiceConnection.
     */
    @Test
    public void testGetContainer() {
        ServiceConnection instance = container.registerService(new TestObj(1));
        assertThat(instance.getContainer(), is(sameInstance(container)));
    }

    /**
     * Test of getContainer method, of class AbstractServiceConnection.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetContainer_2() {
        ServiceConnection instance = container.registerService(new TestObj(1));
        container = null;
        System.gc();
        instance.getContainer();
    }

    /**
     * Test of getContainer method, of class AbstractServiceConnection.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetContainer_3() {
        ServiceConnection instance = container.registerService(new TestObj(1));
        instance.close();
        instance.getContainer();
    }

    /**
     * Test of getPriority method, of class AbstractServiceConnection.
     */
    @Test
    public void testGetPriority() {
        ServiceConnection instance = container.registerService(new TestObj(1));
        assertThat(instance.getPriority(), is(1));

        instance = container.registerService(1, new TestObj(10));
        assertThat(instance.getPriority(), is(1));

        instance = container.registerService(2, new TestObj(100));
        assertThat(instance.getPriority(), is(2));
    }

    /**
     * Test of getTag method, of class AbstractServiceConnection.
     */
    @Test
    public void testGetTag() {
        ServiceConnection instance = container.registerService(new TestObj(1));
        assertThat(instance.getTag(), is(Tag.NONE));

        instance = container.registerService(1, new TestObj(10), Tag.ANY);
        assertThat(instance.getTag(), is(Tag.ANY));

        instance = container.registerService(new TestObj(100), Tag.of(100));
        assertThat(instance.getTag(), is(Tag.of(100)));
    }

    /**
     * Test of isClosed method, of class AbstractServiceConnection.
     */
    @Test
    public void testIsClosed() {
        System.out.println("close");

        ServiceConnection instance = container.registerService(new TestObj(1));
        assertThat("pre-close", instance.isClosed(), is(false));

        container = null;
        System.gc();

        assertThat("closed", instance.isClosed(), is(true));
    }

    /**
     * Test of isClosed method, of class AbstractServiceConnection.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testIsClosed_2() throws Exception {
        System.out.println("close");

        ServiceConnection instance = container.registerService(new TestObj(1));
        assertThat("pre-close", instance.isClosed(), is(false));

        container.close();

        assertThat("closed", instance.isClosed(), is(true));
    }

    @Test
    public void testClose_MT() throws InterruptedException, ExecutionException {
        int threadCount = 10;
        int repeatCount = threadCount * 3;
        final Object lock = new Object();
        final ServiceConnection instance = container.registerService(new TestObj(1));
        ExecutorService e = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> list = e.invokeAll(toList(repeat(repeatCount, new Callable<Integer>() {
            @Override
            public Integer call() {
                int r = 0;
                synchronized (lock) {
                    if (!instance.isClosed()) {
                        instance.getContainer();
                        r = 1;
                    }
                }
                instance.close();
                return r;
            }
        })));
        int i = 0;
        for (Future<Integer> f : list) {
            i += f.get();
        }
        assertThat(i, is(greaterThanOrEqualTo(1)));
        assertThat(i, is(lessThan(threadCount)));
    }
}
