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

import java.io.ByteArrayOutputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Date;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author enlo
 */
public class HttpHeadersTest {

    HttpHeaders headers;

    public HttpHeadersTest() {
    }

    @Before
    public void setUp() {
        headers = new HttpHeaders();
    }

    /**
     * Test of setBuilder method, of class HttpHeaders.
     */
    @Test
    public void testSetBuilder() {

        assertThat(headers, is(empty()));

        HttpHeaderBuilder org = new HttpHeaderBuilder("X-Test");
        org.headerValue("test");

        HttpHeaderBuilder hb = spy(org);

        assertThat(headers.setBuilder(hb), is(sameInstance(hb)));
        verify(hb, times(1)).build();

        assertThat(hb.headers, is(sameInstance(headers)));
        assertThat(headers, is(contains(new HttpHeader("X-Test", "test"))));
    }

    /**
     * Test of setContentDisposition method, of class HttpHeaders.
     */
    @Test
    public void testSetContentDisposition_3args() {

        HttpHeaderBuilder hb = headers.setContentDisposition(ContentDispositionType.ATTACHMENT, "ABC", "test.txt");

        HttpHeader expected = new HttpHeader("Content-Disposition", "attachment;name=\"ABC\";filename=\"test.txt\"");
        assertThat(headers, is(contains(expected)));

        hb.parameter("filename", "フ ァイル名.doc");
        hb.update();
        expected = new HttpHeader("Content-Disposition", "attachment;name=\"ABC\";filename=\"フ ァイル名.doc\";filename*=UTF-8''%E3%83%95%20%E3%82%A1%E3%82%A4%E3%83%AB%E5%90%8D.doc");
        assertThat(headers, is(contains(expected)));
    }

    /**
     * Test of setContentDisposition method, of class HttpHeaders.
     */
    @Test
    public void testSetContentDisposition_3args_2() {

        HttpHeaderBuilder hb = headers.setContentDisposition(ContentDispositionType.FORMDATA, "ABC", "フ ァイル名.doc");

        HttpHeader expected = new HttpHeader("Content-Disposition", "form-data;name=\"ABC\";filename=\"フ ァイル名.doc\";filename*=UTF-8''%E3%83%95+%E3%82%A1%E3%82%A4%E3%83%AB%E5%90%8D.doc");
        assertThat(headers, is(contains(expected)));
    }

    /**
     * Test of setContentDisposition method, of class HttpHeaders.
     */
    @Test
    public void testSetContentDisposition_ContentDispositionType_String() {

        HttpHeaderBuilder hb = headers.setContentDisposition(ContentDispositionType.INLINE, "ABC");

        HttpHeader expected = new HttpHeader("Content-Disposition", "inline;name=\"ABC\"");
        assertThat(headers, is(contains(expected)));

        hb.parameter("filename", "test.txt");
        hb.update();
        expected = new HttpHeader("Content-Disposition", "inline;name=\"ABC\";filename=\"test.txt\"");
        assertThat(headers, is(contains(expected)));
    }

    /**
     * Test of setContentLength method, of class HttpHeaders.
     */
    @Test
    public void testSetContentLength() {

        headers.setContentLength(100);

        assertThat(headers, is(contains(new HttpHeader("Content-Length", "100"))));
    }

    /**
     * Test of setContentTransferEncoding method, of class HttpHeaders.
     */
    @Test
    public void testSetContentTransferEncoding() {
        headers.setContentTransferEncoding(ContentTransferEncodingType.BIT8);
        assertThat(headers, is(contains(new HttpHeader("Content-Transfer-Encoding", "8bit"))));

        headers.setContentTransferEncoding(ContentTransferEncodingType.BINARY);
        assertThat(headers, hasSize(1));
        assertThat(headers, is(contains(new HttpHeader("Content-Transfer-Encoding", "binary"))));
    }

    /**
     * Test of setContentType method, of class HttpHeaders.
     */
    @Test
    public void testSetContentType_String() {
        headers.setContentType("application/xml");
        assertThat(headers, is(contains(new HttpHeader("Content-Type", "application/xml"))));
    }

    /**
     * Test of setContentType method, of class HttpHeaders.
     */
    @Test
    public void testSetContentType_String_Charset() {
        headers.setContentType("application/xml", UTF_8);
        assertThat(headers, is(contains(new HttpHeader("Content-Type", "application/xml;charset=\"UTF-8\""))));
    }

    /**
     * Test of setHeader method, of class HttpHeaders.
     */
    @Test
    public void testSetHeader_HttpHeaderElement() {
        String date = (new Date()).toString();
        HttpHeaderElement e = new HttpHeaderElement("Last-Modified", date);

        headers.setHeader(e);
        assertThat(headers, is(contains(new HttpHeader("Last-Modified", date))));
    }

    /**
     * Test of setHeader method, of class HttpHeaders.
     */
    @Test
    public void testSetHeader_String_String() {
        headers.setHeader("ETag", "12345678");
        assertThat(headers, is(contains(new HttpHeader("ETag", "12345678"))));
    }

    /**
     * Test of writeTo method, of class HttpHeaders.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteTo() throws Exception {

        headers.setContentType("text/csv");
        headers.setContentDisposition(ContentDispositionType.INLINE, null, "Sample.csv");
        headers.setContentLength(120);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        headers.writeTo(baos, UTF_8);

        String actual = new String(baos.toByteArray(), UTF_8);
        String expected = "Content-Type: text/csv\r\n"
                + "Content-Disposition: inline;filename=\"Sample.csv\"\r\n"
                + "Content-Length: 120\r\n";
        assertThat(actual, is(expected));
    }

}
