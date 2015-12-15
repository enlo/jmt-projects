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
import org.apache.commons.lang3.ArrayUtils;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class WeekSettingsTest {


    WeekSettings weekSettings;

    public WeekSettingsTest() {
    }

    @Before
    public void setUp() {
        weekSettings = new WeekSettings(Calendar.MONDAY, Calendar.WEDNESDAY, new int[]{Calendar.FRIDAY});
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFirstDayOfWeek method, of class WeekSettings.
     */
    @Test
    public void testGetFirstDayOfWeek() {
        assertThat(weekSettings.getFirstDayOfWeek(), is(Calendar.MONDAY));
    }

    /**
     * Test of getOfficialHoliday method, of class WeekSettings.
     */
    @Test
    public void testGetOfficialHoliday() {
        assertThat(weekSettings.getOfficialHoliday(), is(Calendar.WEDNESDAY));
    }

    /**
     * Test of getUnofficialHolidays method, of class WeekSettings.
     */
    @Test
    public void testGetUnofficialHolidays() {
        int[] actual = weekSettings.getUnofficialHolidays();
        assertThat((Integer[]) ArrayUtils.toObject(actual), is(arrayContaining(Calendar.FRIDAY)));
    }

    /**
     * Test of isHoliday method, of class WeekSettings.
     */
    @Test
    public void testIsHoliday() {
        WeekSettings newFirstDayOfWeek = weekSettings.newFirstDayOfWeek(Calendar.SUNDAY);
        assertThat(weekSettings.isHoliday(Calendar.SUNDAY), is(false));
        assertThat(weekSettings.isHoliday(Calendar.MONDAY), is(false));
        assertThat(weekSettings.isHoliday(Calendar.TUESDAY), is(false));
        assertThat(weekSettings.isHoliday(Calendar.WEDNESDAY), is(true));
        assertThat(weekSettings.isHoliday(Calendar.THURSDAY), is(false));
        assertThat(weekSettings.isHoliday(Calendar.FRIDAY), is(true));
        assertThat(weekSettings.isHoliday(Calendar.SATURDAY), is(false));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.SUNDAY), is(false));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.MONDAY), is(false));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.TUESDAY), is(false));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.WEDNESDAY), is(true));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.THURSDAY), is(false));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.FRIDAY), is(true));
        assertThat(newFirstDayOfWeek.isHoliday(Calendar.SATURDAY), is(false));
    }

    /**
     * Test of newFirstDayOfWeek method, of class WeekSettings.
     */
    @Test
    public void testNewFirstDayOfWeek() {
        WeekSettings newFirstDayOfWeek = weekSettings.newFirstDayOfWeek(Calendar.SUNDAY);
        assertThat(newFirstDayOfWeek.getFirstDayOfWeek(), is(Calendar.SUNDAY));
        assertThat(newFirstDayOfWeek.getOfficialHoliday(), is(Calendar.WEDNESDAY));
        int[] actual = newFirstDayOfWeek.getUnofficialHolidays();
        assertThat((Integer[]) ArrayUtils.toObject(actual), is(arrayContaining(Calendar.FRIDAY)));
    }
    

}
