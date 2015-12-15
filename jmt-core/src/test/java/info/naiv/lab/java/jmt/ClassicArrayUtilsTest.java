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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.*;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class ClassicArrayUtilsTest {

    public ClassicArrayUtilsTest() {
    }

    /**
     * Test of arrayAsIterable method, of class ClassicArrayUtils.
     */
    @Test
    public void testArrayAsIterable() {
        String[] arr = {"1", "2", "3"};
        Iterable<String> actual = arrayAsIterable(arr);
        assertThat(actual, contains("1", "2", "3"));
    }

    /**
     * Test of arrayCompareTo method, of class Misc.
     */
    @Test
    public void testArrayCompareTo_4args() {
        Integer[] arr1 = {1, 2, 3, 4, 5};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {2, 2, 3};
        Integer[] arr4 = {1, 2, 3};
        assertThat(arrayCompareTo(arr1, 0, arr2, 0), is(greaterThan(0)));
        assertThat(arrayCompareTo(arr1, 1, arr3, 1), is(greaterThan(0)));
        assertThat(arrayCompareTo(arr3, 1, arr4, 0), is(greaterThan(0)));
        assertThat(arrayCompareTo(arr3, 0, arr4, 2), is(lessThan(0)));
        assertThat(arrayCompareTo(arr2, 1, arr3, 1), is(0));
    }

    /**
     * Test of arrayCompareTo method, of class Misc.
     */
    @Test
    public void testArrayCompareTo_6args() {
        Integer[] arr1 = {1, 2, 3, 4, 5};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {2, 2, 3};
        Integer[] arr4 = {1, 2, 3};
        assertThat(arrayCompareTo(arr1, 0, 3, arr2, 0, 3), is(0));
        assertThat(arrayCompareTo(arr3, 0, 2, arr4, 0, 1), is(greaterThan(0)));
    }

    /**
     * Test of arrayCompareTo method, of class Misc.
     */
    @Test
    public void testArrayCompareTo_GenericType_GenericType() {
        Integer[] arr1 = {1, 2, 3, 4, 5};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {2, 2, 3};
        Integer[] arr4 = {1, 2, 3};
        assertThat(arrayCompareTo(arr1, arr2), is(greaterThan(0)));
        assertThat(arrayCompareTo(arr1, arr3), is(lessThan(0)));
        assertThat(arrayCompareTo(arr3, arr4), is(greaterThan(0)));
        assertThat(arrayCompareTo(arr2, arr4), is(0));
    }

    /**
     * Test of arrayEqualsInRange method, of class Misc.
     */
    @Test
    public void testArrayEqualsInRange_5args_1() {
        byte[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] arr2 = {3, 4, 5, 7};
        assertThat("1", arrayEqualsInRange(arr1, 0, arr2, 0, 3), is(false));
        assertThat("2", arrayEqualsInRange(arr1, 1, arr2, 0, 3), is(false));
        assertThat("3", arrayEqualsInRange(arr1, 2, arr2, 0, 1), is(true));
        assertThat("4", arrayEqualsInRange(arr1, 2, arr2, 0, 2), is(true));
        assertThat("5", arrayEqualsInRange(arr1, 2, arr2, 0, 3), is(true));
        assertThat("6", arrayEqualsInRange(arr1, 2, arr2, 0, 4), is(false));
        assertThat("7", arrayEqualsInRange(arr1, 2, arr2, 1, 2), is(false));
        assertThat("8", arrayEqualsInRange(arr1, 2, arr2, 2, 1), is(false));
        assertThat("9", arrayEqualsInRange(arr1, 3, arr2, 0, 3), is(false));
        assertThat("10", arrayEqualsInRange(arr1, 3, arr2, 1, 2), is(true));
        assertThat("11", arrayEqualsInRange(arr1, 3, arr2, 2, 1), is(false));
        assertThat("12", arrayEqualsInRange(arr1, 4, arr2, 0, 3), is(false));
        assertThat("13", arrayEqualsInRange(arr1, 4, arr2, 1, 2), is(false));
        assertThat("14", arrayEqualsInRange(arr1, 4, arr2, 2, 1), is(true));
    }

    /**
     * Test of arrayEqualsInRange method, of class Misc.
     */
    @Test
    public void testArrayEqualsInRange_5args_2() {
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] arr2 = {3, 4, 5, 7};
        assertThat("1", arrayEqualsInRange(arr1, 0, arr2, 0, 3), is(false));
        assertThat("2", arrayEqualsInRange(arr1, 1, arr2, 0, 3), is(false));
        assertThat("3", arrayEqualsInRange(arr1, 2, arr2, 0, 1), is(true));
        assertThat("4", arrayEqualsInRange(arr1, 2, arr2, 0, 2), is(true));
        assertThat("5", arrayEqualsInRange(arr1, 2, arr2, 0, 3), is(true));
        assertThat("6", arrayEqualsInRange(arr1, 2, arr2, 0, 4), is(false));
        assertThat("7", arrayEqualsInRange(arr1, 2, arr2, 1, 2), is(false));
        assertThat("8", arrayEqualsInRange(arr1, 2, arr2, 2, 1), is(false));
        assertThat("9", arrayEqualsInRange(arr1, 3, arr2, 0, 3), is(false));
        assertThat("10", arrayEqualsInRange(arr1, 3, arr2, 1, 2), is(true));
        assertThat("11", arrayEqualsInRange(arr1, 3, arr2, 2, 1), is(false));
        assertThat("12", arrayEqualsInRange(arr1, 4, arr2, 0, 3), is(false));
        assertThat("13", arrayEqualsInRange(arr1, 4, arr2, 1, 2), is(false));
        assertThat("14", arrayEqualsInRange(arr1, 4, arr2, 2, 1), is(true));
    }

    /**
     * Test of arrayEqualsInRange method, of class Misc.
     */
    @Test
    public void testArrayEqualsInRange_5args_3() {
        String[] arr1 = {"1", "2", "3", "4", "5", "6", "7"};
        String[] arr2 = {"3", "4", "5", "7"};
        assertThat("1", arrayEqualsInRange(arr1, 0, arr2, 0, 3), is(false));
        assertThat("2", arrayEqualsInRange(arr1, 1, arr2, 0, 3), is(false));
        assertThat("3", arrayEqualsInRange(arr1, 2, arr2, 0, 1), is(true));
        assertThat("4", arrayEqualsInRange(arr1, 2, arr2, 0, 2), is(true));
        assertThat("5", arrayEqualsInRange(arr1, 2, arr2, 0, 3), is(true));
        assertThat("6", arrayEqualsInRange(arr1, 2, arr2, 0, 4), is(false));
        assertThat("7", arrayEqualsInRange(arr1, 2, arr2, 1, 2), is(false));
        assertThat("8", arrayEqualsInRange(arr1, 2, arr2, 2, 1), is(false));
        assertThat("9", arrayEqualsInRange(arr1, 3, arr2, 0, 3), is(false));
        assertThat("10", arrayEqualsInRange(arr1, 3, arr2, 1, 2), is(true));
        assertThat("11", arrayEqualsInRange(arr1, 3, arr2, 2, 1), is(false));
        assertThat("12", arrayEqualsInRange(arr1, 4, arr2, 0, 3), is(false));
        assertThat("13", arrayEqualsInRange(arr1, 4, arr2, 1, 2), is(false));
        assertThat("14", arrayEqualsInRange(arr1, 4, arr2, 2, 1), is(true));
    }

    /**
     * Test of sort method, of class Misc.
     */
    @Test
    public void testArraySort() {
        List<Integer> x = Arrays.asList(arraySort(6, 3, 5, 2, 4, 1));
        assertThat(x, is(contains(1, 2, 3, 4, 5, 6)));
    }

    /**
     * Test of arrayToString method, of class ClassicArrayUtils.
     */
    @Test
    public void testArrayToString() {
        assertThat(arrayToString(new String[]{"a", "b", "c"}), is("abc"));
        assertThat(arrayToString(new Object[]{"a", null, 1}), is("a1"));
    }

    /**
     * Test of createArray method, of class ClassicArrayUtils.
     */
    @Test
    public void testCreateArray() {
        String[] actual;

        actual = createArray("A");
        assertThat(actual, is(allOf(arrayWithSize(1), arrayContaining("A"))));

        actual = createArray("A", (String) null);
        assertThat(actual, is(allOf(arrayWithSize(2), arrayContaining("A", null))));

        actual = createArray("A", "B", "C");
        assertThat(actual, is(allOf(arrayWithSize(3), arrayContaining("A", "B", "C"))));

    }
}
