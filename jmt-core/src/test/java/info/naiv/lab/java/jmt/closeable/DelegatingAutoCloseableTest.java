/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.closeable;

import info.naiv.lab.java.jmt.fx.Consumer1;
import static org.hamcrest.Matchers.is;
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
import org.mockito.MockitoAnnotations;

/**
 *
 * @author enlo
 */
public class DelegatingAutoCloseableTest {

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

    @Mock
    Test1 test1;

    /**
     *
     */
    public DelegatingAutoCloseableTest() {
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
     * Test of close method, of class DynamicAutoCloseable.
     */
    @Test
    public void testClose() {
        DelegatingAutoCloseable<?> instance = new DelegatingAutoCloseable<>(test1);
        instance.close();
        verify(test1, times(1)).close();
    }

    /**
     * Test of close method, of class DynamicAutoCloseable.
     */
    @Test
    public void testClose_Delegate() {
        Consumer1<Test1> mock = mock(Consumer1.class);
        DelegatingAutoCloseable<?> instance = new DelegatingAutoCloseable<>(test1, mock);
        instance.close();
        verify(mock, times(1)).accept(test1);
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor() {
        DelegatingAutoCloseable<?> instance = new DelegatingAutoCloseable<>(new Object());
        instance.close();
    }

    /**
     * Test of get method, of class DynamicAutoCloseable.
     */
    @Test
    public void testGet() {
        DelegatingAutoCloseable<Test1> instance = new DelegatingAutoCloseable<>(test1);
        assertThat(instance.getContent(), is(test1));
    }

    static interface Test1 {

        void close();
    }

}
