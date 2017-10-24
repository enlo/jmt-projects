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

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.SerializationUtils;

/**
 *
 * @author enlo
 */
public class UniCordTest {

    static final String NULL_STRING = null;

    /**
     *
     */
    public UniCordTest() {
    }

    /**
     * Test of compareNormalized method, of class UniCord.
     */
    @Test
    public void testCompareNormalized_Rope() {
        UniCord left1 = new UniCord("あいうえお");
        UniCord left2 = new UniCord("か\u3099");
        UniCord right1 = new UniCord("あいうえ");
        UniCord right2 = new UniCord("あいうえお");
        UniCord right3 = new UniCord("あいうえおか");
        UniCord right4 = new UniCord("が");
        assertThat(left1.compareTo(right1) > 0, is(true));
        assertThat(left1.compareNormalized(right1) > 0, is(true));
        assertThat(left1.compareNormalized(right2) == 0, is(true));
        assertThat(left1.compareNormalized(right3) < 0, is(true));
        assertThat(left2.compareNormalized(right4) == 0, is(true));
    }

    /**
     * Test of compareNormalized method, of class UniCord.
     */
    @Test
    public void testCompareNormalized_String_String() {
        String left1 = "あいうえお";
        String left2 = "か\u3099";
        String right1 = "あいうえ";
        String right2 = "あいうえお";
        String right3 = "あいうえおか";
        String right4 = "が";
        assertThat(UniCord.compareNormalized(left1, right1) > 0, is(true));
        assertThat(UniCord.compareNormalized(left1, right2) == 0, is(true));
        assertThat(UniCord.compareNormalized(left1, right3) < 0, is(true));
        assertThat(UniCord.compareNormalized(left2, right4) == 0, is(true));
    }

    /**
     * Test of compareTo method, of class UniCord.
     */
    @Test
    public void testCompareTo() {
        String[] source = {"ABC", "abc", "cdef", "1234"};
        for (String s : source) {
            UniCord l = UniCord.valueOf(s);
            UniCord k = UniCord.valueOf("abc");
            assertThat(s, l.compareTo(k), is(s.compareTo("abc")));
        }
    }

    /**
     * Test of contains method, of class UniCord.
     */
    @Test
    public void testContains_Rope() {
        UniCord str = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");

        assertThat(str.contains(UniCord.valueOf("が")), is(true));
        assertThat(str.contains(UniCord.valueOf("か\u3099")), is(true));
        assertThat(str.contains(UniCord.valueOf("が美")), is(true));
        assertThat(str.contains(UniCord.valueOf("が馬")), is(false));
    }

