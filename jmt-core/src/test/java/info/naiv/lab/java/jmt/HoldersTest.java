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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

/**
 *
 * @author enlo
 */
@RunWith(Theories.class)
public class HoldersTest {

    public static Holder holder;

    @DataPoints("as")
    public static Fixture[] asAsFixtures() {
        Fixture[] fixtures = new Fixture[]{
            new Fixture(null, holder, String.class),
            new Fixture(holder, holder, Number.class),
            new Fixture(holder, holder, Integer.class),
            new Fixture(null, holder, Long.class),};
        return fixtures;
    }

    @DataPoints("contentAs")
    public static Fixture[] asContentAsFixtures() {
        Fixture[] fixtures = new Fixture[]{
            new Fixture(null, holder, String.class),
            new Fixture(1, holder, Number.class),
            new Fixture(1, holder, Integer.class),
            new Fixture(null, holder, Long.class),};
        return fixtures;
    }

    @BeforeClass
    public static void setupClass() {
        holder = mock(Holder.class);
        Mockito.when(holder.getContent()).thenReturn(1);
        Mockito.when(holder.getContentType()).thenReturn(Integer.class);
    }

    public HoldersTest() {
    }

    /**
     * Test of as method, of class Holders.
     *
     * @param fix
     */
    @Theory
    public void testAs(@FromDataPoints("as") Fixture fix) {
        assertThat(Holders.as(fix.holder, fix.clazz), is(fix.expected));
    }

    /**
     * Test of getContentAs method, of class Holders.
     *
     * @param fix
     */
    @Theory
    public void testGetContentAs(@FromDataPoints("contentAs") Fixture fix) {
        assertThat(Holders.getContentAs(fix.holder, fix.clazz), is(fix.expected));
    }

    /**
     * Test of of method, of class Holders.
     */
    @Test
    public void testOf() {
        Holder<?> result = Holders.of(10);
        assertThat(result, is(not(nullValue())));
        assertThat(result.getContentType(), is((Object) Integer.class));
        assertThat(result.getContent(), is((Object) 10));
    }

    /**
     * Test of of method, of class Holders.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOf02() {
        Holders.of(null);
    }

    public static class Fixture {

        public Class<?> clazz;

        public Object expected;
        public Holder<?> holder;

        public Fixture(Object expected, Holder<?> holder, Class<?> clazz) {
            this.expected = expected;
            this.holder = holder;
            this.clazz = clazz;
        }
    }
}
