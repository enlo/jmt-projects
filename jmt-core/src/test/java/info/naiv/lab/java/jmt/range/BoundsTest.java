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

import java.util.Date;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class BoundsTest {

    /**
     *
     */
    public BoundsTest() {
    }

    /**
     * Test of closedLo method, of class Bounds.
     */
    @Test
    public void testClosedLo() {
        Bound.ClosedLowerBound<Integer> b = Bounds.closedLo(10);
        assertThat(b.value, is(10));
    }

    /**
     * Test of closedUp method, of class Bounds.
     */
    @Test
    public void testClosedUp() {
        Bound.ClosedUpperBound<String> b = Bounds.closedUp("ABC");
        assertThat(b.value, is("ABC"));
    }

    /**
     * Test of openLo method, of class Bounds.
     */
    @Test
    public void testOpenLo() {
        Bound.OpenLowerBound<Double> b = Bounds.openLo(0.0);
        assertThat(b.value, is(0.0));
    }

    /**
     * Test of openUp method, of class Bounds.
     */
    @Test
    public void testOpenUp() {
        Bound.OpenUpperBound<Date> b = Bounds.openUp(new Date(1));
        assertThat(b.value, is(new Date(1)));
    }

}