    /**
     * Test of contains method, of class UniCord.
     */
    @Test
    public void testContains_String() {
        UniCord str = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.contains("が"), is(true));
        assertThat(str.contains("か\u3099"), is(true));
        assertThat(str.contains("が美"), is(true));
        assertThat(str.contains("が馬"), is(false));
    }

    /**
     * Test of decompose method, of class UniCord.
     */
    @Test
    public void testDecompose() {
        UniCord expected = new UniCord("か\u3099");
        UniCord source = new UniCord("が");
        UniCord actual = source.decompose();
        assertThat(actual, is(expected));
    }

    /**
     * decomposeテスト2<br>
     * 一度 decompose したら、あとは自分自身を戻す.
     */
    @Test
    public void testDecompose2() {
        UniCord source = new UniCord("が");
        UniCord expected = source.decompose();
        UniCord actual = expected.decompose();
        assertThat(actual, is(sameInstance(expected)));
    }

    /**
     * Test of endsWith method, of class UniCord.
     */
    @Test
    public void testEndsWith() {
        UniCord left = new UniCord("あいうえお");
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
     * Test of endsWith method, of class UniCord.
     */
    @Test
    public void testEndsWith_String_int() {
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.endsWith("\uD867\uDE3Dか\u3099", 1), is(false));
        assertThat(str.endsWith("\uD867\uDE3Dか\u3099", 3), is(true));
    }

    /**
     * Test of equals method, of class UniCord.
     */
    @Test
    public void testEquals() {
        UniCord left = new UniCord("あいうえお");
        UniCord right = new UniCord("あいうえお");
        assertThat(left.hashCode(), is(right.hashCode()));
        assertThat(left, is(right));
    }

    /**
     * Test of equalsIgnoreCase method, of class UniCord.
     */
    @Test
    public void testEqualsIgnoreCase() {

        UniCord lhs = UniCord.valueOf("AbcdＡｂあiIう");
        UniCord rhs = UniCord.valueOf("ABCDＡＢあIIう");
        assertThat(UniCord.equalsIgnoreCase(null, null), is(true));
        assertThat(UniCord.equalsIgnoreCase(lhs, rhs), is(true));
        assertThat(UniCord.equalsIgnoreCase(null, rhs), is(false));
        assertThat(UniCord.equalsIgnoreCase(lhs, null), is(false));
        assertThat(UniCord.equalsIgnoreCase(lhs, UniCord.valueOf("ABCDＡＢあI")), is(false));
    }

    /**
     * Test of getAt method, of class UniCord.
     */
    @Test
    public void testGetAt() {
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        String actual1 = str.getAt(2);
        String actual2 = str.getAt(3);
        assertThat(actual1, is("\uD867\uDE3D"));
        assertThat(actual2, is("か\u3099"));
    }

    /**
     * Test of getBreakIterator method, of class UniCord.
     */
    @Test
    public void testGetBreakIterator() {
        UniCord instance = new UniCord("あいう");
        Object iterType = BreakIterator.getCharacterInstance().getClass();
        BreakIterator result = instance.getBreakIterator();
        assertThat(result.getClass(), is(iterType));
    }

    /**
     * Test of hashCode method, of class UniCord.
     */
    @Test
    public void testHashCode() {
        UniCord left = new UniCord("あいうえお");
        UniCord right = new UniCord("あいうえお");
        assertThat(left.hashCode(), is(right.hashCode()));
    }

    /**
     * Test of indexOf method, of class UniCord.
     */
    @Test
    public void testIndexOf_Rope() {
        UniCord key = UniCord.valueOf("が");
        UniCord str = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf(key), is(3));
    }

    /**
     * Test of indexOf method, of class UniCord.
     */
    @Test
    public void testIndexOf_Rope_int() {
        UniCord key = UniCord.valueOf("が");
        UniCord str = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf(key, 3), is(3));
        assertThat(str.indexOf(key, 4), is(-1));

        String find = "え";
        String source = "あいうえお";
        UniCord rfind = UniCord.valueOf(find);
        UniCord rsource = UniCord.valueOf(source);
        assertThat(rsource.indexOf(rfind, 3), is(source.indexOf(find, 3)));
    }

    /**
     * Test of indexOf method, of class UniCord.
     */
    @Test
    public void testIndexOf_String() {
        UniCord str = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.indexOf("が"), is(3));
    }

    /**
     * Test of indexOf method, of class UniCord.
     */
    @Test
    public void testIndexOf_String_int() {
        String source = "あいうえおかき";
        String key = "お";
        int offset = 2;
        UniCord str = new UniCord(source);
        int expected = source.indexOf(key, offset);
        int actual = str.indexOf(UniCord.valueOf(key), offset);
        assertThat(actual, is(expected));
    }

    /**
     * Test of isEmpty method, of class UniCord.
     */
    @Test
    public void testIsEmpty_0args() {
        assertThat(UniCord.EMPTY.isEmpty(), is(true));
        assertThat(UniCord.valueOf("").isEmpty(), is(true));
        assertThat(UniCord.valueOf(" ").isEmpty(), is(false));
        assertThat(UniCord.valueOf(1).isEmpty(), is(false));
    }

    /**
     * Test of isEmpty method, of class UniCord.
     */
    @Test
    public void testIsEmpty_Rope() {

        assertThat(UniCord.isEmpty(null), is(true));
        assertThat(UniCord.isEmpty(UniCord.EMPTY), is(true));
        assertThat(UniCord.isEmpty(UniCord.valueOf("")), is(true));
        assertThat(UniCord.isEmpty(UniCord.valueOf(" ")), is(false));
    }

    /**
     * Test of iterator method, of class UniCord.
     */
    @Test
    public void testIterator() {
        UniCord instance = UniCord.valueOf("この\uD867\uDE3Dか\u3099美味い");
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
     * Test of lastIndexOf method, of class UniCord.
     */
    @Test
    public void testLastIndexOf_Rope() {
        String source = "あいうえおかき";
        String key = "うえ";
        UniCord str = new UniCord(source);
        int expected = source.lastIndexOf(key);
        int actual = str.lastIndexOf(new UniCord(key));
        assertThat(actual, is(expected));
    }

    /**
     * Test of lastIndexOf method, of class UniCord.
     */
    @Test
    public void testLastIndexOf_String() {
        String key = "が美";
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.lastIndexOf(key), is(3));
    }

    /**
     * Test of left method, of class UniCord.
     */
    @Test
    public void testLeft() {
        UniCord str = new UniCord("この\uD867\uDE3Dが美味い");
        String expected = "この\uD867\uDE3Dが";
        String actual = str.left(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of left method, of class UniCord.
     */
    @Test
    public void testLeft_2() {
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        String expected = "この\uD867\uDE3Dか\u3099";
        String actual = str.left(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of length method, of class UniCord.
     */
    @Test
    public void testLength() {
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        assertThat(str.length(), is(7));

        str = new UniCord("");
        assertThat(str.length(), is(0));
    }

    /**
     * Test of replace method, of class UniCord.
     */
    @Test
    public void testReplace() {
        int n = 2;
        UniCord search = new UniCord("が美味い");
        UniCord replacement = new UniCord("がまずい");
        UniCord source = new UniCord("この\uD867\uDE3Dか\u3099美味い。この\uD867\uDE3Dが美味い。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        UniCord expected = new UniCord("この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        UniCord actual = source.replace(search, replacement, n);
        assertThat(actual, is(expected));
    }

    /**
     * Test of replaceAll method, of class UniCord.
     */
    @Test
    public void testReplaceAll() {
        UniCord search = new UniCord("が美味い");
        UniCord replacement = new UniCord("がまずい");
        UniCord source = new UniCord("この\uD867\uDE3Dか\u3099美味い。この\uD867\uDE3Dが美味い。この\uD867\uDE3Dか\u3099美味い。大事なことなので3度言いました");
        String expected = "この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。この\uD867\uDE3Dがまずい。大事なことなので3度言いました";
        UniCord actual = source.replaceAll(search, replacement);
        assertThat(actual.toString(), is(expected));
    }

    /**
     * Test of right method, of class UniCord.
     */
    @Test
    public void testRight() {
        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        String expected = "か\u3099美味い";
        String actual = str.right(4);
        assertThat(actual, is(expected));
    }

    /**
     * Test of startsWith method, of class UniCord.
     */
    @Test
    public void testStartsWith() {
        UniCord left = new UniCord("あいうえお");
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
     * Test of substring method, of class UniCord.
     */
    @Test
    public void testSubstring_int() {
        String source = "あいうえおか";
        String expected = source.substring(2);
        String actual = UniCord.valueOf(source).substring(2);
        assertThat(actual, is(expected));

        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        expected = "か\u3099美味い";
        actual = str.substring(3);
        assertThat(actual, is(expected));
    }

    /**
     * Test of substring method, of class UniCord.
     */
    @Test
    public void testSubstring_int_int() {
        String source = "あいうえおか";
        String expected = source.substring(1, 3);
        String actual = UniCord.valueOf(source).substring(1, 3);
        assertThat(actual, is(expected));

        UniCord str = new UniCord("この\uD867\uDE3Dか\u3099美味い");
        expected = "か\u3099美";
        actual = str.substring(3, 5);
        assertThat(actual, is(expected));
    }

    /**
     * Test of toLowerCase method, of class UniCord.
     */
    @Test
    public void testToLowerCase_0args() {
        String source = "AbcdＡｂあiIう";
        String expected = "abcdａｂあiiう";
        UniCord instance = UniCord.valueOf(source);
        UniCord result = instance.toLowerCase();
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toLowerCase method, of class UniCord.
     */
    @Test
    public void testToLowerCase_Locale() {

        Locale locale = Locale.forLanguageTag("tr");

        String source = "AbcdＡｂあiIう";
        String expected = "abcdａｂあiıう";
        UniCord instance = UniCord.valueOf(source);
        UniCord result = instance.toLowerCase(locale);
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toString method, of class UniCord.
     */
    @Test
    public void testToString() {
        String expected = "あいうえお";
        UniCord str = new UniCord(expected);
        String actual = str.toString();
        assertThat(actual, is(expected));
    }

    /**
     * Test of toUpperCase method, of class UniCord.
     */
    @Test
    public void testToUpperCase_0args() {
        String source = "AbcdＡｂあiIう";
        String expected = "ABCDＡＢあIIう";
        UniCord instance = UniCord.valueOf(source);
        UniCord result = instance.toUpperCase();
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of toUpperCase method, of class UniCord.
     */
    @Test
    public void testToUpperCase_Locale() {

        Locale locale = Locale.forLanguageTag("tr");

        String source = "AbcdＡｂあiIう";
        String expected = "ABCDＡＢあİIう";
        UniCord instance = UniCord.valueOf(source);
        UniCord result = instance.toUpperCase(locale);
        assertThat(result.toString(), is(expected));
    }

    /**
     * Test of valueOf method, of class UniCord.
     */
    @Test
    public void testValueOf_Object() {
        assertThat(UniCord.valueOf((Object) null), is(sameInstance(UniCord.EMPTY)));
        assertThat(UniCord.valueOf(123), is(new UniCord("123")));
        assertThat(UniCord.valueOf(12.5), is(new UniCord("12.5")));
        assertThat(UniCord.valueOf(UniCord.EMPTY), is(sameInstance(UniCord.EMPTY)));
    }

    /**
     * Test of valueOf method, of class UniCord.
     */
    @Test
    public void testValueOf_String() {
        assertThat(UniCord.valueOf(NULL_STRING), is(sameInstance(UniCord.EMPTY)));
        assertThat(UniCord.valueOf(""), is(sameInstance(UniCord.EMPTY)));
        assertThat(UniCord.valueOf(" "), is(new UniCord(" ")));
        assertThat(UniCord.valueOf("123"), is(new UniCord("123")));
    }

    @Test
    public void testSerialize() throws IOException {
        UniCord src = new UniCord("\u0065\u0301");
        byte[] bin = SerializationUtils.serialize(src);
        UniCord dest = (UniCord) SerializationUtils.deserialize(bin);
        assertThat(dest, is(src));
    }

    @Test
    public void testLargeText() throws IOException {
        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("/META-INF/test-application-context2.xml");) {

            Resource res = context.getResource("classpath:TEXT/largeText.txt");
            try (InputStream is = res.getInputStream()) {
                String text = IOUtils.toString(is, StandardCharsets.UTF_8);
                UniCord test = UniCord.valueOf(text);
                StopWatch sw = new StopWatch();
            }
        }
    }
}
