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

import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class AbstractAwaitableTest {

    /**
     *
     */
    public AbstractAwaitableTest() {
    }

    /**
     * Test of await method, of class AbstractAwaitable.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAwait_0args() throws Exception {
        AbstractAwaitable m1 = mock(AbstractAwaitable.class);
        AbstractAwaitable m2 = mock(AbstractAwaitable.class);
        AbstractAwaitable m3 = mock(AbstractAwaitable.class);

        Exception e = new Exception("ABC");

        when(m1.doAwait()).thenReturn(Boolean.TRUE);
        when(m2.doAwait()).thenReturn(Boolean.FALSE);
        when(m3.doAwait()).thenThrow(e);

        assertThat(m1.await(), is(true));
        assertThat(m1.lastException(), is(nullValue()));
        assertThat(m2.await(), is(false));
        assertThat(m2.lastException(), is(nullValue()));
        assertThat(m3.await(), is(false));
        assertThat(m3.lastException(), is(e));

        verify(m1).doAwait();
        verify(m2).doAwait();
        verify(m3).doAwait();
    }

    /**
     * Test of await method, of class AbstractAwaitable.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAwait_long_TimeUnit() throws Exception {
        AbstractAwaitable m1 = mock(AbstractAwaitable.class);
        AbstractAwaitable m2 = mock(AbstractAwaitable.class);
        AbstractAwaitable m3 = mock(AbstractAwaitable.class);

        long x = 10;
        long y = 20;
        long z = 30;
        TimeUnit u = TimeUnit.SECONDS;
        Exception e = new Exception("ABC");

        when(m1.doAwait(x, u)).thenReturn(Boolean.TRUE);
        when(m1.doAwait(z, u)).thenThrow(e);
        when(m1.doAwait(y, u)).thenReturn(Boolean.FALSE);

        assertThat(m1.await(x, u), is(true));
        assertThat(m1.lastException(), is(nullValue()));
        assertThat(m1.await(z, u), is(false));
        assertThat(m1.lastException(), is(e));
        assertThat(m1.await(y, u), is(false));
        assertThat(m1.lastException(), is(nullValue()));

        verify(m1).doAwait(x, u);
        verify(m1).doAwait(y, u);
        verify(m1).doAwait(z, u);
    }

    /**
     * Test of lastException method, of class AbstractAwaitable.
     */
    @Test
    public void testLastException() {
        AbstractAwaitable m1 = mock(AbstractAwaitable.class);
        assertThat(m1.lastException(), is(nullValue()));
    }

}
