/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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
package info.naiv.lab.java.jmt.text;

import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class CsvStringStreamTest {

    public CsvStringStreamTest() {
    }

    /**
     * Test of skipColumn method, of class CsvStringStream.
     */
    @Test
    public void testSkipColumn() {
        String sampleCsv = "\"ABC\",CDF, E F G  ,\" A B \r\n C\"\"\"";
        CsvStringStream css = new CsvStringStream(sampleCsv);
        assertThat(css.skipColumn(), is(true));
        assertThat(css.index, is(6));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.index, is(10));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.index, is(19));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.index, is(32));
        assertThat(css.skipColumn(), is(false));
        assertThat(css.index, is(32));
    }

    /**
     * Test of getColumn method, of class CsvStringStream.
     */
    @Test
    public void testGetColumn() {
        String sampleCsv = "\"ABC\",CDF, E F G  ,\" A B, \r\n C\"\"\"";
        CsvStringStream css = new CsvStringStream(sampleCsv);
        assertThat(css.getColumn(), is("ABC"));
        assertThat(css.getColumn(), is("CDF"));
        assertThat(css.getColumn(), is(" E F G  "));
        assertThat(css.getColumn(), is(" A B, \r\n C\""));
        assertThat(css.getColumn(), is(""));
    }

    /**
     * Test of getRemaining method, of class CsvStringStream.
     */
    @Test
    public void testGetRemaining() {
        String sampleCsv = "\"ABC\",CDF, E F G  ,\" A B, \r\n C\"\"\"";
        CsvStringStream css = new CsvStringStream(sampleCsv);
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getRemaining(), is("CDF, E F G  ,\" A B, \r\n C\"\"\""));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getRemaining(), is(" E F G  ,\" A B, \r\n C\"\"\""));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getRemaining(), is("\" A B, \r\n C\"\"\""));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getRemaining(), is(""));
        assertThat(css.skipColumn(), is(false));
        assertThat(css.getRemaining(), is(""));
    }

    /**
     * Test of nextLine method, of class CsvStringStream.
     */
    @Test
    public void testNextLine() {
        String sampleCsv = "\"ABC\",CDF, E F G  ,\" A B, \r\n C\"\"\"\n"
                + "123,456,789,ABC\r\n"
                + "あいう,えおか,きくけ,さしす\r"
                + "漢字,かな,カタカナ,\u0208";

        CsvStringStream css = new CsvStringStream(sampleCsv);
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getColumn(), is("CDF"));
        assertThat(css.skipColumn(), is(true));
        assertThat(css.getColumn(), is(" A B, \r\n C\""));
        assertThat(css.getRemaining(), is("\n"
                   + "123,456,789,ABC\r\n"
                   + "あいう,えおか,きくけ,さしす\r"
                   + "漢字,かな,カタカナ,\u0208"));
        assertThat(css.nextLine(), is(true));
        assertThat(css.getRemaining(), is("123,456,789,ABC\r\n"
                   + "あいう,えおか,きくけ,さしす\r"
                   + "漢字,かな,カタカナ,\u0208"));
        assertThat(css.nextLine(), is(true));
        assertThat(css.getRemaining(), is("あいう,えおか,きくけ,さしす\r"
                   + "漢字,かな,カタカナ,\u0208"));
        assertThat(css.nextLine(), is(true));
        assertThat(css.getRemaining(), is("漢字,かな,カタカナ,\u0208"));
        assertThat(css.nextLine(), is(false));
        assertThat(css.getRemaining(), is(""));
    }

}
