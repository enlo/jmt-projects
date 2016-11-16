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
package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.datetime.CalendarIterationUnits.*;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.createCalendar;
import java.util.Calendar;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class CalendarIterationUnitsTest {

    public CalendarIterationUnitsTest() {
    }

    /**
     * Test of truncate method, of class CalendarIterationUnits.
     */
    @Test
    public void testTruncate() {
        Calendar cal = createCalendar(2016, 10, 24, 17, 42, 37, 357);

        assertThat(DAY1.name(), DAY1.truncate(cal), is(createCalendar(2016, 10, 24)));
        assertThat(HOUR1.name(), HOUR1.truncate(cal), is(createCalendar(2016, 10, 24, 17, 0, 0, 0)));
        assertThat(HOUR12.name(), HOUR12.truncate(cal), is(createCalendar(2016, 10, 24, 12, 0, 0, 0)));
        assertThat(HOUR2.name(), HOUR2.truncate(cal), is(createCalendar(2016, 10, 24, 16, 0, 0, 0)));
        assertThat(HOUR3.name(), HOUR3.truncate(cal), is(createCalendar(2016, 10, 24, 15, 0, 0, 0)));
        assertThat(HOUR4.name(), HOUR4.truncate(cal), is(createCalendar(2016, 10, 24, 16, 0, 0, 0)));
    }

    /**
     * Test of advance method, of class CalendarIterationUnits.
     */
    @Test
    public void testAdvance() {
    }

    /**
     * Test of next method, of class CalendarIterationUnits.
     */
    @Test
    public void testNext() {
    }

    /**
     * Test of prior method, of class CalendarIterationUnits.
     */
    @Test
    public void testPrior() {
    }

    /**
     * Test of distance method, of class CalendarIterationUnits.
     */
    @Test
    public void testDistance() {
    }

    /**
     * Test of compare method, of class CalendarIterationUnits.
     */
    @Test
    public void testCompare() {
    }

}
