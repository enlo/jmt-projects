/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.net.http;

import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class HttpHeaderElementTest {

    public HttpHeaderElementTest() {
    }

    /**
     * Test of parse method, of class HttpHeaderElement.
     */
    @Test
    public void testParse01() {

        String source = "Content-Disposition: attachment";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.anEmptyMap()));
    }

    @Test
    public void testParse02() {

        String source = "Content-Disposition: attachment; filename=AAA.txt";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.hasEntry("filename", "AAA.txt")));
    }

    @Test
    public void testParse03() {

        String source = "Content-Disposition: attachment; filename=\"ABCD.doc\"";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.hasEntry("filename", "ABCD.doc")));
    }

    @Test
    public void testParse04() {

        String source = "Content-Disposition: attachment;"
                + " filename*=UTF-8''%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E5%90%8D.doc;"
                + " filename=\"ABCD.doc\"";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.hasEntry("filename", "ファイル名.doc")));
    }

    @Test
    public void testParse05() {

        String source = "Content-Disposition: attachment;"
                + " filename*=UTF-8'ja_JP'%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E5%90%8D.doc;"
                + " filename=\"ABCD.doc\"";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.hasEntry("filename", "ファイル名.doc")));
    }

    @Test
    public void testParse06() {

        String source = "Content-Disposition: attachment;"
                + " filename*0*=UTF-8'ja_JP';"
                + " filename*1*=%E3;"
                + " filename*2*=%83;"
                + " filename*3*=%95;"
                + " filename*4*=%E3;"
                + " filename*5*=%82;"
                + " filename*6*=%A1;"
                + " filename*12*=.doc;"
                + " filename*7*=%E3;"
                + " filename*8*=%82;"
                + " filename*9*=%A4;"
                + " filename*10*=%E3%83;"
                + " filename*11*=%AB%E5%90%8D;"
                + " filename=\"ABCD.doc\"";
        HttpHeaderElement actual = HttpHeaderElement.parse(source);
        assertThat(actual.getHeaderName(), is("Content-Disposition"));
        assertThat(actual.getHeaderValue(), is("attachment"));
        assertThat(actual.getParameters(), is(Matchers.hasEntry("filename", "ファイル名.doc")));
    }

}
