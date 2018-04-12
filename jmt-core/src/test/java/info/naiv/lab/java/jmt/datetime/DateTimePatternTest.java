/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.datetime.DateTimePattern.DATETIME_PATTERN;
import static info.naiv.lab.java.jmt.datetime.DateTimePattern.DATE_PATTERN;
import static info.naiv.lab.java.jmt.datetime.DateTimePattern.DATE_PATTERN_SY;
import static info.naiv.lab.java.jmt.datetime.DateTimePattern.TIME_PATTERN;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 *
 * @author enlo
 */
public class DateTimePatternTest {

    CurrentDateProvider currentDateProvider;
    Calendar testCalendar;

    /**
     *
     */
    @Before
    public void setUp() {

        ServiceProviders.setThreadContainer(null);

        testCalendar = new GregorianCalendar(2015, Calendar.MAY, 14);
        currentDateProvider = mock(CurrentDateProvider.class);
        when(currentDateProvider.getNow()).thenReturn(testCalendar);
        when(currentDateProvider.getToday()).thenReturn(DateUtils.truncate(testCalendar, Calendar.DAY_OF_MONTH));

        ServiceProviders.getThreadContainer().registerService(currentDateProvider);

    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     * @throws ParseException
     */
    @Test
    public void testToCalendar() throws ParseException {
        String source = "2015-12-24T12:12:47";
        Calendar out = Calendar.getInstance();
        boolean result = DATETIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_1() {
        String source = "2014-12-11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_2() {
        String source = "2014/12/11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_3() {
        String source = "2014.12.11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_4() {
        String source = "910/2/7";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(910, 2, 7)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_5() {
        String source = "10912.12.17";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(10912, 12, 17)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_6() {
        String source = "10912.112.7";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_7() {
        String source = "10912.12.147";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_8() {
        String source = "12.12.17";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_SY_1() {
        String source = "14-12-11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_SY_2() {
        String source = "14/12/11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_DATE_PATTERN_SY_3() {
        String source = "14.12.11";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(2014, 12, 11)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_SY_4() {
        String source = "91/2/7";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(1991, 2, 7)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_SY_5() {
        String source = "92.12.17";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(ClassicDateUtils.createDate(1992, 12, 17)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_SY_6() {
        String source = "12.112.7";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_SY_7() {
        String source = "12.12.147";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_DATE_PATTERN_SY_8() {
        String source = "912.12.17";
        Calendar out = Calendar.getInstance();
        boolean result = DATE_PATTERN_SY.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_TIME_PATTERN_1() {
        String source = "14:12:11";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(timeOf(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_TIME_PATTERN_2() {
        String source = "14:12:11.234";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(timeOf(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_TIME_PATTERN_3() {
        String source = "22:12";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(timeOf(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test
    public void testToCalendar_TIME_PATTERN_4() {
        String source = "11:12:44+09:00";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(timeOf(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_TIME_PATTERN_5() {
        String source = "11:12:33.123+09:00";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(true));
        assertThat(out.getTime(), is(timeOf(source)));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_TIME_PATTERN_6() {
        String source = "12:112:07";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_TIME_PATTERN_7() {
        String source = "12:12:147";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     * Test of toCalendar method, of class DateTimePattern.
     */
    @Test()
    public void testToCalendar_TIME_PATTERN_8() {
        String source = "112:12:47";
        Calendar out = Calendar.getInstance();
        boolean result = TIME_PATTERN.toCalendar(source, out);
        assertThat(result, is(false));
    }

    /**
     *
     * @param time
     * @return
     */
    public Date timeOf(String time) {
        try {
            return DateUtils.parseDate(time, "HH:mm:ss.SSSZZ", "HH:mm:ss.SSS", "HH:mm:ssZZ", "HH:mm:ss", "HH:mm");
        }
        catch (ParseException ex) {
            throw new InternalError(ex.getMessage());
        }
    }
}
