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

import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.createCalendar;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.createDate;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getDatePart;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateUtils;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class DateOnlyTest {

    CurrentDateProvider currentDateProvider;
    Calendar testCalendar;

    /**
     *
     */
    @Before
    public void setUp() {
        ServiceProviders.setThreadContainer(null);
        testCalendar = new GregorianCalendar(2015, Calendar.MAY, 14, 16, 34, 20);
        currentDateProvider = mock(CurrentDateProvider.class);
        when(currentDateProvider.getNow()).thenReturn(testCalendar);
        when(currentDateProvider.getToday()).thenReturn(DateUtils.truncate(testCalendar, Calendar.DAY_OF_MONTH));
        ServiceProviders.getThreadContainer().registerService(currentDateProvider);
    }

    /**
     *
     */
    @Test
    public void testCtor() {
        Date actual = new DateOnly();
        Date expected = new GregorianCalendar(2015, Calendar.MAY, 14).getTime();
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_date() {
        Date x = new Date();
        Date actual = new DateOnly(x);
        Date expected = DateUtils.truncate(x, Calendar.DATE);
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_long1() {
        Date actual = new DateOnly(0);
        Date expected = createCalendar(1970, 1, 1).getTime();
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_long2() {
        long x = 1234567890;
        Date actual = new DateOnly(x);
        Date expected = DateUtils.truncate(new Date(x), Calendar.DATE);
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_ymd() {
        Date actual = new DateOnly(1991, 12, 10);
        Date expected = createDate(1991, 12, 10);
        assertThat(actual, is(expected));
    }

    /**
     * Test of setTime method, of class DateOnly.
     */
    @Test
    public void testSetTime() {
        Date now = new Date();
        Date actual = new DateOnly(0);
        actual.setTime(now.getTime());
        Date expected = getDatePart(now);
        assertThat(actual, is(expected));
    }

}
