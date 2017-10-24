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
package info.naiv.lab.java.jmt.text;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

/**
 *
 * @author enlo
 */
public class UnicodeScalarTest {

    /**
     *
     */
    public UnicodeScalarTest() {
    }

    /**
     * Test of charAt method, of class UnicodeScalar.
     */
    @Test
    public void testCharAt() {
        UnicodeScalar instance = new UnicodeScalar("\u0065\u0301");
        assertThat(instance.charAt(0), is('\u0065'));
        assertThat(instance.charAt(1), is('\u0301'));
    }

    /**
     * Test of clone method, of class UnicodeScalar.
     */
    @Test
    public void testClone() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        UnicodeScalar clone = str1.clone();
        assertThat(str1, is(not(sameInstance(clone))));
        assertThat(str1.toString(), is(clone.toString()));
    }

    /**
     * Test of compareTo method, of class UnicodeScalar.
     */
    @Test
    public void testCompareTo() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        UnicodeScalar str2 = new UnicodeScalar("\u00e9");
        assertThat(str1.compareTo(str2), is(0));
    }

    /**
     * Test of compareTo method, of class UnicodeScalar.
     */
    @Test
    public void testCompareTo_2() {
        UnicodeScalar str1 = new UnicodeScalar("A");
        UnicodeScalar str2 = new UnicodeScalar("B");
        assertThat(str1.compareTo(str2), is(lessThan(0)));
    }

    /**
     * Test of compareTo method, of class UnicodeScalar.
     */
    @Test
    public void testCompareTo_3() {
        UnicodeScalar str1 = new UnicodeScalar("い");
        UnicodeScalar str2 = new UnicodeScalar("あ");
        assertThat(str1.compareTo(str2), is(greaterThan(0)));
    }

    /**
     * Test of equals method, of class UnicodeScalar.
     */
    @Test
    public void testEquals() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        UnicodeScalar str2 = new UnicodeScalar("\u00e9");
        UnicodeScalar str3 = new UnicodeScalar("\u0065\u0301");
        assertThat(str1, is(not(str2)));
        assertThat(str1, is(str3));
    }

    /**
     * Test of getDecomposed method, of class UnicodeScalar.
     */
    @Test
    public void testGetDecomposed() {
        UnicodeScalar str1 = new UnicodeScalar("é");
        assertThat(str1.getDecomposed(), is("\u0065\u0301"));
    }

    /**
     * Test of getDecomposed method, of class UnicodeScalar.
     */
    @Test
    public void testGetDecomposed_2() {
        UnicodeScalar str1 = new UnicodeScalar(true, "é");
        assertThat(str1.getDecomposed(), is("é"));
    }

    /**
     * Test of getElement method, of class UnicodeScalar.
     */
    @Test
    public void testGetElement() {
        UnicodeScalar str1 = new UnicodeScalar("㋐");
        assertThat(str1.getElement(), is("\u32D0"));
    }

    /**
     * Test of hashCode method, of class UnicodeScalar.
     */
    @Test
    public void testHashCode() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        UnicodeScalar str2 = new UnicodeScalar("\u0065\u0301");
        assertThat(str1.hashCode(), is(str2.hashCode()));
    }

    /**
     * Test of length method, of class UnicodeScalar.
     */
    @Test
    public void testLength() {
        UnicodeScalar instance = new UnicodeScalar("\u0065\u0301");
        assertThat(instance.length(), is(2));
    }

    @Test
    public void testSerialize() {
        UnicodeScalar src = new UnicodeScalar("\u0065\u0301");
        byte[] bin = SerializationUtils.serialize(src);
        UnicodeScalar dest = (UnicodeScalar) SerializationUtils.deserialize(bin);
        assertThat(dest.getDecomposed(), is(src.getDecomposed()));
    }

    /**
     * Test of subSequence method, of class UnicodeScalar.
     */
    @Test
    public void testSubSequence() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        assertThat(str1.subSequence(0, 1), is((CharSequence) "\u0065"));
        assertThat(str1.subSequence(1, 2), is((CharSequence) "\u0301"));
    }

    /**
     * Test of toString method, of class UnicodeScalar.
     */
    @Test
    public void testToString() {
        UnicodeScalar str1 = new UnicodeScalar("\u0065\u0301");
        assertThat(str1.toString(), is("\u0065\u0301"));
    }

}
