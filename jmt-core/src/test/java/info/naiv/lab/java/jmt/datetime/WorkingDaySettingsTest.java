/*
 * Copyright (c) 2015, enlo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.naiv.lab.java.jmt.datetime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class WorkingDaySettingsTest {

    public WorkingDaySettingsTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isHoliday method, of class WorkingDaySettings.
     */
    @Test
    public void testIsHoliday() {
        WorkingDaySettings instance = WorkingDaySettings.newInstance();
        Calendar cal1 = new GregorianCalendar(2015, Calendar.JANUARY, 1, 14, 15, 16);

        Calendar cal2 = ClassicDateUtils.createCalendar(2015, 1, 2);
        assertThat(instance.isHoliday(cal1), is(false));
        assertThat(instance.isHoliday(cal2), is(false));

        WeekSettings newSettings = new WeekSettings(Calendar.SUNDAY, Calendar.FRIDAY, new int[]{Calendar.SATURDAY});
        instance.setWeekSettings(newSettings);
        assertThat(instance.isHoliday(cal1), is(false));
        assertThat(instance.isHoliday(cal2), is(true));

        instance.getHolidays().add(ClassicDateUtils.createCalendar(2015, 1, 1));
        assertThat(instance.isHoliday(cal1), is(true));
        assertThat(instance.isHoliday(cal2), is(true));
    }

    /**
     * Test of newInstance method, of class WorkingDaySettings.
     */
    @Test
    public void testNewInstance() {
        WorkingDaySettings actual = WorkingDaySettings.newInstance();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getExtractHolidays(), is(empty()));
        assertThat(actual.getHolidays(), is(empty()));
        assertThat(actual.getWeekSettings(), is(WeekSettings.DEFAULT));
        assertThat(actual.isShiftForward(), is(true));
    }

    /**
     * Test of setExtractHolidays method, of class WorkingDaySettings.
     */
    @Test
    public void testSetExtractHolidays() {
        Set<Calendar> extractHolidays = new HashSet<>();
        extractHolidays.add(ClassicDateUtils.createCalendar(2015, 1, 1));
        WorkingDaySettings instance = WorkingDaySettings.newInstance();
        instance.setExtractHolidays(extractHolidays);
        assertThat(instance.getExtractHolidays(), is(extractHolidays));
    }

    /**
     * Test of setHolidays method, of class WorkingDaySettings.
     */
    @Test
    public void testSetHolidays() {
        Set<Calendar> holidays = new HashSet<>();
        holidays.add(ClassicDateUtils.createCalendar(2015, 1, 1));
        WorkingDaySettings instance = WorkingDaySettings.newInstance();
        instance.setHolidays(holidays);
        assertThat(instance.getHolidays(), is(holidays));
    }

    /**
     * Test of setShiftForward method, of class WorkingDaySettings.
     */
    @Test
    public void testSetShiftForward() {
        WorkingDaySettings instance = WorkingDaySettings.newInstance();
        instance.setShiftForward(false);
        assertThat(instance.isShiftForward(), is(false));
    }

    /**
     * Test of setWeekSettings method, of class WorkingDaySettings.
     */
    @Test
    public void testSetWeekSettings() {
        WeekSettings weekSettings = new WeekSettings(Calendar.SUNDAY, Calendar.SATURDAY, new int[]{});
        WorkingDaySettings instance = WorkingDaySettings.newInstance();
        instance.setWeekSettings(weekSettings);
        assertThat(instance.getWeekSettings(), is(weekSettings));
    }

}
