/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.closeable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author enlo
 */
public class CloseableLockTest {

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    @Mock(name = "lockmock")
    Lock lock;

    /**
     *
     */
    public CloseableLockTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of close method, of class CloseableLock.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testClose() throws Exception {
        CloseableLock instance = new CloseableLock(lock);
        instance.close();
        verify(lock, times(1)).unlock();
    }

    /**
     *
     */
    @Test()
    public void testCtor() {
        CloseableLock lck = new CloseableLock(lock);
        assertThat(lck.getContent(), is(sameInstance(lock)));
    }

    /**
     * Test of lock method, of class CloseableLock.
     */
    @Test
    public void testLock() {
        CloseableLock instance = new CloseableLock(lock);
        instance.lock();
        verify(lock, times(1)).lock();
    }

    /**
     * Test of lockInterruptibly method, of class CloseableLock.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLockInterruptibly() throws Exception {
        CloseableLock instance = new CloseableLock(lock);
        instance.lockInterruptibly();
        verify(lock, times(1)).lockInterruptibly();
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLock_NullArg() {
        CloseableLock.lock(null);
    }

    /**
     * Test of newCondition method, of class CloseableLock.
     */
    @Test
    public void testNewCondition() {

        Condition mockCond = mock(Condition.class);
        when(lock.newCondition()).thenReturn(mockCond);

        CloseableLock instance = new CloseableLock(lock);
        assertThat(instance.newCondition(), is(sameInstance(mockCond)));
    }

    /**
     *
     */
    @Test
    public void testStaticLock() {
        CloseableLock lck = CloseableLock.lock(lock);
        assertThat(lck, is(notNullValue()));
        verify(lock, times(1)).lock();
    }

    /**
     * Test of tryLock method, of class CloseableLock.
     */
    @Test
    public void testTryLock_0args() {
        CloseableLock instance = new CloseableLock(lock);
        when(lock.tryLock()).thenReturn(false);
        assertThat(instance.tryLock(), is(false));
        verify(lock, times(1)).tryLock();

        when(lock.tryLock()).thenReturn(true);
        assertThat(instance.tryLock(), is(true));
    }

    /**
     * Test of tryLock method, of class CloseableLock.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testTryLock_long_TimeUnit() throws Exception {
        {
            long time = 123L;
            TimeUnit unit = TimeUnit.MILLISECONDS;
            CloseableLock instance = new CloseableLock(lock);
            when(lock.tryLock(time, unit)).thenReturn(false);
            assertThat(instance.tryLock(time, unit), is(false));
            verify(lock, times(1)).tryLock(time, unit);
        }
        {
            long time = 456L;
            TimeUnit unit = TimeUnit.DAYS;
            CloseableLock instance = new CloseableLock(lock);
            when(lock.tryLock(time, unit)).thenReturn(true);
            assertThat(instance.tryLock(time, unit), is(true));
            verify(lock).tryLock(time, unit);
        }
    }

    /**
     * Test of unlock method, of class CloseableLock.
     */
    @Test
    public void testUnlock() {
        CloseableLock instance = new CloseableLock(lock);
        instance.unlock();
        verify(lock, times(1)).unlock();
    }

}
