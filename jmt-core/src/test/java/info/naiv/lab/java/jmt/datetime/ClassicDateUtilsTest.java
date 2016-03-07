/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.datetime;

import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateUtils;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class ClassicDateUtilsTest {

    static final Calendar NULL_CALENDAR = null;

    CurrentDateProvider currentDateProvider;
    Calendar testCalendar;

    public void doTestAddWorkingDays(int i, WorkingDaySettings ws, Calendar in, int workingDay, Calendar expected) {
        Calendar saved = (Calendar) in.clone();
        Calendar actual = ClassicDateUtils.addWorkingDays(in, workingDay, ws);
        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

    @Before
    public void setUp() {

        ServiceProviders.setThreadContainer(null);

        testCalendar = new GregorianCalendar(2015, Calendar.MAY, 14);
        currentDateProvider = mock(CurrentDateProvider.class);
        when(currentDateProvider.getNow()).thenReturn(testCalendar);
        when(currentDateProvider.getToday()).thenReturn(DateUtils.truncate(testCalendar, Calendar.DAY_OF_MONTH));

        ServiceProviders.getThreadContainer().registerService(currentDateProvider);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addWorkingDays method, of class ClassicDateUtils.
     */
    @Test
    public void testAddWorkingDays() {

        WorkingDaySettings ws = WorkingDaySettings.newInstance();

        Calendar in = ClassicDateUtils.createCalendar(2015, 3, 10);
        doTestAddWorkingDays(1, ws, in, 7, ClassicDateUtils.createCalendar(2015, 3, 19));
    }

    /**
     * Test of add method, of class ClassicDateUtils.
     */
    @Test
    public void testAdd_3args_1() {
        Calendar in = Calendar.getInstance();

        doTestAdd(1, in, 10, TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        doTestAdd(1, in, 10, TimeUnit.SECONDS, Calendar.SECOND);
        doTestAdd(1, in, 10, TimeUnit.MINUTES, Calendar.MINUTE);
        doTestAdd(1, in, 10, TimeUnit.HOURS, Calendar.HOUR_OF_DAY);
        doTestAdd(1, in, 10, TimeUnit.DAYS, Calendar.DAY_OF_MONTH);
        doTestAdd(1, in, 1100, TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        doTestAdd(1, in, 100, TimeUnit.SECONDS, Calendar.SECOND);
        doTestAdd(1, in, 100, TimeUnit.MINUTES, Calendar.MINUTE);
        doTestAdd(1, in, 100, TimeUnit.HOURS, Calendar.HOUR_OF_DAY);
        doTestAdd(1, in, 400, TimeUnit.DAYS, Calendar.DAY_OF_MONTH);
    }

    /**
     * Test of add method, of class ClassicDateUtils.
     */
    @Test
    public void testAdd_3args_2() {
        Date in = new Date();

        doTestAdd(1, in, 10, TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        doTestAdd(1, in, 10, TimeUnit.SECONDS, Calendar.SECOND);
        doTestAdd(1, in, 10, TimeUnit.MINUTES, Calendar.MINUTE);
        doTestAdd(1, in, 10, TimeUnit.HOURS, Calendar.HOUR_OF_DAY);
        doTestAdd(1, in, 10, TimeUnit.DAYS, Calendar.DAY_OF_MONTH);
        doTestAdd(1, in, 1100, TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        doTestAdd(1, in, 100, TimeUnit.SECONDS, Calendar.SECOND);
        doTestAdd(1, in, 100, TimeUnit.MINUTES, Calendar.MINUTE);
        doTestAdd(1, in, 100, TimeUnit.HOURS, Calendar.HOUR_OF_DAY);
        doTestAdd(1, in, 400, TimeUnit.DAYS, Calendar.DAY_OF_MONTH);

    }

    /**
     * Test of clearDatePart method, of class ClassicDateUtils.
     */
    @Test
    public void testClearDatePart() {
        Calendar base = new GregorianCalendar(2015, 10, 11, 12, 13, 14);
        Calendar in = (Calendar) base.clone();
        Calendar expected = new GregorianCalendar(1970, 0, 1, 12, 13, 14);
        Calendar actual = ClassicDateUtils.clearDatePart(in);
        assertThat(in, is(expected));
        assertThat(actual, is(sameInstance(in)));
    }

    /**
     * Test of clearDatePart method, of class ClassicDateUtils.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClearDatePart_null() {
        ClassicDateUtils.clearDatePart(NULL_CALENDAR);
    }

    /**
     * Test of clearTimePart method, of class ClassicDateUtils.
     */
    @Test
    public void testClearTimePart() {
        Calendar base = new GregorianCalendar(2015, 10, 11, 12, 13, 14);
        Calendar in = (Calendar) base.clone();
        Calendar expected = new GregorianCalendar(2015, 10, 11, 0, 0, 0);
        Calendar actual = ClassicDateUtils.clearTimePart(in);
        assertThat(in, is(expected));
        assertThat(actual, is(sameInstance(in)));
    }

    /**
     * Test of computeFirstBizDayOfWeek method, of class ClassicDateUtils.
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
        ws.getHolidays().add(ClassicDateUtils.createCalendar(2015, 3, 18));
        doTestComputeFirstBizDayOfWeek(19, ws, ClassicDateUtils.createCalendar(2015, 3, 17), ClassicDateUtils.createCalendar(2015, 3, 11));
        doTestComputeFirstBizDayOfWeek(20, ws, ClassicDateUtils.createCalendar(2015, 3, 18), ClassicDateUtils.createCalendar(2015, 3, 19));
        doTestComputeFirstBizDayOfWeek(21, ws, ClassicDateUtils.createCalendar(2015, 3, 19), ClassicDateUtils.createCalendar(2015, 3, 19));
    }

    /**
     * Test of computeNextWeekday method, of class ClassicDateUtils.
     */
    @Test
    public void testComputeNextWeekday() {
        List<Integer> weeks = Arrays.asList(Calendar.WEDNESDAY, Calendar.FRIDAY, Calendar.SATURDAY);
        Calendar base = new GregorianCalendar(2014, Calendar.JULY, 27);
        Calendar from = (Calendar) base.clone();
        Calendar[] expecteds = {
            new GregorianCalendar(2014, Calendar.JULY, 30), // 27
            new GregorianCalendar(2014, Calendar.JULY, 30), // 28
            new GregorianCalendar(2014, Calendar.JULY, 30), // 29
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 30
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 31
            new GregorianCalendar(2014, Calendar.AUGUST, 2), // 1
            new GregorianCalendar(2014, Calendar.AUGUST, 6), // 2
        };
        for (int i = 0; i < expecteds.length; i++, base.add(Calendar.DATE, 1), from.add(Calendar.DATE, 1)) {
            Calendar expected = expecteds[i];
            Calendar actual = ClassicDateUtils.computeNextWeekday(from, weeks, base);
            assertThat(actual, is(expected));
        }
    }

    /**
     * Test of computeNextWeekday method, of class ClassicDateUtils.
     */
    @Test
    public void testComputeNextWeekday2() {
        List<Integer> weeks = Arrays.asList(Calendar.SUNDAY, Calendar.TUESDAY);
        Calendar base = new GregorianCalendar(2014, Calendar.JULY, 27);
        Calendar from = (Calendar) base.clone();
        Calendar[] expecteds = {
            new GregorianCalendar(2014, Calendar.JULY, 29), // 27
            new GregorianCalendar(2014, Calendar.JULY, 29), // 28
            new GregorianCalendar(2014, Calendar.AUGUST, 3), // 29
            new GregorianCalendar(2014, Calendar.AUGUST, 3), // 30
            new GregorianCalendar(2014, Calendar.AUGUST, 3), // 31
            new GregorianCalendar(2014, Calendar.AUGUST, 3), // 1
            new GregorianCalendar(2014, Calendar.AUGUST, 3), // 2
        };
        for (int i = 0; i < expecteds.length; i++, base.add(Calendar.DATE, 1), from.add(Calendar.DATE, 1)) {
            Calendar expected = expecteds[i];
            Calendar actual = ClassicDateUtils.computeNextWeekday(from, weeks, base);
            assertThat(actual, is(expected));
        }
    }

    /**
     * Test of computeNextWeekday method, of class ClassicDateUtils.
     */
    @Test
    public void testComputeNextWeekday3() {
        List<Integer> weeks = Arrays.asList(Calendar.WEDNESDAY, Calendar.FRIDAY, Calendar.SATURDAY);
        Calendar base = new GregorianCalendar(2014, Calendar.JULY, 30);
        Calendar from = new GregorianCalendar(2014, Calendar.JULY, 27);
        Calendar[] expecteds = {
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 27
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 28
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 29
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 30
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 31
            new GregorianCalendar(2014, Calendar.AUGUST, 1), // 1
            new GregorianCalendar(2014, Calendar.AUGUST, 2), // 2
        };
        for (int i = 0; i < expecteds.length; i++, from.add(Calendar.DATE, 1)) {
            Calendar expected = expecteds[i];
            Calendar actual = ClassicDateUtils.computeNextWeekday(from, weeks, base);
            assertThat("" + i, actual, is(expected));
        }
    }

    /**
     * Test of copyFields method, of class ClassicDateUtils.
     */
    @Test
    public void testCopyFields() {
        Calendar in = ClassicDateUtils.createCalendar(2001, 2, 3, 4, 5, 6, 7);

        for (int field : ClassicDateUtils.DATE_PART_FIELDS) {
            doCopyFieldsTest(in, field);
        }
        for (int field : ClassicDateUtils.TIME_PART_FIELDS) {
            doCopyFieldsTest(in, field);
        }
    }

    /**
     * Test of createCalendar method, of class ClassicDateUtils.
     */
    @Test
    public void testCreateCalendar_3args() {
        Calendar actual = ClassicDateUtils.createCalendar(2014, 12, 11);
        Calendar expected = new GregorianCalendar(2014, Calendar.DECEMBER, 11);
        expected.setLenient(false);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCreateCalendar_3args_Ctor() {
        ClassicDateUtils.createCalendar(2014, 12, 30);
        ClassicDateUtils.createCalendar(2014, 10, 11);
        ClassicDateUtils.createCalendar(2014, 1, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_3args_DayOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_3args_DayUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_3args_MonthOver() {
        ClassicDateUtils.createCalendar(2014, 13, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_3args_MonthUnder() {
        ClassicDateUtils.createCalendar(2014, 0, 11);
    }

    /**
     * Test of createCalendar method, of class ClassicDateUtils.
     */
    @Test
    public void testCreateCalendar_6args() {
        int year = 1;
        int month = 2;
        int day = 3;
        int hourOfDay = 4;
        int minute = 5;
        int second = 6;
        Calendar expected = Calendar.getInstance();
        expected.setLenient(false);
        expected.set(year, month - 1, day, hourOfDay, minute, second);
        expected.set(Calendar.MILLISECOND, 0);
        Calendar actual = ClassicDateUtils.createCalendar(year, month, day, hourOfDay, minute, second);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCreateCalendar_6args_Ctor() {
        ClassicDateUtils.createCalendar(2014, 1, 1, 0, 0, 0);
        ClassicDateUtils.createCalendar(2014, 5, 15, 12, 30, 30);
        ClassicDateUtils.createCalendar(2014, 12, 31, 23, 59, 59);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_DayOver() {
        ClassicDateUtils.createCalendar(2014, 11, 31, 0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_DayUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_HourOver() {
        ClassicDateUtils.createCalendar(2014, 13, 11, 25, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_HourUnder() {
        ClassicDateUtils.createCalendar(2014, 11, 11, -1, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_MinuteOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32, 0, 60, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_MinuteUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, -1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_MonthOver() {
        ClassicDateUtils.createCalendar(2014, 13, 11, 0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_MonthUnder() {
        ClassicDateUtils.createCalendar(2014, 0, 11, 0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_SecondOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32, 0, 1, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_6args_SecondUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, 1, -1);
    }

    /**
     * Test of createCalendar method, of class ClassicDateUtils.
     */
    @Test
    public void testCreateCalendar_7args() {
        int year = 1;
        int month = 2;
        int day = 3;
        int hourOfDay = 4;
        int minute = 5;
        int second = 6;
        int millisecond = 7;
        Calendar expected = Calendar.getInstance();
        expected.setLenient(false);
        expected.set(year, month - 1, day, hourOfDay, minute, second);
        expected.set(Calendar.MILLISECOND, millisecond);
        Calendar actual = ClassicDateUtils.createCalendar(year, month, day, hourOfDay, minute, second, millisecond);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCreateCalendar_7args_Ctor() {
        ClassicDateUtils.createCalendar(2014, 1, 1, 0, 0, 0, 0);
        ClassicDateUtils.createCalendar(2014, 5, 15, 12, 30, 30, 500);
        ClassicDateUtils.createCalendar(2014, 12, 31, 23, 59, 59, 999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_DayOver() {
        ClassicDateUtils.createCalendar(2014, 11, 31, 0, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_DayUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_HourOver() {
        ClassicDateUtils.createCalendar(2014, 13, 11, 25, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_HourUnder() {
        ClassicDateUtils.createCalendar(2014, 11, 11, -1, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MillsecondOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32, 0, 1, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MillsecondUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, 1, 0, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MinuteOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32, 0, 60, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MinuteUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, -1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MonthOver() {
        ClassicDateUtils.createCalendar(2014, 13, 11, 0, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_MonthUnder() {
        ClassicDateUtils.createCalendar(2014, 0, 11, 0, 1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_SecondOver() {
        ClassicDateUtils.createCalendar(2014, 12, 32, 0, 1, 60, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCalendar_7args_SecondUnder() {
        ClassicDateUtils.createCalendar(2014, 1, 0, 0, 1, -1, 0);
    }

    @Test
    public void testCreateDate() {
        Date actual = ClassicDateUtils.createDate(2014, 12, 11);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 12 - 1, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertThat(actual, is(cal.getTime()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_DayOver() {
        ClassicDateUtils.createDate(2014, 12, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_DayOver_Leap() {
        ClassicDateUtils.createDate(2011, 2, 29);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_DayUnder() {
        ClassicDateUtils.createDate(2014, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_MonthOver() {
        ClassicDateUtils.createDate(2014, 13, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_MonthUnder() {
        ClassicDateUtils.createDate(2014, 0, 11);
    }

    /**
     * Test of createRandomDate method, of class ClassicDateUtils.
     */
    @Test
    public void testCreateRandomDate() {
    }

    /**
     * Test of getCurrentDateProvider method, of class ClassicDateUtils.
     */
    @Test
    public void testGetCurrentDateProvider() {
        CurrentDateProvider result = ClassicDateUtils.getCurrentDateProvider();
        assertThat(result, is(sameInstance(currentDateProvider)));
    }

    /**
     * Test of getDatePart method, of class ClassicDateUtils.
     */
    @Test
    public void testGetDatePart_Calendar() {
        Calendar base = new GregorianCalendar(2015, 10, 11, 12, 13, 14);
        base.set(Calendar.MILLISECOND, 15);
        Calendar in = (Calendar) base.clone();
        Calendar expected = new GregorianCalendar(2015, 10, 11, 0, 0, 0);
        Calendar actual = ClassicDateUtils.getDatePart(in);
        assertThat(actual, is(expected));
        assertThat(in, is(base));
    }

    /**
     * Test of getDatePart method, of class ClassicDateUtils.
     */
    @Test
    public void testGetDatePart_Date() {
        Date base = new GregorianCalendar(2015, 10, 11, 12, 13, 14).getTime();
        Date in = (Date) base.clone();
        Date expected = new GregorianCalendar(2015, 10, 11, 0, 0, 0).getTime();
        Date actual = ClassicDateUtils.getDatePart(in);
        assertThat(actual, is(expected));
        assertThat(in, is(base));
    }

    /**
     * Test of timePartOf method, of class ClassicDateUtils.
     */
    @Test
    public void testGetTimePart_Calendar() {
        Calendar base = new GregorianCalendar(2015, 10, 11, 12, 13, 14);
        base.set(Calendar.MILLISECOND, 15);
        Calendar in = (Calendar) base.clone();
        Calendar expected = new GregorianCalendar(1970, 0, 1, 12, 13, 14);
        expected.set(Calendar.MILLISECOND, 15);
        Calendar actual = ClassicDateUtils.getTimePart(in);
        assertThat(actual, is(expected));
        assertThat(in, is(base));
    }

    /**
     * Test of timePart method, of class ClassicDateUtils.
     */
    @Test
    public void testGetTimePart_Date() {
        Date base = new GregorianCalendar(2015, 10, 11, 12, 13, 14).getTime();
        Date in = (Date) base.clone();
        Date expected = new GregorianCalendar(1970, 0, 1, 12, 13, 14).getTime();
        Date actual = ClassicDateUtils.getTimePart(in);
        assertThat(actual, is(expected));
        assertThat(in, is(base));
    }

    /**
     * Test of isSameDay method, of class ClassicDateUtils.
     */
    @Test
    public void testIsSameDay() {
    }

    /**
     * Test of now method, of class ClassicDateUtils.
     */
    @Test
    public void testNow() {
        Calendar actual = ClassicDateUtils.now();
        assertThat(actual, is(testCalendar));
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.lang.Exception
     */
    @Test(expected = ParseException.class)
    public void testParseCalendar() throws Exception {
        ClassicDateUtils.parseCalendar("");
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test
    public void testParseCalendar_1() throws ParseException {
        String source = "2014-12-31";
        Calendar expected = ClassicDateUtils.createCalendar(2014, 12, 31);
        Calendar actual = ClassicDateUtils.parseCalendar(source);
        assertThat(actual, is(equalTo(expected)));
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test
    public void testParseCalendar_2() throws ParseException {
        String source = "2014/12/31";
        Calendar expected = ClassicDateUtils.createCalendar(2014, 12, 31);
        Calendar actual = ClassicDateUtils.parseCalendar(source);
        assertThat(actual, is(expected));
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseCalendar_3() throws ParseException {
        String source = "2014/12/32";
        ClassicDateUtils.parseCalendar(source);
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test
    public void testParseCalendar_4() throws ParseException {
        String source = "12/12/12";
        Calendar expected = ClassicDateUtils.createCalendar(2012, 12, 12);
        Calendar actual = ClassicDateUtils.parseCalendar(source);
        assertThat(actual, is(expected));
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test
    public void testParseCalendar_5() throws ParseException {
        String source = "98/12/12";
        Calendar expected = ClassicDateUtils.createCalendar(1998, 12, 12);
        Calendar actual = ClassicDateUtils.parseCalendar(source);
        assertThat(actual, is(expected));
    }

    /**
     * Test of parseCalendar method, of class ClassicDateUtils.
     *
     * @throws java.text.ParseException
     */
    @Test
    public void testParseCalendar_6() throws ParseException {
        String source = "2014-12-24T20:30:40";
        Calendar expected = new GregorianCalendar(2014, 11, 24, 20, 30, 40);
        expected.setLenient(false);

        Calendar actual = ClassicDateUtils.parseCalendar(source);
        assertThat(actual, is(expected));
    }

    /**
     * Test of shiftIfHoliday method, of class ClassicDateUtils.
     */
    @Test
    public void testShiftIfHoliday() {
        WorkingDaySettings ws = WorkingDaySettings.newInstance();
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 10), ClassicDateUtils.createCalendar(2015, 3, 10));
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 14), ClassicDateUtils.createCalendar(2015, 3, 16));
        ws.setShiftForward(false);
        doTestShiftIfHoliday(1, ws, ClassicDateUtils.createCalendar(2015, 3, 14), ClassicDateUtils.createCalendar(2015, 3, 13));
    }

    private void doCopyFieldsTest(Calendar in, int field) {
        Calendar now = Calendar.getInstance();
        Calendar expected = (Calendar) now.clone();
        expected.set(field, in.get(field));

        assertThat(ClassicDateUtils.copyFields(now, in, field), is(expected));
    }

    private void doTestAdd(int i, Calendar in, int amount, TimeUnit timeUnit, int field) {
        Calendar saved = (Calendar) in.clone();
        Calendar expected = (Calendar) in.clone();
        expected.add(field, amount);
        Calendar actual = ClassicDateUtils.add(in, amount, timeUnit);

        String ix = Integer.toString(i);
        assertThat(ix, in, is(saved));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

    private void doTestAdd(int i, Date in, int amount, TimeUnit timeUnit, int field) {
        Date saved = (Date) in.clone();
        Calendar expected = DateUtils.toCalendar(in);
        expected.add(field, amount);
        Date actual = ClassicDateUtils.add(in, amount, timeUnit);

        String ix = Integer.toString(i);
        assertThat(ix, in, is(saved));
        assertThat(ix, actual, is(expected.getTime()));
    }

    private void doTestComputeFirstBizDayOfWeek(int i, WorkingDaySettings ws, Calendar in, Calendar expected) {
        Calendar saved = (Calendar) in.clone();
        Calendar actual = ClassicDateUtils.computeFirstBizDayOfWeek(in, ws);
        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }

    private void doTestShiftIfHoliday(int i, WorkingDaySettings ws, Calendar in, Calendar expected) {
        Calendar saved = (Calendar) in.clone();
        Calendar actual = ClassicDateUtils.shiftIfHoliday(in, ws);

        String ix = Integer.toString(i);
        assertThat(ix, saved, is(in));
        assertThat(ix, actual, is(notNullValue()));
        assertThat(ix, actual, is(not(sameInstance(in))));
        assertThat(ix, actual, is(comparesEqualTo(expected)));
    }
}
