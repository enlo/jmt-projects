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

import info.naiv.lab.java.jmt.IterationUnit;
import info.naiv.lab.java.jmt.StandardIterationUnits;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class RangeTest {

    public RangeTest() {
    }

    /**
     * Test of begin method, of class Range.
     */
    @Test
    public void testBegin() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.closedLo(10);
        Bound<Integer> ub = Bounds.closedUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.begin(), is(lb.getValue()));
    }

    /**
     * Test of end method, of class Range.
     */
    @Test
    public void testEnd() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.closedLo(10);
        Bound<Integer> ub = Bounds.closedUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.end(), is(ub.getValue()));
    }

    /**
     * Test of getMinValue method, of class Range.
     */
    @Test
    public void testGetMinValue() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.openLo(10);
        Bound<Integer> ub = Bounds.closedUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.getMinValue(), is(11));
    }

    /**
     * Test of getMaxValue method, of class Range.
     */
    @Test
    public void testGetMaxValue() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.openLo(10);
        Bound<Integer> ub = Bounds.openUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.getMaxValue(), is(19));
    }

    /**
     * Test of toIterable method, of class Range.
     */
    @Test
    public void testToIterable() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.openLo(10);
        Bound<Integer> ub = Bounds.openUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.toIterable(), is(contains(11, 12, 13, 14, 15, 16, 17, 18, 19)));
    }

    /**
     * Test of toIterable method, of class Range.
     */
    @Test
    public void testToIterable_2() {
        IterationUnit<Integer> ui = StandardIterationUnits.INTEGER;
        Bound<Integer> lb = Bounds.closedLo(10);
        Bound<Integer> ub = Bounds.closedUp(20);
        Range<Integer> r = new Range<>(ui, lb, ub);
        assertThat(r.toIterable(), is(contains(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)));
    }

    /**
     * Test of newUnit method, of class Range.
     */
    @Test
    public void testNewUnit() {
    }

    /**
     * Test of newUpperBound method, of class Range.
     */
    @Test
    public void testNewUpperBound() {
    }

    /**
     * Test of newLowerBound method, of class Range.
     */
    @Test
    public void testNewLowerBound() {
    }

    /**
     * Test of clone method, of class Range.
     */
    @Test
    public void testClone() {
    }

    /**
     * Test of equals method, of class Range.
     */
    @Test
    public void testEquals() {
    }

    /**
     * Test of hashCode method, of class Range.
     */
    @Test
    public void testHashCode() {
    }

    /**
     * Test of toString method, of class Range.
     */
    @Test
    public void testToString() {
    }

    /**
     * Test of getUnit method, of class Range.
     */
    @Test
    public void testGetUnit() {
    }

    /**
     * Test of getLowerBound method, of class Range.
     */
    @Test
    public void testGetLowerBound() {
    }

    /**
     * Test of getUpperBound method, of class Range.
     */
    @Test
    public void testGetUpperBound() {
    }

}
