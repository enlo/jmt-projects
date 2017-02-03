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
package info.naiv.lab.java.jmt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import org.springframework.util.StreamUtils;

/**
 *
 * @author enlo
 */
public class SizedBytesTest {

    public SizedBytesTest() {
    }

    byte[] source;
    byte[] sliced;

    @Before
    public void setUp() {
        source = "あいうえおABC漢字".getBytes();
        sliced = Arrays.copyOf(source, 10);
    }

    /**
     * Test of newInputStream method, of class SizedBytes.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testNewInputStream() throws IOException {
        SizedBytes sz1 = new SizedBytes(source);
        ByteArrayInputStream bais1 = sz1.newInputStream();
        assertThat(StreamUtils.copyToByteArray(bais1), is(source));

        SizedBytes sz2 = new SizedBytes(10, source);
        ByteArrayInputStream bais2 = sz2.newInputStream();
        assertThat(StreamUtils.copyToByteArray(bais2), is(sliced));
    }

    /**
     * Test of toByteArray method, of class SizedBytes.
     */
    @Test
    public void testToByteArray() {
        SizedBytes sz1 = new SizedBytes(source);
        byte[] actual1 = sz1.toByteArray();
        assertThat(actual1, is(not(sameInstance(source))));
        assertThat(actual1, is(source));

        SizedBytes sz2 = new SizedBytes(10, source);
        byte[] actual2 = sz2.toByteArray();
        assertThat(actual2, is(sliced));
    }

    /**
     * Test of clone method, of class SizedBytes.
     */
    @Test
    public void testClone() {
        SizedBytes sz = new SizedBytes(source);
        SizedBytes actual = sz.clone();
        assertThat(actual, is(not(sameInstance(sz))));
        assertThat(actual, is(sz));
    }

    /**
     * Test of getData method, of class SizedBytes.
     */
    @Test
    public void testGetData() {
        SizedBytes sz1 = new SizedBytes(source);
        byte[] actual1 = sz1.getData();
        assertThat(actual1, is(sameInstance(source)));

        SizedBytes sz2 = new SizedBytes(10, source);
        byte[] actual2 = sz2.getData();
        assertThat(actual2, is(sameInstance(source)));
    }

    /**
     * Test of getSize method, of class SizedBytes.
     */
    @Test
    public void testGetSize() {
        SizedBytes sz1 = new SizedBytes(source);
        assertThat(sz1.getSize(), is(source.length));

        SizedBytes sz2 = new SizedBytes(10, source);
        assertThat(sz2.getSize(), is(10));
    }

}
