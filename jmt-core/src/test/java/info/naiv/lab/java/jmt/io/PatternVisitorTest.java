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
package info.naiv.lab.java.jmt.io;

import java.nio.file.Paths;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class PatternVisitorTest {

    /**
     *
     */
    public PatternVisitorTest() {
    }

    /**
     *
     */
    @Test
    public void testGlobMatcher() {

        PatternVisitor pv = new PatternVisitor("**.txt");
        assertThat(pv.matcher.matches(Paths.get("C:/temp/test.txt")), is(true));
        assertThat(pv.matcher.matches(Paths.get("C:/temp/test.pdf")), is(false));
        assertThat(pv.matcher.matches(Paths.get("test.txt")), is(true));
        assertThat(pv.matcher.matches(Paths.get("test.pdf")), is(false));
    }

    /**
     *
     */
    @Test
    public void testGlobMatcher_02() {

        PatternVisitor pv = new PatternVisitor("**");
        assertThat(pv.matcher.matches(Paths.get("C:/temp/test.txt")), is(true));
        assertThat(pv.matcher.matches(Paths.get("C:/temp/test.pdf")), is(true));
        assertThat(pv.matcher.matches(Paths.get("test.txt")), is(true));
        assertThat(pv.matcher.matches(Paths.get("test.pdf")), is(true));
    }
}
