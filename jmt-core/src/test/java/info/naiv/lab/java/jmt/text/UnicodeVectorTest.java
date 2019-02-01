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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.RandomStringGenerator;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.SerializationUtils;

/**
 *
 * @author enlo
 */
@Slf4j
public class UnicodeVectorTest {

    @BeforeClass
    public static void setUp() {
        initUnicodeVectorCache();
    }
    private static void initUnicodeVectorCache() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        for (int i = 0; i < 100; i++) {
            String gen = generator.generate(ThreadLocalRandom.current().nextInt(10, 100000));
            UnicodeVectorCache.getDecomposed(gen);
        }
        logger.info("cache stats is {}", UnicodeVectorCache.DECOMP);
    }

    /**
     *
     */
    public UnicodeVectorTest() {
    }

    /**
     * Test of charAt method, of class UnicodeVector.
     */
    @Test
    public void testCharAt() {
        UnicodeVector instance = new UnicodeVector("\u0065\u0301㋐");
        assertThat(instance.asCharSequence().charAt(0), is('\u0065'));
        assertThat(instance.asCharSequence().charAt(1), is('\u0301'));
        assertThat(instance.asCharSequence().charAt(2), is('\u32D0'));
    }

    /**
     * Test of clone method, of class UnicodeVector.
     */
    @Test
    public void testClone() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301亜");
        UnicodeVector clone = str1.clone();
        assertThat(str1, is(not(sameInstance(clone))));
        assertThat(str1.toString(), is(clone.toString()));
    }

    /**
     * Test of compareTo method, of class UnicodeVector.
     */
    @Test
    public void testCompareTo() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        UnicodeVector str2 = new UnicodeVector("\u00e9㋐");
        assertThat(str1.compareTo(str2), is(0));
    }

    /**
     * Test of compareTo method, of class UnicodeVector.
     */
    @Test
    public void testCompareTo_2() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301");
        UnicodeVector str2 = new UnicodeVector("\u00e9㋐");
        assertThat(str1.compareTo(str2), is(lessThan(0)));
    }

    /**
     * Test of compareTo method, of class UnicodeVector.
     */
    @Test
    public void testCompareTo_3() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        UnicodeVector str2 = new UnicodeVector("\u00e9");
        assertThat(str1.compareTo(str2), is(greaterThan(0)));
    }

    /**
     * Test of compareTo method, of class UnicodeVector.
     */
    @Test
    public void testCompareTo_4() {
        UnicodeVector str1 = new UnicodeVector("A");
        UnicodeVector str2 = new UnicodeVector("B");
        assertThat(str1.compareTo(str2), is(lessThan(0)));
    }

    /**
     * Test of compareTo method, of class UnicodeVector.
     */
    @Test
    public void testCompareTo_5() {
        UnicodeVector str1 = new UnicodeVector("う");
        UnicodeVector str2 = new UnicodeVector("い");
        assertThat(str1.compareTo(str2), is(greaterThan(0)));
    }

    /**
     * Test of decompose method, of class UnicodeVector.
     */
    @Test
    public void testDecompose() {
        UnicodeVector str1 = new UnicodeVector("\u00e9あいう").decompose();
        UnicodeScalar s1 = new UnicodeScalar("\u0065\u0301");
        UnicodeScalar s2 = new UnicodeScalar("あ");
        UnicodeScalar s3 = new UnicodeScalar("い");
        UnicodeScalar s4 = new UnicodeScalar("う");
        assertThat(str1.toString(), is("\u0065\u0301あいう"));
        assertThat(str1.elements(), is(arrayContaining(s1, s2, s3, s4)));
    }

    /**
     * Test of decompose method, of class UnicodeVector.
     */
    @Test
    public void testDecompose_2() {
        UnicodeVector str1 = new UnicodeVector(true, "\u00e9あいう").decompose();
        assertThat(str1.toString(), is("\u00e9あいう"));
    }

    /**
     * Test of elements method, of class UnicodeVector.
     */
    @Test
    public void testElements() {
        UnicodeVector str1 = new UnicodeVector("\u00e9あいう");
        UnicodeScalar s1 = new UnicodeScalar("\u00e9");
        UnicodeScalar s2 = new UnicodeScalar("あ");
        UnicodeScalar s3 = new UnicodeScalar("い");
        UnicodeScalar s4 = new UnicodeScalar("う");
        assertThat(str1.elements(), is(arrayContaining(s1, s2, s3, s4)));
    }

    /**
     * Test of equals method, of class UnicodeVector.
     */
    @Test
    public void testEquals() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        UnicodeVector str2 = new UnicodeVector("\u00e9㋐");
        UnicodeVector str3 = new UnicodeVector("\u0065\u0301㋐");
        assertThat(str1, is(not(str2)));
        assertThat(str1, is(str3));
    }

    /**
     * Test of getSource method, of class UnicodeVector.
     */
    @Test
    public void testGetSource() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301あいう");
        assertThat(str1.toString(), is("\u0065\u0301あいう"));
    }

    /**
     * Test of hashCode method, of class UnicodeVector.
     */
    @Test
    public void testHashCode() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        UnicodeVector str2 = new UnicodeVector("\u0065\u0301㋐");
        assertThat(str1.hashCode(), is(str2.hashCode()));
    }

    /**
     * Test of isDecomposed method, of class UnicodeVector.
     */
    @Test
    public void testIsDecomposed() {
        UnicodeVector str1 = new UnicodeVector("\u00e9");
        UnicodeVector str2 = str1.decompose();
        assertThat(str1.isDecomposed(), is(false));
        assertThat(str2.isDecomposed(), is(true));
    }

    /**
     * Test of isEmpty method, of class UnicodeVector.
     */
    @Test
    public void testIsEmpty() {
        UnicodeVector str1 = new UnicodeVector("\u00e9");
        UnicodeVector str2 = new UnicodeVector("");
        assertThat(str1.isEmpty(), is(false));
        assertThat(str2.isEmpty(), is(true));
    }

    /**
     * Test of iterator method, of class UnicodeVector.
     */
    @Test
    public void testIterator() {
        UnicodeVector instance = new UnicodeVector("\u0065\u0301㋐");
        Iterator<UnicodeScalar> result = instance.iterator();
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(new UnicodeScalar("\u0065\u0301")));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(new UnicodeScalar("㋐")));
        assertThat(result.hasNext(), is(false));
    }
    @Test
    public void testLargeText() throws IOException {
        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("/META-INF/test-application-context2.xml");) {
            
            Resource res = context.getResource("classpath:TEXT/largeText.txt");
            try (InputStream is = res.getInputStream()) {
                String text = IOUtils.toString(is, StandardCharsets.UTF_8);
                UnicodeVector test = new UnicodeVector(text);
                StopWatch sw = new StopWatch();
                sw.start();
                int length = test.length();
                sw.stop();
                System.out.println("length time is " + sw.toString());
                sw.reset();
                sw.start();
                
                UnicodeVector decomposed = test.decompose();
                for (int i = 0; i < 1000; i++) {
                    decomposed = test.decompose();
                }
                sw.stop();
                System.out.println("decompose time is " + sw.toString());
                sw.reset();
                sw.start();
                UnicodeVector decomposed2 = UnicodeVectorCache.getDecomposed(test);
                for (int i = 0; i < 1000; i++) {
                    decomposed2 = UnicodeVectorCache.getDecomposed(test);
                }
                sw.stop();
                assertThat(decomposed2, is(decomposed));
                System.out.println("decompose time (use cache) is " + sw.toString());
                sw.reset();
                sw.start();
                boolean b = true;
                for (int i = 0; i < 1000; i++) {
                    b = test.equals(decomposed);
                }
                sw.stop();
                assertThat(b, is(false));
                System.out.println("compare time is " + sw.toString() + ": ");
            }
        }
    }

    /**
     * Test of length method, of class UnicodeVector.
     */
    @Test
    public void testLength() {
        UnicodeVector instance = new UnicodeVector("\u0065\u0301㋐");
        assertThat(instance.length(), is(2));
    }

    /**
     * Test of reverseIterator method, of class UnicodeVector.
     */
    @Test
    public void testReverseIterator() {
        UnicodeVector instance = new UnicodeVector("\u0065\u0301㋐");
        Iterator<UnicodeScalar> result = instance.reverseIterator();
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(new UnicodeScalar("㋐")));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(new UnicodeScalar("\u0065\u0301")));
        assertThat(result.hasNext(), is(false));
    }

    @Test
    public void testSerialize() {

        UnicodeVector src = new UnicodeVector("あかさたな");

        UnicodeScalar[] expected = src.elements();
        byte[] bin = SerializationUtils.serialize(src);
        UnicodeVector dest = (UnicodeVector) SerializationUtils.deserialize(bin);
        UnicodeScalar[] actual = dest.elements();
        assertThat(actual, is(expected));
    }
    @Test
    public void testSmallText() throws IOException {
        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("/META-INF/test-application-context2.xml");) {
            
            initUnicodeVectorCache();
            
            Resource res = context.getResource("classpath:TEXT/nobomtext.txt");
            try (InputStream is = res.getInputStream()) {
                String text = IOUtils.toString(is, StandardCharsets.UTF_8);
                UnicodeVector test = new UnicodeVector(text);
                StopWatch sw = new StopWatch();
                sw.start();
                UnicodeVector decomposed = test.decompose();
                for (int i = 0; i < 1000; i++) {
                    decomposed = test.decompose();
                }
                sw.stop();
                System.out.println("decompose time is " + sw.toString());
                sw.reset();
                sw.start();
                UnicodeVector decomposed2 = UnicodeVectorCache.getDecomposed(test);
                for (int i = 0; i < 1000; i++) {
                    decomposed2 = UnicodeVectorCache.getDecomposed(test);
                }
                sw.stop();
                assertThat(decomposed2, is(decomposed));
                System.out.println("decompose time (use cache) is " + sw.toString());
                sw.reset();
                sw.start();
                boolean b = true;
                for (int i = 0; i < 1000; i++) {
                    b = test.equals(decomposed);
                }
                sw.stop();
                assertThat(b, is(false));
                System.out.println("compare time is " + sw.toString() + ": ");
            }
        }
    }

    /**
     * Test of subSequence method, of class UnicodeVector.
     */
    @Test
    public void testSubSequence() {
        UnicodeVector instance = new UnicodeVector("\u0065\u0301㋐");
        assertThat(instance.asCharSequence().subSequence(0, 1), is((CharSequence) "\u0065"));
        assertThat(instance.asCharSequence().subSequence(1, 2), is((CharSequence) "\u0301"));
        assertThat(instance.asCharSequence().subSequence(1, 3), is((CharSequence) "\u0301㋐"));
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test
    public void testSubVector_int() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        UnicodeVector str2 = new UnicodeVector("㋐");
        UnicodeVector str3 = new UnicodeVector("");
        assertThat(str1.subVector(0), is(str1));
        assertThat(str1.subVector(1), is(str2));
        assertThat(str1.subVector(2), is(str3));
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSubVector_int_2() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        str1.subVector(-1);
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSubVector_int_3() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        str1.subVector(3);
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test
    public void testSubVector_int_int() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐いう");
        UnicodeVector str2 = new UnicodeVector("㋐");
        UnicodeVector str3 = new UnicodeVector("㋐いう");
        UnicodeVector str4 = new UnicodeVector("");
        assertThat(str1.subVector(0, 4), is(str1));
        assertThat(str1.subVector(1, 2), is(str2));
        assertThat(str1.subVector(1, 4), is(str3));
        assertThat(str1.subVector(1, 5), is(str3));
        assertThat(str1.subVector(4, 4), is(str4));
        assertThat(str1.subVector(4, 5), is(str4));
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubVector_int_int_2() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐いう");
        str1.subVector(2, 1);
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSubVector_int_int_3() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐いう");
        str1.subVector(5, 6);
    }

    /**
     * Test of subVector method, of class UnicodeVector.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSubVector_int_int_4() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐いう");
        str1.subVector(-1, 6);
    }

    /**
     * Test of toString method, of class UnicodeVector.
     */
    @Test
    public void testToString() {
        UnicodeVector str1 = new UnicodeVector("\u0065\u0301㋐");
        assertThat(str1.toString(), is("\u0065\u0301㋐"));
    }

}
