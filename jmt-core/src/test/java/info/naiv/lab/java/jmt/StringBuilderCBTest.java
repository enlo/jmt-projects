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
package info.naiv.lab.java.jmt;

import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class StringBuilderCBTest {

    public StringBuilderCBTest() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_String_Null() {
        StringBuilderCB sb = new StringBuilderCB();
        String val = null;
        sb.append(val);
        assertThat(sb.length(), is(4));
        assertThat(sb.toString(), is("null"));
        sb.nullValue("[nil]");
        sb.append(val);
        assertThat(sb.length(), is(9));
        assertThat(sb.toString(), is("null[nil]"));
    }

    @Test
    public void testAppend_String_NonNull() {
        StringBuilderCB sb = new StringBuilderCB();
        sb.append("Hello").append(" ").append("World");
        assertThat(sb.length(), is("Hello World".length()));
        assertThat(sb.toString(), is("Hello World"));
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_CharSequence_Null() {
        CharSequence cs = null;
        StringBuilderCB sb = new StringBuilderCB();
        sb.append(cs);
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_CharSequence_NonNull() {
        CharSequence cs = new StringBuilder("hello.");
        StringBuilderCB sb = new StringBuilderCB();
        sb.append(cs);
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_3args() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_char() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_int() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_long() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_short() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_double() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_float() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_byte() {
    }

    /**
     * Test of append method, of class StringBuilderCB.
     */
    @Test
    public void testAppend_boolean() {
    }

    /**
     * Test of appendFormat method, of class StringBuilderCB.
     */
    @Test
    public void testAppendFormat_String_Object() {
    }

    /**
     * Test of appendFormat method, of class StringBuilderCB.
     */
    @Test
    public void testAppendFormat_3args() {
    }

    /**
     * Test of appendFormat method, of class StringBuilderCB.
     */
    @Test
    public void testAppendFormat_String_ObjectArr() {
    }

    /**
     * Test of charAt method, of class StringBuilderCB.
     */
    @Test
    public void testCharAt() {
    }

    /**
     * Test of length method, of class StringBuilderCB.
     */
    @Test
    public void testLength() {
    }

    /**
     * Test of nullValue method, of class StringBuilderCB.
     */
    @Test
    public void testNullValue() {
    }

    /**
     * Test of subSequence method, of class StringBuilderCB.
     */
    @Test
    public void testSubSequence() {
    }

    /**
     * Test of substring method, of class StringBuilderCB.
     */
    @Test
    public void testSubstring_int() {
    }

    /**
     * Test of substring method, of class StringBuilderCB.
     */
    @Test
    public void testSubstring_int_int() {
    }

    /**
     * Test of toString method, of class StringBuilderCB.
     */
    @Test
    public void testToString() {
    }

}
