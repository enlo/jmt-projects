/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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
package info.naiv.lab.java.jmt.range;

import info.naiv.lab.java.jmt.ComparableComparator;
import java.util.Comparator;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class BoundTest {

    public BoundTest() {
    }

    /**
     * Test of clone method, of class Bound.
     */
    @Test
    public void testClone() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            Bound<Integer> cb = b.clone();
            assertThat("ClosedLowerBound#type", cb.type, is(b.type));
            assertThat("ClosedLowerBound#value", cb.value, is(100));
            assertThat("ClosedLowerBound#class", cb, instanceOf(Bound.ClosedLowerBound.class));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            Bound<Integer> cb = b.clone();
            assertThat("ClosedUpperBound#type", cb.type, is(b.type));
            assertThat("ClosedUpperBound#value", cb.value, is(10));
            assertThat("ClosedUpperBound#class", cb, instanceOf(Bound.ClosedUpperBound.class));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            Bound<Double> cb = b.clone();
            assertThat("OpenLowerBound#type", cb.type, is(b.type));
            assertThat("OpenLowerBound#value", cb.value, is(0.1));
            assertThat("OpenLowerBound#class", cb, instanceOf(Bound.OpenLowerBound.class));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            Bound<Double> cb = b.clone();
            assertThat("OpenUpperBound#type", cb.type, is(b.type));
            assertThat("OpenUpperBound#value", cb.value, is(1.1));
            assertThat("OpenUpperBound#class", cb, instanceOf(Bound.OpenUpperBound.class));
        }
    }

    /**
     * Test of construct method, of class Bound.
     */
    @Test
    public void testConstruct() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            Bound<Integer> cb = b.construct(20);
            assertThat("ClosedLowerBound#type", cb.type, is(b.type));
            assertThat("ClosedLowerBound#value", cb.value, is(20));
            assertThat("ClosedLowerBound#class", cb, instanceOf(Bound.ClosedLowerBound.class));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            Bound<Integer> cb = b.construct(20);
            assertThat("ClosedUpperBound#type", cb.type, is(b.type));
            assertThat("ClosedUpperBound#value", cb.value, is(20));
            assertThat("ClosedUpperBound#class", cb, instanceOf(Bound.ClosedUpperBound.class));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            Bound<Double> cb = b.construct(20.0);
            assertThat("OpenLowerBound#type", cb.type, is(b.type));
            assertThat("OpenLowerBound#value", cb.value, is(20.0));
            assertThat("OpenLowerBound#class", cb, instanceOf(Bound.OpenLowerBound.class));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            Bound<Double> cb = b.construct(20.0);
            assertThat("OpenUpperBound#type", cb.type, is(b.type));
            assertThat("OpenUpperBound#value", cb.value, is(20.0));
            assertThat("OpenUpperBound#class", cb, instanceOf(Bound.OpenUpperBound.class));
        }
    }

    /**
     * Test of equals method, of class Bound.
     */
    @Test
    public void testEquals() {
        Bound<Integer> b1 = new Bound.ClosedLowerBound<>(10);
        Bound<Integer> b2 = new Bound.ClosedLowerBound<>(20);
        Bound<Integer> b3 = new Bound.ClosedLowerBound<>(10);
        assertThat(b1, is(not(b2)));
        assertThat(b1, is(b3));
        Bound<Integer> b4 = new Bound.ClosedUpperBound<>(10);
        Bound<Integer> b5 = new Bound.ClosedUpperBound<>(20);
        Bound<Integer> b6 = new Bound.ClosedUpperBound<>(10);
        assertThat(b4, is(not(b5)));
        assertThat(b4, is(b6));
        assertThat(b4, is(not(b1)));
        Bound<Integer> b7 = new Bound.OpenLowerBound<>(10);
        Bound<Integer> b8 = new Bound.OpenLowerBound<>(20);
        Bound<Integer> b9 = new Bound.OpenLowerBound<>(10);
        assertThat(b7, is(not(b8)));
        assertThat(b7, is(b9));
        assertThat(b7, is(not(b1)));
        assertThat(b7, is(not(b4)));
        Bound<Integer> b10 = new Bound.OpenUpperBound<>(10);
        Bound<Integer> b11 = new Bound.OpenUpperBound<>(20);
        Bound<Integer> b12 = new Bound.OpenUpperBound<>(10);
        assertThat(b10, is(not(b11)));
        assertThat(b10, is(b12));
        assertThat(b10, is(not(b1)));
        assertThat(b10, is(not(b4)));
        assertThat(b10, is(not(b7)));
    }

    /**
     * Test of getType method, of class Bound.
     */
    @Test
    public void testGetType() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound", b.getType(), is(BoundType.CLOSED));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            assertThat("ClosedUpperBound", b.getType(), is(BoundType.CLOSED));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            assertThat("OpenLowerBound", b.getType(), is(BoundType.OPEN));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            assertThat("OpenUpperBound", b.getType(), is(BoundType.OPEN));
        }
    }

    /**
     * Test of getValue method, of class Bound.
     */
    @Test
    public void testGetValue() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound", b.getValue(), is(100));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            assertThat("ClosedUpperBound", b.getValue(), is(10));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            assertThat("OpenLowerBound", b.getValue(), is(0.1));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            assertThat("OpenUpperBound", b.getValue(), is(1.1));
        }
    }

    /**
     * Test of hashCode method, of class Bound.
     */
    @Test
    public void testHashCode() {
        Bound<Integer> b1 = new Bound.ClosedLowerBound<>(10);
        Bound<Integer> b2 = new Bound.ClosedLowerBound<>(10);
        assertThat(b1.hashCode(), is(b2.hashCode()));
        Bound<Integer> b3 = new Bound.ClosedUpperBound<>(10);
        Bound<Integer> b4 = new Bound.ClosedUpperBound<>(10);
        assertThat(b3.hashCode(), is(b4.hashCode()));
        Bound<Integer> b5 = new Bound.OpenLowerBound<>(10);
        Bound<Integer> b6 = new Bound.OpenLowerBound<>(10);
        assertThat(b5.hashCode(), is(b6.hashCode()));
        Bound<Integer> b7 = new Bound.OpenUpperBound<>(10);
        Bound<Integer> b8 = new Bound.OpenUpperBound<>(10);
        assertThat(b7.hashCode(), is(b8.hashCode()));
    }

    /**
     * Test of isClosed method, of class Bound.
     */
    @Test
    public void testIsClosed() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound", b.isClosed(), is(true));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            assertThat("ClosedUpperBound", b.isClosed(), is(true));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            assertThat("OpenLowerBound", b.isClosed(), is(false));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            assertThat("OpenUpperBound", b.isClosed(), is(false));
        }
    }

    /**
     * Test of isOpen method, of class Bound.
     */
    @Test
    public void testIsOpen() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound", b.isOpen(), is(false));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            assertThat("ClosedUpperBound", b.isOpen(), is(false));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            assertThat("OpenLowerBound", b.isOpen(), is(true));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            assertThat("OpenUpperBound", b.isOpen(), is(true));
        }
    }

    /**
     * Test of on method, of class Bound.
     */
    @Test
    public void testOn() {
        Comparator<Integer> c = new ComparableComparator<>();
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound(99)", b.on(99, c), is(false));
            assertThat("ClosedLowerBound(100)", b.on(100, c), is(true));
            assertThat("ClosedLowerBound(101)", b.on(101, c), is(true));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(100);
            assertThat("ClosedUpperBound(99)", b.on(99, c), is(true));
            assertThat("ClosedUpperBound(100)", b.on(100, c), is(true));
            assertThat("ClosedUpperBound(101)", b.on(101, c), is(false));
        }
        {
            Bound<Integer> b = new Bound.OpenLowerBound<>(100);
            assertThat("OpenLowerBound(99)", b.on(99, c), is(false));
            assertThat("OpenLowerBound(100)", b.on(100, c), is(false));
            assertThat("OpenLowerBound(101)", b.on(101, c), is(true));
        }
        {
            Bound<Integer> b = new Bound.OpenUpperBound<>(100);
            assertThat("OpenUpperBound(99)", b.on(99, c), is(true));
            assertThat("OpenUpperBound(100)", b.on(100, c), is(false));
            assertThat("OpenUpperBound(101)", b.on(101, c), is(false));
        }
    }

    /**
     * Test of toString method, of class Bound.
     */
    @Test
    public void testToString() {
        {
            Bound<Integer> b = new Bound.ClosedLowerBound<>(100);
            assertThat("ClosedLowerBound", b.toString(), is(not(nullValue())));
        }
        {
            Bound<Integer> b = new Bound.ClosedUpperBound<>(10);
            assertThat("ClosedUpperBound", b.toString(), is(not(nullValue())));
        }
        {
            Bound<Double> b = new Bound.OpenLowerBound<>(0.1);
            assertThat("OpenLowerBound", b.toString(), is(not(nullValue())));
        }
        {
            Bound<Double> b = new Bound.OpenUpperBound<>(1.1);
            assertThat("OpenUpperBound", b.toString(), is(not(nullValue())));
        }
    }

}
