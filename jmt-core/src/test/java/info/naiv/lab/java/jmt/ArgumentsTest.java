/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class ArgumentsTest {

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     */
    public ArgumentsTest() {
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

    /**
     * Test of between method, of class Arguments.
     */
    @Test()
    public void testBetween_4args_1() {
        Calendar begin = new GregorianCalendar(2014, 12, 11);
        Calendar end = new GregorianCalendar(2014, 12, 20);
        Calendar d1 = new GregorianCalendar(2014, 12, 11);
        Calendar d2 = new GregorianCalendar(2014, 12, 20);
        assertThat(Arguments.between(d1, begin, end, "arg"), is(sameInstance(d1)));
        assertThat(Arguments.between(d2, begin, end, "arg"), is(sameInstance(d2)));
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_1_2() {
        Calendar begin = new GregorianCalendar(2014, 12, 11);
        Calendar end = new GregorianCalendar(2014, 12, 20);
        Calendar d1 = new GregorianCalendar(2014, 12, 10);
        Arguments.between(d1, begin, end, "arg");
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_1_3() {
        Calendar begin = new GregorianCalendar(2014, 12, 11);
        Calendar end = new GregorianCalendar(2014, 12, 20);
        Calendar d1 = new GregorianCalendar(2014, 12, 21);
        Arguments.between(d1, begin, end, "arg");
    }

    /**
     * Test of between method, of class Arguments.
     */
    @Test
    public void testBetween_4args_2() {
        int begin = 10;
        int end = 20;
        assertThat(Arguments.between(10L, begin, end, "arg"), is(10L));
        assertThat(Arguments.between(20L, begin, end, "arg"), is(20L));
        assertThat(Arguments.between(15L, begin, end, "arg"), is(15L));
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_2_2() {
        int begin = 10;
        int end = 20;
        Arguments.between(9, begin, end, "arg");
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_2_3() {
        int begin = 10;
        int end = 20;
        Arguments.between(21, begin, end, "arg");
    }

    /**
     * Test of between method, of class Arguments.
     */
    @Test
    public void testBetween_4args_3() {
        double begin = 10;
        double end = 20;
        assertThat(Arguments.between(10.00, begin, end, "arg"), is(10.0));
        assertThat(Arguments.between(20.00, begin, end, "arg"), is(20.0));
        assertThat(Arguments.between(10.01, begin, end, "arg"), is(10.01));
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_3_2() {
        double begin = 10;
        double end = 20;
        Arguments.between(9.9999, begin, end, "arg");
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_4args_3_3() {
        double begin = 10;
        double end = 20;
        Arguments.between(20.01, begin, end, "arg");
    }

    /**
     * Test of between method, of class Arguments.
     */
    @Test
    public void testBetween_4args_4() {
        int begin = 10;
        int end = 20;
        assertThat(Arguments.between(10, begin, end, "arg"), is(10));
        assertThat(Arguments.between(20, begin, end, "arg"), is(20));
        assertThat(Arguments.between(15, begin, end, "arg"), is(15));
    }

    /**
     * Test of isInstanceOf method, of class Arguments.
     */
    @Test
    public void testIsInstanceOf() {
        assertThat(Arguments.isInstanceOf(null, Number.class, "arg"), is(nullValue()));
        assertThat(Arguments.isInstanceOf(12, Number.class, "arg"), is((Number) 12));
        assertThat(Arguments.isInstanceOf(12.5, Number.class, "arg"), is((Number) 12.5));
    }

    /**
     * Test of isInstanceOf method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOf_2() {
        Arguments.isInstanceOf("", Number.class, "arg");
    }

    /**
     * Test of isInterface method, of class Arguments.
     */
    @Test
    public void testIsInterface() {
        Class<Comparable> arg = Comparable.class;
        assertThat(Arguments.isInterface(arg, "arg"), is(sameInstance(arg)));
    }

    /**
     * Test of isInterface method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsInterface_2() {
        Class<String> arg = String.class;
        Arguments.isInterface(arg, "arg");
    }

    /**
     * Test of isInterface method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsInterface_3() {
        Arguments.isInterface(null, "arg");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test
    public void testLessThan_3args_1() {
        assertThat(Arguments.lessThan(0, 1, ""), is(0));
        assertThat(Arguments.lessThan(20, 30, ""), is(20));
        assertThat(Arguments.lessThan(-1, 0, ""), is(-1));
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test
    public void testLessThan_3args_2() {
        assertThat(Arguments.lessThan(0L, 1, ""), is(0L));
        assertThat(Arguments.lessThan(20L, 30, ""), is(20L));
        assertThat(Arguments.lessThan(-1L, 0, ""), is(-1L));
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test
    public void testLessThan_3args_3() {
        assertThat(Arguments.lessThan(d(0), d(1), ""), is(d(0L)));
        assertThat(Arguments.lessThan(d(20L), d(30), ""), is(d(20L)));
        assertThat(Arguments.lessThan(d(-1L), d(0), ""), is(d(-1L)));
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_4() {
        Arguments.lessThan(1, 1, "");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_5() {
        Arguments.lessThan(2, 1, "");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_6() {
        Arguments.lessThan(1L, 1, "");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_7() {
        Arguments.lessThan(2L, 1, "");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_8() {
        Arguments.lessThan(d(1), d(1), "");
    }

    /**
     * Test of lessThan method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessThan_3args_9() {
        Arguments.lessThan(d(2), d(1), "");
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_2args_1() {
        Collection nonEmpty = null;
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_2args_1_2() {
        Collection nonEmpty = null;
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_2args_1_3() {
        List<String> nonEmpty = new ArrayList<>();
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test()
    public void testNonEmpty_2args_1_4() {
        List<String> nonEmpty = new ArrayList<>();
        nonEmpty.add("1");
        String varname = "arg";
        assertThat(Arguments.nonEmpty(nonEmpty, varname), is(sameInstance(nonEmpty)));
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_2args_2() {
        Object[] nonEmpty = null;
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_2args_2_2() {
        Object[] nonEmpty = {};
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test
    public void testNonEmpty_2args_2_3() {
        Object[] nonEmpty = {null};
        String varname = "arg";
        assertThat(Arguments.nonEmpty(nonEmpty, varname), is(sameInstance(nonEmpty)));
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test
    public void testNonEmpty_String_String() {
        String nonEmpty = "1";
        String varname = "arg";
        assertThat(Arguments.nonEmpty(nonEmpty, varname), is(sameInstance(nonEmpty)));
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_String_String_2() {
        String nonEmpty = "";
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     * Test of nonEmpty method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonEmpty_String_String_3() {
        String nonEmpty = null;
        String varname = "arg";
        Arguments.nonEmpty(nonEmpty, varname);
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonMinus() {
        Arguments.nonMinus(-1, "arg");
    }

    /**
     *
     */
    @Test
    public void testNonMinus_2() {
        assertThat(Arguments.nonMinus(0, "arg"), is(0l));
        assertThat(Arguments.nonMinus(10, "arg"), is(10l));
    }

    /**
     * Test of nonNull method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNull() {
        Object nonNull = null;
        String varname = "arg";
        Arguments.nonNull(nonNull, varname);
    }

    /**
     * Test of nonNullAll method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_2args_1() {
        Object[] args = {null};
        Arguments.nonNullAll(args, "args");
    }

    /**
     * Test of nonNullAll method, of class Arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_2args_2() {
        Iterable<?> args = Arrays.asList((Object) null);
        Arguments.nonNullAll(args, "args");
    }

    /**
     *
     */
    @Test
    public void testNonNullAll_Array() {
        String[] actual;
        String[] args = {"A", "B"};
        actual = Arguments.nonNullAll(args, "args");
        assertThat(actual, is(allOf(arrayWithSize(2), arrayContaining("A", "B"))));

        args = new String[]{};
        actual = Arguments.nonNullAll(args, "args");
        assertThat(actual, is(emptyArray()));
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_Array_2() {
        String[] args = {"A", null};
        Arguments.nonNullAll(args, "args");
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_Array_3() {
        String[] args = null;
        Arguments.nonNullAll(args, "arg");
    }

    /**
     *
     */
    @Test
    public void testNonNullAll_Iterable() {
        List<String> actual;
        List<String> args = Arrays.asList("A", "B");
        actual = Arguments.nonNullAll(args, "args");
        assertThat(actual, is(contains("A", "B")));

        args = new ArrayList<>();
        actual = Arguments.nonNullAll(args, "args");
        assertThat(actual, is(emptyIterable()));
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_Iterable_2() {
        List<String> args = Arrays.asList("A", null);
        Arguments.nonNullAll(args, "args");
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonNullAll_Iterable_3() {
        List<String> args = null;
        Arguments.nonNullAll(args, "arg");
    }

    /**
     * Test of nonNull method, of class Arguments.
     */
    @Test()
    public void testNonNull_2() {
        Object nonNull = new Object();
        String varname = "arg";
        assertThat(Arguments.nonNull(nonNull, varname), is(sameInstance(nonNull)));
    }

    private Date d(long i) {
        return new Date(i);
    }
}
