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

import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getTimePart;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateUtils;
import static org.apache.commons.lang3.time.DateUtils.toCalendar;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 *
 * @author enlo
 */
public class TimeOnlyTest {

    CurrentDateProvider currentDateProvider;
    Calendar testCalendar;

    /**
     *
     */
    @Before
    public void setUp() {
        ServiceProviders.setThreadContainer(null);
        testCalendar = new GregorianCalendar(2015, Calendar.MAY, 14, 16, 34, 20);
        testCalendar.set(Calendar.MILLISECOND, 345);
        currentDateProvider = mock(CurrentDateProvider.class);
        when(currentDateProvider.getNow()).thenReturn(testCalendar);
        when(currentDateProvider.getToday()).thenReturn(DateUtils.truncate(testCalendar, Calendar.DAY_OF_MONTH));
        ServiceProviders.getThreadContainer().registerService(currentDateProvider);
    }

    @Test
    public void testConversion() {
        /*
        TargetClass.valueOf(SourceClass) が定義されている場合、
        Spring の ObjectToObjectConverter によって自動変換される.
         */
        Date now = new Date();
        ConversionService conversionService = new DefaultConversionService();
        TimeOnly actual = conversionService.convert(now, TimeOnly.class);
        assertThat(actual, is(TimeOnly.valueOf(now)));
    }

    /**
     *
     */
    @Test
    public void testCtor() {
        Date actual = new TimeOnly();
        Calendar expected = new GregorianCalendar(1970, Calendar.JANUARY, 1, 16, 34, 20);
        expected.set(Calendar.MILLISECOND, 345);
        assertThat(actual, is(expected.getTime()));
    }

    /**
     *
     */
    @Test
    public void testCtor_hms() {
        Date actual = new TimeOnly(16, 24, 30);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 24);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = getTimePart(cal).getTime();
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_hmsi() {
        Date actual = new TimeOnly(16, 24, 30, 123);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 24);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date expected = getTimePart(cal).getTime();
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_long1() {
        Date actual = new TimeOnly(0);
        Date expected = getTimePart(new Date(0));
        assertThat(actual, is(expected));
    }

    /**
     *
     */
    @Test
    public void testCtor_long2() {
        long x = 1234567890;
        Date actual = new TimeOnly(x);
        Date expected = getTimePart(new Date(x));
        assertThat(actual, is(expected));
    }

    /**
     * Test of setTime method, of class TimeOnly.
     */
    @Test
    public void testSetTime() {
        Date now = new Date();
        Date actual = new TimeOnly(0);
        actual.setTime(now.getTime());
        Calendar cal = toCalendar(now);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);
        Date expected = new TimeOnly(hour, min, sec, ms);
        assertThat(actual, is(expected));
    }

}
