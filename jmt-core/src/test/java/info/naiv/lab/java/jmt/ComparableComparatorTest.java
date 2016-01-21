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
package info.naiv.lab.java.jmt;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class ComparableComparatorTest {

    public ComparableComparatorTest() {
    }

    
    /**
     * Test of compare method, of class ComparableComparator.
     */
    @Test
    public void testCompare() {
        ComparableComparator<Integer> instance = ComparableComparator.INSTANCE;
        assertThat("min(1, 2)", instance.compare(1, 2), is(lessThan(0)));
        assertThat("min(4, 2)", instance.compare(4, 2), is(greaterThan(0)));
        assertThat("min(3, 3)", instance.compare(3, 3), is(0));
        assertThat("min(4, null)", instance.compare(4, null), is(greaterThan(0)));
        assertThat("min(null, 5)", instance.compare(null, 5), is(lessThan(0)));
        assertThat("min(null, null)", instance.compare(null, null), is(0));
    }

    /**
     * Test of doCompare method, of class ComparableComparator.
     */
    @Test
    public void testDoCompare() {
        X x = mock(X.class);
        X y = mock(X.class);
        when(x.compareTo(y)).thenReturn(0);
        ComparableComparator instance = ComparableComparator.INSTANCE;
        instance.doCompare(x, y);
        verify(x).compareTo(y);
    }

    /**
     * Test of min method, of class ComparableComparator.
     */
    @Test
    public void testMin() {
        ComparableComparator<Integer> instance = ComparableComparator.INSTANCE;
        assertThat("min(1, 2)", instance.min(1, 2), is(1));
        assertThat("min(4, 2)", instance.min(4, 2), is(2));
        assertThat("min(3, 3)", instance.min(3, 3), is(3));
        assertThat("min(null, 4)", instance.min(null, 4), is(4));
        assertThat("min(5, null)", instance.min(5, null), is(5));
        assertThat("min(null, null)", instance.min(null, null), is(nullValue()));
    }

    interface X extends Comparable<X> {
    }

}
