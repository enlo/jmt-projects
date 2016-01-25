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

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class RopeTest {

    public RopeTest() {
    }

    /**
     * Test of compareNormalized method, of class Rope.
     */
    @Test
    public void testCompareNormalized_Rope() {
        Rope left1 = new Rope("あいうえお");
        Rope left2 = new Rope("か\u3099");
        Rope right1 = new Rope("あいうえ");
        Rope right2 = new Rope("あいうえお");
        Rope right3 = new Rope("あいうえおか");
        Rope right4 = new Rope("が");
        assertThat(left1.compareTo(right1) > 0, is(true));
        assertThat(left1.compareNormalized(right1) > 0, is(true));
        assertThat(left1.compareNormalized(right2) == 0, is(true));
        assertThat(left1.compareNormalized(right3) < 0, is(true));
        assertThat(left2.compareNormalized(right4) == 0, is(true));
    }

    /**
     * Test of compareNormalized method, of class Rope.
     */
    @Test
    public void testCompareNormalized_String_String() {
        String left1 = "あいうえお";
        String left2 = "か\u3099";
        String right1 = "あいうえ";
        String right2 = "あいうえお";
        String right3 = "あいうえおか";
        String right4 = "が";
        assertThat(Rope.compareNormalized(left1, right1) > 0, is(true));
        assertThat(Rope.compareNormalized(left1, right2) == 0, is(true));
        assertThat(Rope.compareNormalized(left1, right3) < 0, is(true));
        assertThat(Rope.compareNormalized(left2, right4) == 0, is(true));
    }

    /**
     * Test of compareTo method, of class Rope.
     */
    @Test
    public void testCompareTo() {
        String[] source = {"ABC", "abc", "cdef", "1234"};
        for (String s : source) {
            Rope l = Rope.valueOf(s);
            Rope k = Rope.valueOf("abc");
            assertThat(s, l.compareTo(k), is(s.compareTo("abc")));
        }
    }

    /**
     * Test of contains method, of class Rope.
     */
    @Test
    public void testContains_Rope() {
        Rope str = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");

        assertThat(str.contains(Rope.valueOf("が")), is(true));
        assertThat(str.contains(Rope.valueOf("か\u3099")), is(true));
        assertThat(str.contains(Rope.valueOf("が美")), is(true));
        assertThat(str.contains(Rope.valueOf("が馬")), is(false));
    }

    /**
     * Test of contains method, of class Rope.
     */
    @Test
    public void testContains_String() {
        Rope str = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.contains("が"), is(true));
        assertThat(str.contains("か\u3099"), is(true));
        assertThat(str.contains("が美"), is(true));
        assertThat(str.contains("が馬"), is(false));
    }

    /**
     * Test of decompose method, of class Rope.
     */
    @Test
    public void testDecompose() {
        Rope expected = new Rope("か\u3099");
        Rope source = new Rope("が");
        Rope actual = source.decompose();
        assertThat(actual, is(expected));
    }

    /**
     * decomposeテスト2<br>
     * 一度 decompose したら、あとは自分自身を戻す.
     */
    @Test
    public void testDecompose2() {
        Rope source = new Rope("が");
        Rope expected = source.decompose();
        Rope actual = expected.decompose();
        assertThat(actual, is(sameInstance(expected)));
    }

    /**
     * Test of endsWith method, of class Rope.
     */
    @Test
    public void testEndsWith() {
        Rope left = new Rope("あいうえお");
        String right1 = ("いうえお");
        String right2 = ("あいうえお");
        String right3 = ("ああいうえお");
        String right4 = ("あいう");
        assertThat(left.endsWith(right1, 0), is(true));
        assertThat(left.endsWith(right2, 0), is(true));
        assertThat(left.endsWith(right3, 0), is(false));
        assertThat(left.endsWith(right4, 0), is(false));
        assertThat(left.endsWith(right4, 2), is(true));
    }

    /**
     * Test of endsWith method, of class Rope.
     */
    @Test
    public void testEndsWith_String_int() {
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.endsWith("\uD867\uDE3Dか\u3099", 1), is(false));
        assertThat(str.endsWith("\uD867\uDE3Dか\u3099", 3), is(true));
    }

    /**
     * Test of equals method, of class Rope.
     */
    @Test
    public void testEquals() {
        Rope left = new Rope("あいうえお");
        Rope right = new Rope("あいうえお");
        assertThat(left.hashCode(), is(right.hashCode()));
        assertThat(left, is(right));
    }

    /**
     * Test of equalsIgnoreCase method, of class Rope.
     */
    @Test
    public void testEqualsIgnoreCase() {

        Rope lhs = Rope.valueOf("AbcdＡｂあiIう");
        Rope rhs = Rope.valueOf("ABCDＡＢあIIう");
        assertThat(Rope.equalsIgnoreCase(null, null), is(true));
        assertThat(Rope.equalsIgnoreCase(lhs, rhs), is(true));
        assertThat(Rope.equalsIgnoreCase(null, rhs), is(false));
        assertThat(Rope.equalsIgnoreCase(lhs, null), is(false));
        assertThat(Rope.equalsIgnoreCase(lhs, Rope.valueOf("ABCDＡＢあI")), is(false));
    }

    /**
     * Test of getAt method, of class Rope.
     */
    @Test
    public void testGetAt() {
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        String actual1 = str.getAt(2);
        String actual2 = str.getAt(3);
        assertThat(actual1, is("\uD867\uDE3D"));
        assertThat(actual2, is("か\u3099"));
    }

    /**
     * Test of getBreakIterator method, of class Rope.
     */
    @Test
    public void testGetBreakIterator() {
        Rope instance = new Rope("あいう");
        Object iterType = BreakIterator.getCharacterInstance().getClass();
        BreakIterator result = instance.getBreakIterator();
        assertThat(result.getClass(), is(iterType));
    }

    /**
     * Test of hashCode method, of class Rope.
     */
    @Test
    public void testHashCode() {
        Rope left = new Rope("あいうえお");
        Rope right = new Rope("あいうえお");
        assertThat(left.hashCode(), is(right.hashCode()));
    }

    /**
     * Test of indexOf method, of class Rope.
     */
    @Test
    public void testIndexOf_Rope() {
        Rope key = Rope.valueOf("が");
        Rope str = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf(key), is(3));
    }

    /**
     * Test of indexOf method, of class Rope.
     */
    @Test
    public void testIndexOf_Rope_int() {
        Rope key = Rope.valueOf("が");
        Rope str = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf(key, 3), is(3));
        assertThat(str.indexOf(key, 4), is(-1));

        String find = "え";
        String source = "あいうえお";
        Rope rfind = Rope.valueOf(find);
        Rope rsource = Rope.valueOf(source);
        assertThat(rsource.indexOf(rfind, 3), is(source.indexOf(find, 3)));
    }

    /**
     * Test of indexOf method, of class Rope.
     */
    @Test
    public void testIndexOf_String() {
        Rope str = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf("が"), is(3));
    }

    /**
     * Test of indexOf method, of class Rope.
     */
    @Test
    public void testIndexOf_String_int() {
        String source = "あいうえおかき";
        String key = "お";
        int offset = 2;
        Rope str = new Rope(source);
        int expected = source.indexOf(key, offset);
        int actual = str.indexOf(Rope.valueOf(key), offset);
        assertThat(actual, is(expected));
    }

    /**
     * Test of isEmpty method, of class Rope.
     */
    @Test
    public void testIsEmpty_0args() {
        assertThat(Rope.EMPTY.isEmpty(), is(true));
        assertThat(Rope.valueOf("").isEmpty(), is(true));
        assertThat(Rope.valueOf(" ").isEmpty(), is(false));
        assertThat(Rope.valueOf(1).isEmpty(), is(false));
    }

    /**
     * Test of isEmpty method, of class Rope.
     */
    @Test
    public void testIsEmpty_Rope() {

        assertThat(Rope.isEmpty(null), is(true));
        assertThat(Rope.isEmpty(Rope.EMPTY), is(true));
        assertThat(Rope.isEmpty(Rope.valueOf("")), is(true));
        assertThat(Rope.isEmpty(Rope.valueOf(" ")), is(false));
    }

    /**
     * Test of iterator method, of class Rope.
     */
    @Test
    public void testIterator() {
        Rope instance = Rope.valueOf("この\uD867\uDE3Dか\u3099美味い");
        Iterator<String> result = instance.iterator();
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("こ"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("の"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("\uD867\uDE3D"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("か\u3099"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("美"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("味"));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is("い"));
        assertThat(result.hasNext(), is(false));
    }

    /**
     * Test of lastIndexOf method, of class Rope.
     */
    @Test
    public void testLastIndexOf_Rope() {
        String source = "あいうえおかき";
        String key = "うえ";
        Rope str = new Rope(source);
        int expected = source.lastIndexOf(key);
        int actual = str.lastIndexOf(new Rope(key));
        assertThat(actual, is(expected));
    }

    /**
     * Test of lastIndexOf method, of class Rope.
     */
    @Test
    public void testLastIndexOf_String() {
        String key = "が美";
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.lastIndexOf(key), is(3));
    }

    /**
     * Test of left method, of class Rope.
     */
    @Test
    public void testLeft() {
        Rope str = new Rope("この\uD867\uDE3Dが美味い");
        String expected = "この\uD867\uDE3Dが";
        String actual = str.left(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of left method, of class Rope.
     */
    @Test
    public void testLeft_2() {
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        String expected = "この\uD867\uDE3Dか\u3099";
        String actual = str.left(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of length method, of class Rope.
     */
    @Test
    public void testLength() {
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.length(), is(7));

        str = new Rope("");
        assertThat(str.length(), is(0));
    }

    /**
     * Test of replace method, of class Rope.
     */
    @Test
    public void testReplace() {
        int n = 2;
        Rope search = new Rope("が美味い");
        Rope replacement = new Rope("がまずい");
        Rope source = new Rope("この\uD867\uDE3Dか\u3099美味い。この\uD867\uDE3Dが美味い。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        Rope expected = new Rope("この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        Rope actual = source.replace(search, replacement, n);
        assertThat(actual, is(expected));
    }

    /**
     * Test of replaceAll method, of class Rope.
     */
    @Test
    public void testReplaceAll() {
        Rope search = new Rope("が美味い");
        Rope replacement = new Rope("がまずい");
        Rope source = new Rope("この\uD867\uDE3Dか\u3099美味い。この\uD867\uDE3Dが美味い。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        String expected = "この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。大事なことなので3度言いました";
        Rope actual = source.replaceAll(search, replacement);
        assertThat(actual.toString(), is(expected));
    }

    /**
     * Test of right method, of class Rope.
     */
    @Test
    public void testRight() {
        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        String expected = "か\u3099美味い";
        String actual = str.right(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of startsWith method, of class Rope.
     */
    @Test
    public void testStartsWith() {
        Rope left = new Rope("あいうえお");
        String right1 = ("あいうえ");
        String right2 = ("あいうえお");
        String right3 = ("あいうえおお");
        String right4 = ("うえお");
        assertThat(left.startsWith(right1, 0), is(true));
        assertThat(left.startsWith(right2, 0), is(true));
        assertThat(left.startsWith(right3, 0), is(false));
        assertThat(left.startsWith(right4, 0), is(false));
        assertThat(left.startsWith(right4, 2), is(true));
    }

    /**
     * Test of substring method, of class Rope.
     */
    @Test
    public void testSubstring_int() {
        String source = "あいうえおか";
        String expected = source.substring(2);
        String actual = Rope.valueOf(source).substring(2);
        assertThat(actual, is(expected));

        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        expected = "か\u3099美味い";
        actual = str.substring(3);
        assertThat(actual, is(expected));
    }

    /**
     * Test of substring method, of class Rope.
     */
    @Test
    public void testSubstring_int_int() {
        String source = "あいうえおか";
        String expected = source.substring(1, 3);
        String actual = Rope.valueOf(source).substring(1, 3);
        assertThat(actual, is(expected));

        Rope str = new Rope("この\uD867\uDE3Dか\u3099美味い");
        expected = "か\u3099美";
        actual = str.substring(3, 5);
        assertThat(actual, is(expected));
    }

    /**
     * Test of toLowerCase method, of class Rope.
     */
    @Test
    public void testToLowerCase_0args() {
        String source = "AbcdＡｂあiIう";
        String expected = "abcdａｂあiiう";
        Rope instance = Rope.valueOf(source);
        Rope result = instance.toLowerCase();
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toLowerCase method, of class Rope.
     */
    @Test
    public void testToLowerCase_Locale() {

        Locale locale = Locale.forLanguageTag("tr");

        String source = "AbcdＡｂあiIう";
        String expected = "abcdａｂあiıう";
        Rope instance = Rope.valueOf(source);
        Rope result = instance.toLowerCase(locale);
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toString method, of class Rope.
     */
    @Test
    public void testToString() {
        String expected = "あいうえお";
        Rope str = new Rope(expected);
        String actual = str.toString();
        assertThat(actual, is(expected));
    }

    /**
     * Test of toUpperCase method, of class Rope.
     */
    @Test
    public void testToUpperCase_0args() {
        String source = "AbcdＡｂあiIう";
        String expected = "ABCDＡＢあIIう";
        Rope instance = Rope.valueOf(source);
        Rope result = instance.toUpperCase();
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toUpperCase method, of class Rope.
     */
    @Test
    public void testToUpperCase_Locale() {

        Locale locale = Locale.forLanguageTag("tr");

        String source = "AbcdＡｂあiIう";
        String expected = "ABCDＡＢあİIう";
        Rope instance = Rope.valueOf(source);
        Rope result = instance.toUpperCase(locale);
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of valueOf method, of class Rope.
     */
    @Test
    public void testValueOf_Object() {
        assertThat(Rope.valueOf((Object) null), is(sameInstance(Rope.EMPTY)));
        assertThat(Rope.valueOf(123), is(new Rope("123")));
        assertThat(Rope.valueOf(12.5), is(new Rope("12.5")));
        assertThat(Rope.valueOf(Rope.EMPTY), is(sameInstance(Rope.EMPTY)));
    }

    /**
     * Test of valueOf method, of class Rope.
     */
    @Test
    public void testValueOf_String() {
        assertThat(Rope.valueOf((String) null), is(sameInstance(Rope.EMPTY)));
        assertThat(Rope.valueOf(""), is(sameInstance(Rope.EMPTY)));
        assertThat(Rope.valueOf(" "), is(new Rope(" ")));
        assertThat(Rope.valueOf("123"), is(new Rope("123")));
    }

}
