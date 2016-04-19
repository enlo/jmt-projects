/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt.datetime;

import java.util.Calendar;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class DefaultShortYearConverterTest {

    private Calendar baseDate;
    private ShortYearConverter instance;

    /**
     *
     */
    public DefaultShortYearConverterTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        baseDate = Calendar.getInstance();
        baseDate.set(2014, Calendar.APRIL, 15);
        instance = new DefaultShortYearConverter();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of convert method, of class DefaultShortYearConverter.
     */
    @Test
    public void testConvert_1() {

        int values[][] = {
            {0, 2000},
            {13, 2013},
            {14, 2014},
            {15, 2015},
            {34, 2034},
            {35, 1935},
            {50, 1950},
            {99, 1999},
            {100, 100}
        };

        for (int[] value : values) {
            int result = instance.convert(value[0], baseDate);
            assertThat(result, is(value[1]));
        }

    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_2() {
        instance.convert(-1, baseDate);
    }
}
