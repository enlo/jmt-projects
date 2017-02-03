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
package info.naiv.lab.java.jmt.datetime.workingday;

import info.naiv.lab.java.jmt.datetime.WeekSettings;
import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import java.util.Calendar;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class WorkingDayCalculatorTest {

    public WorkingDayCalculatorTest() {
    }

    /**
     * Test of addWorkingDays method, of class WorkingDayCalculator.
     */
    @Test
    public void testAddWorkingDays() {
        WorkingDaySettings ws = WorkingDaySettings.newInstance();
        Calendar in = ClassicDateUtils.createCalendar(2015, 3, 10);
        doTestAddWorkingDays(1, ws, in, 7, ClassicDateUtils.createCalendar(2015, 3, 19));
    }

    /**
     * Test of computeFirstBizDayOfWeek method, of class WorkingDayCalculator.
     */
    @Test
    public void testComputeFirstBizDayOfWeek() {
        WorkingDaySettings ws = WorkingDaySettings.newInstance();
        doTestComputeFirstBizDayOfWeek(1, ws, ClassicDateUtils.createCalendar(2015, 3, 14), ClassicDateUtils.createCalendar(2015, 3, 9));
        doTestComputeFirstBizDayOfWeek(2, ws, ClassicDateUtils.createCalendar(2015, 3, 15), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(3, ws, ClassicDateUtils.createCalendar(2015, 3, 18), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(4, ws, ClassicDateUtils.createCalendar(2015, 3, 20), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(5, ws, ClassicDateUtils.createCalendar(2015, 3, 21), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(6, ws, ClassicDateUtils.createCalendar(2015, 3, 22), ClassicDateUtils.createCalendar(2015, 3, 23));

        ws.setWeekSettings(new WeekSettings(Calendar.MONDAY, Calendar.SUNDAY));
        doTestComputeFirstBizDayOfWeek(7, ws, ClassicDateUtils.createCalendar(2015, 3, 15), ClassicDateUtils.createCalendar(2015, 3, 9));
        doTestComputeFirstBizDayOfWeek(8, ws, ClassicDateUtils.createCalendar(2015, 3, 16), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(9, ws, ClassicDateUtils.createCalendar(2015, 3, 18), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(10, ws, ClassicDateUtils.createCalendar(2015, 3, 21), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(11, ws, ClassicDateUtils.createCalendar(2015, 3, 22), ClassicDateUtils.createCalendar(2015, 3, 16));
        doTestComputeFirstBizDayOfWeek(12, ws, ClassicDateUtils.createCalendar(2015, 3, 23), ClassicDateUtils.createCalendar(2015, 3, 23));

        ws.setWeekSettings(new WeekSettings(Calendar.WEDNESDAY, Calendar.SUNDAY));
        doTestComputeFirstBizDayOfWeek(13, ws, ClassicDateUtils.createCalendar(2015, 3, 17), ClassicDateUtils.createCalendar(2015, 3, 11));
        doTestComputeFirstBizDayOfWeek(14, ws, ClassicDateUtils.createCalendar(2015, 3, 18), ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(15, ws, ClassicDateUtils.createCalendar(2015, 3, 19), ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(16, ws, ClassicDateUtils.createCalendar(2015, 3, 21), ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(17, ws, ClassicDateUtils.createCalendar(2015, 3, 22), ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(18, ws, ClassicDateUtils.createCalendar(2015, 3, 23), ClassicDateUtils.createCalendar(2015, 3, 18));

        ws.setWeekSettings(new WeekSettings(Calendar.WEDNESDAY, Calendar.SUNDAY));
        ws.addHolidays(ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(19, ws, ClassicDateUtils.createCalendar(2015, 3, 17), ClassicDateUtils.createCalendar(2015, 3, 11));
        doTestComputeFirstBizDayOfWeek(20, ws, ClassicDateUtils.createCalendar(2015, 3, 18), ClassicDateUtils.createCalendar(2015, 3, 19));
        doTestComputeFirstBizDayOfWeek(21, ws, ClassicDateUtils.createCalendar(2015, 3, 19), ClassicDateUtils.createCalendar(2015, 3, 19));

    }

    /**
     * Test of countWorkingDays method, of class WorkingDayCalculator.
     */
    @Test
    public void testCountWorkingDays() {
    }

    /**
     * Test of getSettings method, of class WorkingDayCalculator.
     */
    @Test
    public void testGetSettings() {
    }

    /**
     * Test of setSettings method, of class WorkingDayCalculator.
     */
    @Test
    public void testSetSettings() {
    }

    /**
     * Test of shiftIfHoliday method, of class WorkingDayCalculator.
     */
    @Test
    public void testShiftIfHoliday() {
        WorkingDaySettings ws = WorkingDaySettings.newInstance();
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 10), ClassicDateUtils.createCalendar(2015, 3, 10));
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 14), ClassicDateUtils.createCalendar(2015, 3, 16));
        ws.setShiftForward(false);
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 14), ClassicDateUtils.createCalendar(2015, 3, 13));
    }

    /**
     *
     * @param i
     * @param ws
     * @param in
     * @param workingDay
     * @param expected
     */
    private void doTestAddWorkingDays(int i, WorkingDaySettings ws, Calendar in, int workingDay, Calendar expected) {
        WorkingDayCalculator calc = new WorkingDayCalculator(ws);
        Calendar saved = (Calendar) in.clone();
        Calendar actual = calc.addWorkingDays(in, workingDay);
        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

    private void doTestComputeFirstBizDayOfWeek(int i, WorkingDaySettings ws, Calendar in, Calendar expected) {
        WorkingDayCalculator calc = new WorkingDayCalculator(ws);
        Calendar saved = (Calendar) in.clone();
        Calendar actual = calc.computeFirstBizDayOfWeek(in);
        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

    private void doTestShiftIfHoliday(int i, WorkingDaySettings ws, Calendar in, Calendar expected) {
        WorkingDayCalculator calc = new WorkingDayCalculator(ws);
        Calendar saved = (Calendar) in.clone();
        Calendar actual = calc.shiftIfHoliday(in);

        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

}
