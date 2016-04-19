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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class DateTimeMaskTest {

    /**
     *
     */
    @Test
    public void testDATE_MASK_between() {
        Calendar x1 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 19);
        Calendar x2 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar x3 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 21);
        Calendar x4 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 19);
        Calendar x5 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        Calendar x6 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 21);
        Calendar x7 = ClassicDateUtils.createCalendar(2015, 1, 20, 20, 19, 20);
        Calendar from = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar to = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        assertThat(DateTimeMask.DATE_MASK.between(x1, from, to), is(false));
        assertThat(DateTimeMask.DATE_MASK.between(x2, from, to), is(true));
        assertThat(DateTimeMask.DATE_MASK.between(x3, from, to), is(false));
        assertThat(DateTimeMask.DATE_MASK.between(x4, from, to), is(false));
        assertThat(DateTimeMask.DATE_MASK.between(x5, from, to), is(true));
        assertThat(DateTimeMask.DATE_MASK.between(x6, from, to), is(false));
        assertThat(DateTimeMask.DATE_MASK.between(x7, from, to), is(true));
    }

    /**
     *
     */
    @Test
    public void testDATE_MASK_mask() {
        Calendar in = ClassicDateUtils.createCalendar(2015, 1, 17, 18, 19, 20);
        Calendar expected = ClassicDateUtils.createCalendar(1970, 1, 1, 18, 19, 20);
        assertThat(DateTimeMask.DATE_MASK.mask(in), is(expected));
    }

    /**
     *
     */
    @Test
    public void testNO_MASK_between() {
        Calendar x1 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 19);
        Calendar x2 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar x3 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 21);
        Calendar x4 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 19);
        Calendar x5 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        Calendar x6 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 21);
        Calendar x7 = ClassicDateUtils.createCalendar(2015, 1, 20, 20, 19, 20);
        Calendar from = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar to = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        assertThat(DateTimeMask.NO_MASK.between(x1, from, to), is(false));
        assertThat(DateTimeMask.NO_MASK.between(x2, from, to), is(true));
        assertThat(DateTimeMask.NO_MASK.between(x3, from, to), is(true));
        assertThat(DateTimeMask.NO_MASK.between(x4, from, to), is(true));
        assertThat(DateTimeMask.NO_MASK.between(x5, from, to), is(true));
        assertThat(DateTimeMask.NO_MASK.between(x6, from, to), is(false));
        assertThat(DateTimeMask.NO_MASK.between(x7, from, to), is(false));
    }

    /**
     *
     */
    @Test
    public void testNO_MASK_mask() {
        Calendar in = ClassicDateUtils.createCalendar(2015, 1, 17, 18, 19, 20);
        Calendar expected = ClassicDateUtils.createCalendar(2015, 1, 17, 18, 19, 20);
        assertThat(DateTimeMask.NO_MASK.mask(in), is(expected));
    }

    /**
     *
     */
    @Test
    public void testTIME_MASK_between() {
        Calendar x1 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 19);
        Calendar x2 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar x3 = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 21);
        Calendar x4 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 19);
        Calendar x5 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        Calendar x6 = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 21);
        Calendar x7 = ClassicDateUtils.createCalendar(2015, 1, 20, 20, 19, 20);
        Calendar from = ClassicDateUtils.createCalendar(2015, 1, 17, 20, 19, 20);
        Calendar to = ClassicDateUtils.createCalendar(2015, 1, 18, 20, 19, 20);
        assertThat(DateTimeMask.TIME_MASK.between(x1, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x2, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x3, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x4, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x5, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x6, from, to), is(true));
        assertThat(DateTimeMask.TIME_MASK.between(x7, from, to), is(false));
    }

    /**
     *
     */
    @Test
    public void testTIME_MASK_mask() {
        Calendar in = ClassicDateUtils.createCalendar(2015, 1, 17, 18, 19, 20);
        Calendar expected = ClassicDateUtils.createCalendar(2015, 1, 17, 0, 0, 0);
        assertThat(DateTimeMask.TIME_MASK.mask(in), is(expected));
    }
}
