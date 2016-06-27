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

import info.naiv.lab.java.jmt.monad.Optional;
import info.naiv.lab.java.jmt.monad.OptionalImpl;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.spy;

/**
 *
 * @author enlo
 */
public class TimeSpanTest {

    public TimeSpanTest() {
    }

    /**
     * Test of tryParse method, of class TimeSpan.
     */
    @Test
    public void testTryParse() {
        Optional<TimeSpan> ts1 = TimeSpan.tryParse("100sec");
        Optional<TimeSpan> ts2 = TimeSpan.tryParse("100meter");
        Optional<TimeSpan> ts3 = TimeSpan.tryParse("a100");
        Optional<TimeSpan> ts4 = TimeSpan.tryParse("");
        Optional<TimeSpan> ts5 = TimeSpan.tryParse(null);
        Optional<TimeSpan> ts6 = TimeSpan.tryParse("10");
        
        assertThat("ts1", ts1, is(OptionalImpl.of(new TimeSpan(100, TimeUnit.SECONDS))));
        assertThat("ts2", ts2, is(Optional.EMPTY));
        assertThat("ts3", ts3, is(Optional.EMPTY));
        assertThat("ts4", ts4, is(Optional.EMPTY));
        assertThat("ts5", ts5, is(Optional.EMPTY));
        assertThat("ts6", ts6, is(OptionalImpl.of(new TimeSpan(10, TimeUnit.MILLISECONDS))));
    }

    /**
     * Test of distance method, of class TimeSpan.
     */
    @Test
    public void testDistance() {
    }

    /**
     * Test of parse method, of class TimeSpan.
     */
    @Test
    public void testParse() throws Exception {
        TimeSpan ts1 = TimeSpan.parse("0");
        TimeSpan ts2 = TimeSpan.parse("1");
        TimeSpan ts3 = TimeSpan.parse("-1");
        TimeSpan ts4 = TimeSpan.parse("+1");
        TimeSpan ts5 = TimeSpan.parse("100");
        TimeSpan ts6 = TimeSpan.parse("100000000000");
        
        assertThat("ts1", ts1, is(new TimeSpan(0, TimeUnit.MILLISECONDS)));
        assertThat("ts2", ts2, is(new TimeSpan(1, TimeUnit.MILLISECONDS)));
        assertThat("ts3", ts3, is(new TimeSpan(-1, TimeUnit.MILLISECONDS)));
        assertThat("ts4", ts4, is(new TimeSpan(1, TimeUnit.MILLISECONDS)));
        assertThat("ts5", ts5, is(new TimeSpan(100, TimeUnit.MILLISECONDS)));
        assertThat("ts6", ts6, is(new TimeSpan(100000000000L, TimeUnit.MILLISECONDS)));

    }

    /**
     * Test of parse method, of class TimeSpan.
     */
    @Test
    public void testParseNanos() throws Exception {

        TimeSpan ts1 = TimeSpan.parse("50n");
        TimeSpan ts2 = TimeSpan.parse("100ns");
        TimeSpan ts3 = TimeSpan.parse("120Ns");
        TimeSpan ts4 = TimeSpan.parse("1nano");
        TimeSpan ts5 = TimeSpan.parse("2nanos");
        TimeSpan ts6 = TimeSpan.parse("+1000nanosec");
        TimeSpan ts7 = TimeSpan.parse("-1234NanoSecond");
        TimeSpan ts8 = TimeSpan.parse("20000000000NanoSeconds");

        assertThat("ts1", ts1, is(new TimeSpan(50, TimeUnit.NANOSECONDS)));
        assertThat("ts2", ts2, is(new TimeSpan(100, TimeUnit.NANOSECONDS)));
        assertThat("ts3", ts3, is(new TimeSpan(120, TimeUnit.NANOSECONDS)));
        assertThat("ts4", ts4, is(new TimeSpan(1, TimeUnit.NANOSECONDS)));
        assertThat("ts5", ts5, is(new TimeSpan(2, TimeUnit.NANOSECONDS)));
        assertThat("ts6", ts6, is(new TimeSpan(1000, TimeUnit.NANOSECONDS)));
        assertThat("ts7", ts7, is(new TimeSpan(-1234, TimeUnit.NANOSECONDS)));
        assertThat("ts8", ts8, is(new TimeSpan(20000000000L, TimeUnit.NANOSECONDS)));
    }

    /**
     * Test of parse method, of class TimeSpan.
     */
    @Test
    public void testParseMills() throws Exception {

        TimeSpan ts1 = TimeSpan.parse("50ms");
        TimeSpan ts2 = TimeSpan.parse("120MS");
        TimeSpan ts3 = TimeSpan.parse("1mil");
        TimeSpan ts4 = TimeSpan.parse("2MILLI");
        TimeSpan ts5 = TimeSpan.parse("+1000Millis");
        TimeSpan ts6 = TimeSpan.parse("000Millisec");
        TimeSpan ts7 = TimeSpan.parse("-1234milliSecond");
        TimeSpan ts8 = TimeSpan.parse("20000000000milliSeconds");

        assertThat("ts1", ts1, is(new TimeSpan(50, TimeUnit.MILLISECONDS)));
        assertThat("ts2", ts2, is(new TimeSpan(120, TimeUnit.MILLISECONDS)));
        assertThat("ts3", ts3, is(new TimeSpan(1, TimeUnit.MILLISECONDS)));
        assertThat("ts4", ts4, is(new TimeSpan(2, TimeUnit.MILLISECONDS)));
        assertThat("ts5", ts5, is(new TimeSpan(1000, TimeUnit.MILLISECONDS)));
        assertThat("ts6", ts6, is(new TimeSpan(0, TimeUnit.MILLISECONDS)));
        assertThat("ts7", ts7, is(new TimeSpan(-1234, TimeUnit.MILLISECONDS)));
        assertThat("ts8", ts8, is(new TimeSpan(20000000000L, TimeUnit.MILLISECONDS)));
    }

    /**
     * Test of compareTo method, of class TimeSpan.
     */
    @Test
    public void testCompareTo() {
    }

    /**
     * Test of toMillis method, of class TimeSpan.
     */
    @Test
    public void testToMillis() {
    }

    /**
     * Test of newTime method, of class TimeSpan.
     */
    @Test
    public void testNewTime() {
    }

    /**
     * Test of newUnit method, of class TimeSpan.
     */
    @Test
    public void testNewUnit() {
    }

    /**
     * Test of getTime method, of class TimeSpan.
     */
    @Test
    public void testGetTime() {
    }

    /**
     * Test of getUnit method, of class TimeSpan.
     */
    @Test
    public void testGetUnit() {
    }

    /**
     * Test of equals method, of class TimeSpan.
     */
    @Test
    public void testEquals() {
    }

    /**
     * Test of hashCode method, of class TimeSpan.
     */
    @Test
    public void testHashCode() {
    }

    /**
     * Test of toString method, of class TimeSpan.
     */
    @Test
    public void testToString() {
    }

}
