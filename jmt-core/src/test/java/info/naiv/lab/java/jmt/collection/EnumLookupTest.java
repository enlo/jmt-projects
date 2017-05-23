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
package info.naiv.lab.java.jmt.collection;

import info.naiv.lab.java.jmt.IntegerEnum;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class EnumLookupTest {

    public EnumLookupTest() {
    }

    /**
     * Test of byIntegerEnum method, of class EnumLookup.
     */
    @Test
    public void testByIntegerEnum() {
        Lookup<Integer, TestE3> lookup = EnumLookup.byIntegerEnum(TestE3.class);

        assertThat(lookup.containsKey(1), is(true));
        assertThat(lookup.containsKey(2), is(true));
        assertThat(lookup.containsKey(3), is(true));
        assertThat(lookup.containsKey(4), is(false));

        assertThat(lookup.get(1), is(TestE3.A));
        assertThat(lookup.get(2), is(TestE3.B));
        assertThat(lookup.get(3), is(TestE3.C));
        assertThat(lookup.get(4), is(nullValue()));
    }

    /**
     * Test of byKeyedEnum method, of class EnumLookup.
     */
    @Test
    public void testByKeyedEnum() {
        Lookup<String, TestE2> lookup = EnumLookup.byKeyedEnum(TestE2.class);

        assertThat(lookup.containsKey("alpha"), is(true));
        assertThat(lookup.containsKey("beta"), is(true));
        assertThat(lookup.containsKey("gamma"), is(true));
        assertThat(lookup.containsKey("A"), is(false));
        assertThat(lookup.containsKey("Alpha"), is(false));

        assertThat(lookup.get("alpha"), is(TestE2.A));
        assertThat(lookup.get("beta"), is(TestE2.B));
        assertThat(lookup.get("gamma"), is(TestE2.C));
        assertThat(lookup.get("A"), is(nullValue()));
        assertThat(lookup.get("Alpha"), is(nullValue()));
    }

    /**
     * Test of byName method, of class EnumLookup.
     */
    @Test
    public void testByName() {
        Lookup<String, TestE1> lookup = EnumLookup.byName(TestE1.class);
        assertThat(lookup.containsKey("A"), is(true));
        assertThat(lookup.containsKey("B"), is(true));
        assertThat(lookup.containsKey("C"), is(true));
        assertThat(lookup.containsKey("D"), is(false));
        assertThat(lookup.containsKey("a"), is(false));

        assertThat(lookup.get("A"), is(TestE1.A));
        assertThat(lookup.get("B"), is(TestE1.B));
        assertThat(lookup.get("C"), is(TestE1.C));
        assertThat(lookup.get("D"), is(nullValue()));
        assertThat(lookup.get("a"), is(nullValue()));

    }

    public enum TestE1 {
        A, B, C;
    }

    public enum TestE2 implements KeyedValue<String> {

        A("alpha"), B("beta"), C("gamma");

        private final String key;

        private TestE2(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

    }

    public enum TestE3 implements IntegerEnum {

        A(1), B(2), C(3);

        private final int value;

        private TestE3(int value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public int getValue() {
            return value;
        }

    }

}
