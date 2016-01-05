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
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class RangesTest {

    public RangesTest() {
    }

    /**
     * Test of closedOpenRange method, of class Ranges.
     */
    @Test
    public void testClosedOpenRange_GenericType_GenericType() {
        Range<Integer> range = Ranges.closedOpenRange(11, 18);
        assertThat(range.contains(10), is(false));
        assertThat(range.contains(11), is(true));
        assertThat(range.contains(12), is(true));
        assertThat(range.contains(17), is(true));
        assertThat(range.contains(18), is(false));
        assertThat(range.contains(19), is(false));
    }

    /**
     * Test of closedOpenRange method, of class Ranges.
     */
    @Test
    public void testClosedOpenRange_3args() {
        IterationUnit<Integer> iu = StandardIterationUnits.INTEGER;
        Range<Integer> range = Ranges.closedOpenRange(11, 18, iu);
        assertThat(range.contains(10), is(false));
        assertThat(range.contains(11), is(true));
        assertThat(range.contains(12), is(true));
        assertThat(range.contains(17), is(true));
        assertThat(range.contains(18), is(false));
        assertThat(range.contains(19), is(false));
    }

    /**
     * Test of closedRange method, of class Ranges.
     */
    @Test
    public void testClosedRange_3args() {
        IterationUnit<Number> iu = StandardIterationUnits.NUMBER_TO_LONG;
        Range<Double> range = Ranges.closedRange(11.0, 18.0, iu);
        assertThat(range.contains(10.0), is(false));
        assertThat(range.contains(11.0), is(true));
        assertThat(range.contains(12.0), is(true));
        assertThat(range.contains(17.0), is(true));
        assertThat(range.contains(18.0), is(true));
        assertThat(range.contains(18.5), is(true));
        assertThat(range.contains(19.0), is(false));
    }

    /**
     * Test of closedRange method, of class Ranges.
     */
    @Test
    public void testClosedRange_GenericType_GenericType() {
        Range<Integer> range = Ranges.closedRange(11, 18);
        assertThat(range.contains(10), is(false));
        assertThat(range.contains(11), is(true));
        assertThat(range.contains(12), is(true));
        assertThat(range.contains(17), is(true));
        assertThat(range.contains(18), is(true));
        assertThat(range.contains(19), is(false));
    }

    /**
     * Test of openClosedRange method, of class Ranges.
     */
    @Test
    public void testOpenClosedRange_GenericType_GenericType() {
        Range<Integer> range = Ranges.openClosedRange(11, 18);
        assertThat(range.contains(10), is(false));
        assertThat(range.contains(11), is(false));
        assertThat(range.contains(12), is(true));
        assertThat(range.contains(17), is(true));
        assertThat(range.contains(18), is(true));
        assertThat(range.contains(19), is(false));
    }

    /**
     * Test of openClosedRange method, of class Ranges.
     */
    @Test
    public void testOpenClosedRange_3args() {
        IterationUnit<Number> iu = StandardIterationUnits.NUMBER_TO_LONG;
        Range<Double> range = Ranges.openClosedRange(11.0, 18.0, iu);
        assertThat(range.contains(10.0), is(false));
        assertThat(range.contains(11.0), is(false));
        assertThat(range.contains(12.0), is(true));
        assertThat(range.contains(17.0), is(true));
        assertThat(range.contains(18.0), is(true));
        assertThat(range.contains(18.5), is(true));
        assertThat(range.contains(19.0), is(false));
    }

    /**
     * Test of openRange method, of class Ranges.
     */
    @Test
    public void testOpenRange_3args() {
        IterationUnit<Number> iu = StandardIterationUnits.NUMBER_TO_LONG;
        Range<Double> range = Ranges.openRange(11.0, 18.0, iu);
        assertThat(range.contains(10.0), is(false));
        assertThat(range.contains(11.0), is(false));
        assertThat(range.contains(12.0), is(true));
        assertThat(range.contains(17.0), is(true));
        assertThat(range.contains(18.0), is(false));
        assertThat(range.contains(18.5), is(false));
        assertThat(range.contains(19.0), is(false));
    }

    /**
     * Test of openRange method, of class Ranges.
     */
    @Test
    public void testOpenRange_GenericType_GenericType() {
        Range<Integer> range = Ranges.openRange(11, 18);
        assertThat(range.contains(10), is(false));
        assertThat(range.contains(11), is(false));
        assertThat(range.contains(12), is(true));
        assertThat(range.contains(17), is(true));
        assertThat(range.contains(18), is(false));
        assertThat(range.contains(19), is(false));
    }

}
