/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.closeable;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.locks.Lock;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author enlo
 */
public class CloseablesTest {

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of close method, of class Closeables.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testClose() throws Exception {
        AutoCloseable ac = mock(AutoCloseable.class);
        assertThat(Closeables.close(ac), is(nullValue()));
        verify(ac, times(1)).close();
    }

    /**
     * Test of close method, of class Closeables.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testClose_ReturnException() throws Exception {
        AutoCloseable ac = mock(AutoCloseable.class);
        doThrow(new Exception()).when(ac).close();
        assertThat(Closeables.close(ac), is(instanceOf(Exception.class)));
    }

    /**
     *
     */
    @Test
    public void testLock_Lock() {
        Lock lock = mock(Lock.class);
        CloseableLock cl = Closeables.lock(lock);
        assertThat(cl, is(notNullValue()));
        assertThat(cl.getContent(), is(lock));
        verify(lock, times(1)).lock();
    }

    /**
     *
     */
    @Test
    public void testLock_Lock_Boolean() {
        Lock lock = mock(Lock.class);
        ACS<Lock> cl = Closeables.lock(lock, true);
        assertThat(cl, is(notNullValue()));
        assertThat(cl.getContent(), is(lock));
        verify(lock, times(1)).lock();
    }

    /**
     *
     */
    @Test
    public void testLock_Lock_Boolean_2() {
        Lock lock = mock(Lock.class);
        ACS<Lock> cl = Closeables.lock(lock, false);
        assertThat(cl, is(notNullValue()));
        assertThat(cl.getContent(), is(lock));
        verify(lock, times(1)).lock();
    }

    /**
     *
     */
    @Test
    public void testLock_Lock_Boolean_3() {
        ACS<Lock> cl = Closeables.lock(null, true);
        assertThat(cl, is(instanceOf(DummyCloseable.class)));
    }

    /**
     *
     */
    @Test(expected = NullPointerException.class)
    public void testLock_Lock_Boolean_4() {
        Closeables.lock(null, false);
    }

    /**
     * Test of of method, of class Closeables.
     */
    @Test
    public void testOf() {
        ACS result = Closeables.of(null);
        assertThat(result, instanceOf(DummyCloseable.class));
    }

    /**
     * Test of of method, of class Closeables.
     */
    @Test
    public void testOf_2() {
        StringReader obj = mock(StringReader.class);
        ACS result = Closeables.of(obj);
        result.close();
        verify(obj, times(1)).close();
    }

    /**
     * Test of of method, of class Closeables.
     */
    @Test
    public void testOf_3() {
        StringReader mock = mock(StringReader.class);
        ACS result = Closeables.of(mock);
        result.close();
        verify(mock, times(1)).close();
    }

    /**
     * Test of of method, of class Closeables.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testOf_4() throws IOException {
        CloseTest mock = mock(CloseTest.class);
        ACS result = Closeables.of(mock);
        result.close();
        assertThat(result, instanceOf(DelegatingAutoCloseable.class));
        verify(mock, times(1)).close();
    }

    private static class CloseTest {

        void close() {
        }
    }

}
