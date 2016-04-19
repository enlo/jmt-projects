/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class StringJoinerTest extends AbstractStringBuilderJoinerTest<String> {

    /**
     *
     */
    public StringJoinerTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    @Override
    public void testJoin_GenericType() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        String[] strs = {"a", "b", "c"};
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,b,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_GenericType_2() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        String[] strs = {"a", null, "c"};
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_GenericType_3() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        String[] strs = {"a", "", "c"};
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_GenericType_4() {
        StringJoiner joiner = new StringJoiner();
        String[] strs = {"a", null, "c"};
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("ac"));
    }

    @Override
    public void testJoin_Iterable() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        List<String> strs = Arrays.asList("a", "b", "c");
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,b,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_Iterable_2() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        List<String> strs = Arrays.asList("a", null, "c");
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_Iterable_3() {
        StringJoiner joiner = StringJoiner.valueOf(",");
        List<String> strs = Arrays.asList("a", "", "c");
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("a,,c"));
    }

    /**
     *
     */
    @Test
    public void testJoin_Iterable_4() {
        StringJoiner joiner = new StringJoiner();
        List<String> strs = Arrays.asList("a", null, "c");
        String actual = joiner.join(strs).toString();
        assertThat(actual, is("ac"));
    }
}
