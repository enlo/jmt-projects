/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author enlo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/test-application-context.xml")
public class MiscTest {

    @Autowired
    ApplicationContext context;

    /**
     * Test of addIfNotFound method, of class Misc.
     */
    @Test
    public void testAddIfNotFound() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertThat(Misc.addIfNotFound(list, "a"), is(false));
        assertThat(Misc.addIfNotFound(list, "b"), is(false));
        assertThat(Misc.addIfNotFound(list, "c"), is(false));
        assertThat(list, is(contains("a", "b", "c")));

        assertThat(Misc.addIfNotFound(list, "d"), is(true));
        assertThat(list, is(contains("a", "b", "c", "d")));
    }

    /**
     * Test of advance method, of class Misc.
     */
    @Test
    public void testAdvance() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Iterator<Integer> result = Misc.advance(list.iterator(), 4);
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(5));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(6));
        assertThat(result.hasNext(), is(true));
        assertThat(result.next(), is(7));
        assertThat(result.hasNext(), is(false));
    }

    /**
     * Test of advance method, of class Misc.
     */
    @Test
    public void testAdvance_2() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Iterator<Integer> result = Misc.advance(list.iterator(), 10);
        assertThat(result.hasNext(), is(false));
    }

    /**
     * Test of between method, of class Misc.
     */
    @Test
    public void testBetween() {
        int[] values = {4, 5, 6, 10, 14, 15, 16};
        boolean[] expecteds = {false, true, true, true, true, true, false};
        int from = 5;
        int to = 15;
        for (int i = 0; i < values.length; i++) {
            int value = values[i];
            assertThat(Misc.between(value, from, to), is(expecteds[i]));
        }
    }

    /**
     * Test of between method, of class Misc.
     */
    @Test
    public void testBetween_null() {
        Integer x = 10;
        Integer from = 5;
        Integer to = 15;
        assertThat(Misc.between(null, from, to), is(false));
        assertThat(Misc.between(x, null, to), is(false));
        assertThat(Misc.between(x, from, null), is(false));
        assertThat(Misc.between(x, from, to), is(true));
    }

    /**
     * Test of arrayContainsCompareEquals method, of class Misc.
     */
    @Test
    public void testContainsCompareEquals_Collection_GenericType() {
        List<BigDecimal> list = null;
        assertThat(Misc.containsCompareEquals(list, null), is(false));
        assertThat(Misc.containsCompareEquals(list, BigDecimal.ZERO), is(false));

        list = Arrays.asList(new BigDecimal("0.0"), new BigDecimal("1.0"));
        assertThat(list.contains(new BigDecimal("0")), is(false));
        assertThat(list.contains(new BigDecimal("1")), is(false));
        assertThat(list.contains(new BigDecimal("2")), is(false));
        assertThat(Misc.containsCompareEquals(list, new BigDecimal("0")), is(true));
        assertThat(Misc.containsCompareEquals(list, new BigDecimal("1")), is(true));
        assertThat(Misc.containsCompareEquals(list, new BigDecimal("2")), is(false));
    }

    /**
     * Test of contains method, of class Misc.
     */
    @Test
    public void testContains_Collection_Function1() {
        final BigDecimal bd = BigDecimal.ZERO;
        Function1<Boolean, BigDecimal> predicate = new Function1<Boolean, BigDecimal>() {
            @Override
            public Boolean apply(BigDecimal a1) {
                return bd.compareTo(a1) == 0;
            }
        };

        List<BigDecimal> list = null;
        assertThat(Misc.contains(list, predicate), is(false));

        list = Arrays.asList(new BigDecimal("0.0"), new BigDecimal("1.0"));
        assertThat(list.contains(new BigDecimal("0")), is(false));
        assertThat(list.contains(new BigDecimal("1")), is(false));
        assertThat(list.contains(new BigDecimal("2")), is(false));
        assertThat(Misc.contains(list, predicate), is(true));

        list = Arrays.asList(new BigDecimal("1.0"), new BigDecimal("2.0"));
        assertThat(Misc.contains(list, predicate), is(false));
    }

    @Test
    public void testContains_Collection_Predicate1() {
        final BigDecimal bd = BigDecimal.ZERO;
        Predicate1<BigDecimal> predicate = new Predicate1<BigDecimal>() {
            @Override
            public boolean test(BigDecimal a1) {
                return bd.compareTo(a1) == 0;
            }
        };

        List<BigDecimal> list = null;
        assertThat(Misc.contains(list, predicate), is(false));

        list = Arrays.asList(new BigDecimal("0.0"), new BigDecimal("1.0"));
        assertThat(list.contains(new BigDecimal("0")), is(false));
        assertThat(list.contains(new BigDecimal("1")), is(false));
        assertThat(list.contains(new BigDecimal("2")), is(false));
        assertThat(Misc.contains(list, predicate), is(true));

        list = Arrays.asList(new BigDecimal("1.0"), new BigDecimal("2.0"));
        assertThat(Misc.contains(list, predicate), is(false));
    }

    /**
     * Test of equals method, of class Misc.
     */
    @Test
    public void testEquals() {
        assertThat("equals(1, 2)", Misc.equals(1, 2), is(false));
        assertThat("equals(4, 2)", Misc.equals(4, 2), is(false));
        assertThat("equals(3, 3)", Misc.equals(3, 3), is(true));
        assertThat("equals(null, 4)", Misc.equals(null, 4), is(false));
        assertThat("equals(5, null)", Misc.equals(5, null), is(false));
        assertThat("equals(null, null)", Misc.equals(null, null), is(true));
    }

    @Test
    public void testGetFirst() {
        assertThat(Misc.getFirst(Arrays.asList(1, 2, 3)), is(1));
        assertThat(Misc.getFirst(Collections.EMPTY_LIST), is(nullValue()));
        assertThat(Misc.getFirst(null), is(nullValue()));
    }

    @Test
    public void testGetKeySetByValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 1);
        map.put("D", 3);
        assertThat(Misc.getKeySetByValue(map, 1), is(containsInAnyOrder("A", "C")));
        assertThat(Misc.getKeySetByValue(map, 2), is(containsInAnyOrder("B")));
        assertThat(Misc.getKeySetByValue(map, 3), is(containsInAnyOrder("D")));
        assertThat(Misc.getKeySetByValue(map, 4), is(empty()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetKeySetByValue_2() {
        Misc.getKeySetByValue(null, null);
    }

    /**
     * Test of isBlank method, of class Misc.
     */
    @Test
    public void testIsBlank() {
        assertThat("null", Misc.isBlank(null), is(true));
        assertThat("empty", Misc.isBlank(""), is(true));
        assertThat("SPACE", Misc.isBlank("  "), is(true));
        assertThat("TAB", Misc.isBlank("\t"), is(true));
        assertThat("NEWLINE", Misc.isBlank(Constants.CRLF), is(true));
        assertThat("ZWNBSP", Misc.isBlank(Constants.ZWNBSP), is(true));
        assertThat("ZWNBSP", Misc.isBlank("a"), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_Collection() {
        assertThat(Misc.isEmpty((List) null), is(true));
        assertThat(Misc.isEmpty(Arrays.asList()), is(true));
        assertThat(Misc.isEmpty(Arrays.asList("")), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_GenericType() {
        assertThat(Misc.isEmpty((String[]) null), is(true));
        assertThat(Misc.isEmpty(new String[]{}), is(true));
        assertThat(Misc.isEmpty(new String[]{""}), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_Iterable() {
        assertThat(Misc.isEmpty((Iterable) null), is(true));
        assertThat(Misc.isEmpty((Iterable) Arrays.asList()), is(true));
        assertThat(Misc.isEmpty((Iterable<String>) Arrays.asList("")), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_Map() {
        assertThat(Misc.isEmpty((Map) null), is(true));
        Map<String, String> map = new HashMap<>();
        assertThat(Misc.isEmpty(map), is(true));
        map.put("key", "value");
        assertThat(Misc.isEmpty(map), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_String() {
        assertThat(Misc.isEmpty((String) null), is(true));
        assertThat(Misc.isEmpty(""), is(true));
        assertThat(Misc.isEmpty("A"), is(false));
    }

    /**
     * Test of isNotBlank method, of class Misc.
     */
    @Test
    public void testIsNotBlank() {
        assertThat("null", Misc.isNotBlank(null), is(false));
        assertThat("empty", Misc.isNotBlank(""), is(false));
        assertThat("SPACE", Misc.isNotBlank("  "), is(false));
        assertThat("TAB", Misc.isNotBlank("\t"), is(false));
        assertThat("NEWLINE", Misc.isNotBlank(Constants.CRLF), is(false));
        assertThat("ZWNBSP", Misc.isNotBlank(Constants.ZWNBSP), is(false));
        assertThat("a", Misc.isNotBlank("a"), is(true));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_Collection() {
        assertThat(Misc.isNotEmpty((List) null), is(false));
        assertThat(Misc.isNotEmpty(Arrays.asList()), is(false));
        assertThat(Misc.isNotEmpty(Arrays.asList("")), is(true));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_GenericType() {
        assertThat(Misc.isNotEmpty((String[]) null), is(false));
        assertThat(Misc.isNotEmpty(new String[]{}), is(false));
        assertThat(Misc.isNotEmpty(new String[]{""}), is(true));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_Iterable() {
        assertThat(Misc.isNotEmpty((Iterable) null), is(false));
        assertThat(Misc.isNotEmpty((Iterable) Arrays.asList()), is(false));
        assertThat(Misc.isNotEmpty((Iterable<String>) Arrays.asList("")), is(true));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_Map() {
        assertThat(Misc.isNotEmpty((Map) null), is(false));
        Map<String, String> map = new HashMap<>();
        assertThat(Misc.isNotEmpty(map), is(false));
        map.put("key", "value");
        assertThat(Misc.isNotEmpty(map), is(true));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_String() {
        assertThat(Misc.isNotEmpty((String) null), is(false));
        assertThat(Misc.isNotEmpty(""), is(false));
        assertThat(Misc.isNotEmpty("A"), is(true));
    }

    /**
     * Test of join method, of class Misc.
     */
    @Test
    public void testJoin_Iterable_String() {
        List<Object> obj = Arrays.asList((Object) 1, 2, "a");
        assertThat(Misc.join(obj, ","), is("1,2,a"));
    }

    /**
     * Test of join method, of class Misc.
     */
    @Test
    public void testJoin_ObjectArr() {
        assertThat(Misc.join("a", "b", "c"), is("abc"));
        assertThat(Misc.join("a", null, "c"), is("ac"));
    }

    /**
     * Test of map method, of class Misc.
     */
    @Test
    public void testMap_3args_1() {
        Set<String> result = new HashSet<>();
        Set<Integer> in = new HashSet<>();
        in.add(12);
        in.add(24);
        in.add(36);
        assertThat(
                Misc.map(result, in, new Function1<String, Integer>() {
                    @Override
                    public String apply(Integer a1) {
                        return a1.toString();
                    }
                }), is(sameInstance(result)));
        assertThat(in.size(), is(3));
        assertThat(in, is(containsInAnyOrder(12, 24, 36)));
        assertThat(result.size(), is(3));
        assertThat(result, is(containsInAnyOrder("12", "24", "36")));
    }

    /**
     * Test of map method, of class Misc.
     */
    @Test
    public void testMap_3args_2() {
        Map<String, String> result = new HashMap<>();
        Map<String, Integer> in = new HashMap<>();
        in.put("A", 12);
        in.put("B", 24);
        in.put("C", 36);
        assertThat(
                Misc.map(result, in, new Function1<String, Integer>() {
                    @Override
                    public String apply(Integer a1) {
                        return a1.toString();
                    }
                }), is(sameInstance(result)));
        assertThat(in.size(), is(3));
        assertThat(in, hasEntry("A", 12));
        assertThat(in, hasEntry("B", 24));
        assertThat(in, hasEntry("C", 36));
        assertThat(result.size(), is(3));
        assertThat(result, hasEntry("A", "12"));
        assertThat(result, hasEntry("B", "24"));
        assertThat(result, hasEntry("C", "36"));
    }

    /**
     * Test of map method, of class Misc.
     */
    @Test
    public void testMap_List_Function1() {
        List<Integer> in = new ArrayList<>();
        in.add(1);
        in.add(2);
        in.add(3);
        in.add(4);
        in.add(5);
        in.add(6);
        List<Integer> result = Misc.map(in, new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer a1) {
                return a1 % 3;
            }
        });
        assertThat(in.size(), is(6));
        assertThat(in, is(containsInAnyOrder(1, 2, 3, 4, 5, 6)));
        assertThat(result.size(), is(6));
        assertThat(result, is(contains(1, 2, 0, 1, 2, 0)));
    }

    /**
     * Test of map method, of class Misc.
     */
    @Test
    public void testMap_Set_Function1() {
        Set<Integer> in = new HashSet<>();
        in.add(1);
        in.add(2);
        in.add(3);
        in.add(4);
        in.add(5);
        in.add(6);
        Set<Integer> result = Misc.map(in, new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer a1) {
                return a1 % 3;
            }
        });
        assertThat(in.size(), is(6));
        assertThat(in, is(containsInAnyOrder(1, 2, 3, 4, 5, 6)));
        assertThat(result.size(), is(3));
        assertThat(result, is(containsInAnyOrder(0, 1, 2)));
    }

    /**
     * Test of min method, of class Misc.
     */
    @Test
    public void testMin() {
        assertThat("min(1, 2)", Misc.min(1, 2), is(1));
        assertThat("min(4, 2)", Misc.min(4, 2), is(2));
        assertThat("min(3, 3)", Misc.min(3, 3), is(3));
        assertThat("min(null, 4)", Misc.min(null, 4), is(4));
        assertThat("min(5, null)", Misc.min(5, null), is(5));
        assertThat("min(null, null)", Misc.min(null, null), is(nullValue()));
    }

    /**
     * Test of nop method, of class Misc.
     */
    @Test
    public void testNop() {
        Object x = null;
        Misc.nop(x);
    }

    /**
     * Test of removeZwnbsp method, of class Misc.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRemoveZwnbsp() throws IOException {
        Resource nobomres = context.getResource("classpath:TEXT/nobomtext.txt");
        Resource bomres = context.getResource("classpath:TEXT/bomtext.txt");
        try (InputStream nobomis = nobomres.getInputStream();//
             InputStream bomis = bomres.getInputStream()) {
            String nobomtext = IOUtils.toString(nobomis, StandardCharsets.UTF_8);
            String bomtext = IOUtils.toString(bomis, StandardCharsets.UTF_8);
            assertThat(bomtext, is(not(nobomtext)));

            String actual = Misc.removeZwnbsp(bomtext);
            assertThat(actual, is(nobomtext));
        }
    }

    /**
     * Test of repeat method, of class Misc.
     */
    @Test
    public void testRepeat() {
        String x = "A";
        assertThat(Misc.repeat(-1, x), is(emptyIterable()));
        assertThat(Misc.repeat(0, x), is(emptyIterable()));
        assertThat(Misc.repeat(1, x), is(containsInAnyOrder("A")));
        assertThat(Misc.repeat(2, x), is(contains("A", "A")));
    }

    /**
     * Test of toBigDecimal method, of class Misc.
     */
    @Test
    public void testToBigDecimal_Number_BigDecimal() {
        assertThat(Misc.toBigDecimal((Number) null, null), is(nullValue()));
        assertThat(Misc.toBigDecimal((Number) null, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal(12, BigDecimal.ONE), is(BigDecimal.valueOf(12)));
        assertThat(Misc.toBigDecimal(12.5, BigDecimal.ONE), is(BigDecimal.valueOf(12.5)));
        assertThat(Misc.toBigDecimal(BigInteger.TEN, BigDecimal.ONE), is(BigDecimal.TEN));
    }

    /**
     * Test of toBigDecimal method, of class Misc.
     */
    @Test
    public void testToBigDecimal_String_BigDecimal() {
        assertThat(Misc.toBigDecimal((String) null, null), is(nullValue()));
        assertThat(Misc.toBigDecimal("", BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal("INT", BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal("12", BigDecimal.ONE), is(BigDecimal.valueOf(12)));
        assertThat(Misc.toBigDecimal("12.5", BigDecimal.ONE), is(BigDecimal.valueOf(12.5)));
        assertThat(Misc.toBigDecimal("0.0000", BigDecimal.ONE), is(new BigDecimal("0.0000")));
    }

    /**
     * Test of toByteArray method, of class Misc.
     */
    @Test
    public void testToByteArray() {

        String text = "あいうABC漢字テスト";
        String charset = "MS932";
        Charset cs = Charset.forName(charset);

        assertArrayEquals(new byte[]{}, Misc.toByteArray(null, charset));
        assertArrayEquals(text.getBytes(cs), Misc.toByteArray(text, charset));
    }

    /**
     * Test of toCalendar method, of class Misc.
     */
    @Test
    public void testToCalendar_Date_Calendar() {
        Calendar d = Calendar.getInstance();
        assertThat(Misc.toCalendar((Date) null, null), is(nullValue()));
        assertThat(Misc.toCalendar((Date) null, d), is(d));
        d = new GregorianCalendar(2015, Calendar.MARCH, 12);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        assertThat(Misc.toCalendar(now, null), is(comparesEqualTo(cal)));

        Date date = d.getTime();
        assertThat(Misc.toCalendar(date, null), is(comparesEqualTo(d)));
    }

    /**
     * Test of toCalendar method, of class Misc.
     */
    @Test
    public void testToCalendar_String_Calendar() {
        Calendar d = Calendar.getInstance();
        assertThat(Misc.toCalendar((String) null, null), is(nullValue()));
        assertThat(Misc.toCalendar((String) null, d), is(d));
        d = new GregorianCalendar(2015, Calendar.MARCH, 12);
        assertThat(Misc.toCalendar("2015.03.12", null), is(comparesEqualTo(d)));
        assertThat(Misc.toCalendar("2015/3/12", null), is(comparesEqualTo(d)));
        assertThat(Misc.toCalendar("2015-03-12", null), is(comparesEqualTo(d)));
        d = new GregorianCalendar(2015, Calendar.MARCH, 12, 13, 34, 50);
        assertThat(Misc.toCalendar("2015-03-12T13:34:50", null), is(comparesEqualTo(d)));
    }

    /**
     * Test of toDate method, of class Misc.
     */
    @Test
    public void testToDate() {
        Date d = new Date();
        assertThat(Misc.toDate(null, null), is(nullValue()));
        assertThat(Misc.toDate(null, d), is(d));
        d = new GregorianCalendar(2015, Calendar.MARCH, 12).getTime();
        assertThat(Misc.toDate("2015.03.12", null), is(d));
        assertThat(Misc.toDate("2015/3/12", null), is(d));
        assertThat(Misc.toDate("2015-03-12", null), is(d));
        d = new GregorianCalendar(2015, Calendar.MARCH, 12, 13, 34, 50).getTime();
        assertThat(Misc.toDate("2015-03-12T13:34:50", null), is(d));
    }

    /**
     * Test of toDoubleNum method, of class Misc.
     */
    @Test
    public void testToDoubleNum() {
        assertThat(Misc.toDoubleNum((Number) null, 1d), is(1d));
        assertThat(Misc.toDoubleNum(2, 1d), is(2d));
        assertThat(Misc.toDoubleNum(12.5, 1d), is(12.5d));
        assertThat(Misc.toDoubleNum(Double.MAX_VALUE, 1d), is(Double.MAX_VALUE));
        assertThat(Misc.toDoubleNum(Double.MIN_VALUE, 1d), is(Double.MIN_VALUE));
    }

    /**
     * Test of toFloatNum method, of class Misc.
     */
    @Test
    public void testToFloatNum() {
        assertThat(Misc.toFloatNum((Number) null, 1f), is(1f));
        assertThat(Misc.toFloatNum(2, 1f), is(2f));
        assertThat(Misc.toFloatNum(12.5, 1f), is(12.5f));
        assertThat(Misc.toFloatNum(Double.MAX_VALUE, 1f), is(Float.POSITIVE_INFINITY));
        assertThat(Misc.toFloatNum(-0d, 1f), is(-0f));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToIntNum_Number_Integer() {
        assertThat(Misc.toIntNum((Number) null, null), is(nullValue()));
        assertThat(Misc.toIntNum((Number) null, 1), is(1));
        assertThat(Misc.toIntNum(2, 1), is(2));
        assertThat(Misc.toIntNum(12.5, 1), is(12));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToIntNum_String_Integer() {
        assertThat(Misc.toIntNum((String) null, null), is(nullValue()));
        assertThat(Misc.toIntNum("", 1), is(1));
        assertThat(Misc.toIntNum("2", 1), is(2));
        assertThat(Misc.toIntNum("12.5", 1), is(12));
        assertThat(Misc.toIntNum("123,456", 1), is(123456));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToInt_Number_int() {
        assertThat(Misc.toInt((Number) null, 1), is(1));
        assertThat(Misc.toInt(2, 1), is(2));
        assertThat(Misc.toInt(12.5, 1), is(12));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToInt_String_int() {
        assertThat(Misc.toInt((String) null, 1), is(1));
        assertThat(Misc.toInt("", 1), is(1));
        assertThat(Misc.toInt("2", 1), is(2));
        assertThat(Misc.toInt("12.5", 1), is(12));
        assertThat(Misc.toInt("123,456", 1), is(123456));
    }

    /**
     * Test of toList method, of class Misc.
     */
    @Test
    public void testToList() {
        final List<String> obj = Arrays.asList("a", "b", "c");
        List<String> res1 = Misc.toList(obj);
        assertThat(res1, is(not(sameInstance(obj))));
        assertThat(res1, is(contains("a", "b", "c")));

        Iterable<String> itr = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return obj.iterator();
            }
        };
        List<String> res2 = Misc.toList(itr);
        assertThat(res2, is(not(sameInstance(obj))));
        assertThat(res2, is(contains("a", "b", "c")));
    }

    /**
     * Test of toLong method, of class Misc.
     */
    @Test
    public void testToLongNum_Number_Long() {
        assertThat(Misc.toLongNum((Number) null, null), is(nullValue()));
        assertThat(Misc.toLongNum((Number) null, 1L), is(1L));
        assertThat(Misc.toLongNum(2, 1L), is(2L));
        assertThat(Misc.toLongNum(12.5, 1L), is(12L));
        assertThat(Misc.toLongNum(Long.MAX_VALUE, 1L), is(Long.MAX_VALUE));
        assertThat(Misc.toLongNum(Long.MIN_VALUE, 1L), is(Long.MIN_VALUE));
    }

    /**
     * Test of toLongNum method, of class Misc.
     */
    @Test
    public void testToLongNum_String_Long() {
        assertThat(Misc.toLongNum((String) null, null), is(nullValue()));
        assertThat(Misc.toLongNum("", 1L), is(1L));
        assertThat(Misc.toLongNum("2", 1L), is(2L));
        assertThat(Misc.toLongNum("12.5", 1L), is(12L));
        assertThat(Misc.toLongNum("123,456,789,910", 1L), is(123456789910L));
        assertThat(Misc.toLongNum(Long.toString(Long.MAX_VALUE), 1L), is(Long.MAX_VALUE));
        assertThat(Misc.toLongNum(Long.toString(Long.MIN_VALUE), 1L), is(Long.MIN_VALUE));
    }

    /**
     * Test of toLong method, of class Misc.
     */
    @Test
    public void testToLong_Number_long() {
        assertThat(Misc.toLong((Number) null, 1L), is(1L));
        assertThat(Misc.toLong(2, 1L), is(2L));
        assertThat(Misc.toLong(12.5, 1L), is(12L));
        assertThat(Misc.toLong(Long.MAX_VALUE, 1L), is(Long.MAX_VALUE));
        assertThat(Misc.toLong(Long.MIN_VALUE, 1L), is(Long.MIN_VALUE));
    }

    /**
     * Test of toLong method, of class Misc.
     */
    @Test
    public void testToLong_String_long() {
        assertThat(Misc.toLong((String) null, 1L), is(1L));
        assertThat(Misc.toLong("", 1L), is(1L));
        assertThat(Misc.toLong("2", 1L), is(2L));
        assertThat(Misc.toLong("12.5", 1L), is(12L));
        assertThat(Misc.toLong("123,456,789,910", 1L), is(123456789910L));
        assertThat(Misc.toLong(Long.toString(Long.MAX_VALUE), 1L), is(Long.MAX_VALUE));
        assertThat(Misc.toLong(Long.toString(Long.MIN_VALUE), 1L), is(Long.MIN_VALUE));
    }

    /**
     * Test of toNumber method, of class Misc.
     */
    @Test
    public void testToNumber_3args() {
        assertThat(Misc.toNumber((String) null, null, null), is(nullValue()));
        assertThat(Misc.toNumber("2", 1, BigDecimal.class), is((Number) new BigDecimal(2)));
        assertThat(Misc.toNumber("12.5", 1L, Long.class), is((Number) 12L));
        assertThat(Misc.toNumber("123,456,789,910", 1L, Long.class), is((Number) 123456789910L));
    }

    /**
     * Test of toNumber method, of class Misc.
     */
    @Test
    public void testToNumber_String_Number() {
        assertThat(Misc.toNumber((String) null, null), is(nullValue()));
        assertThat(Misc.toNumber("", 1), is((Number) 1));
        assertThat(Misc.toNumber("2", 1), is((Number) 2L));
        assertThat(Misc.toNumber("12.5", 1L), is((Number) 12.5));
        assertThat(Misc.toNumber("123,456,789,910", 1L), is((Number) 123456789910L));
    }

    /**
     * Test of toString method, of class Misc.
     */
    @Test
    public void testToString_3args() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String sym = format.getCurrency().getSymbol();
        assertThat(Misc.toString(null, null, null), is(nullValue()));
        assertThat(Misc.toString(null, null, "12"), is("12"));
        assertThat(Misc.toString(123456789, null, null), is("123456789"));
        assertThat(Misc.toString(123456789, format, null), is(sym + "123,456,789"));
    }

    /**
     * Test of toString method, of class Misc.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testToString_4args() throws UnsupportedEncodingException {
        String source = "ABC漢字ｶﾅ";
        String[] csl = {"UTF8", "MS932", "ISO-2022-JP", "Unicode"};
        for (String cs : csl) {
            byte[] data = source.getBytes(cs);
            assertThat(cs, Misc.toString(data, 3, 5, cs), is(new String(data, 3, 5, cs)));
        }
    }

    /**
     * Test of toString method, of class Misc.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testToString_byteArr_String() throws UnsupportedEncodingException {
        String source = "ABC漢字ｶﾅ";
        String[] csl = {"UTF8", "MS932", "ISO-2022-JP", "Unicode"};
        for (String cs : csl) {
            byte[] data = source.getBytes(cs);
            assertThat(cs, Misc.toString(data, cs), is(source));
        }
    }

    /**
     * Test of isLoadable method, of class Misc.
     */
    @Test
    public void testIsLoadable_String() {
        assertThat(Misc.isLoadable("java.lang.String"), is(true));
        assertThat(Misc.isLoadable("my.sample.Class"), is(false));
    }

    /**
     * Test of isLoadable method, of class Misc.
     *
     * @throws java.lang.ClassNotFoundException
     */
    @Test
    public void testIsLoadable_String_ClassLoader() throws ClassNotFoundException {

        ClassLoader cl = Mockito.spy(ClassLoader.class);
        assertThat(Misc.isLoadable("java.lang.String", cl), is(true));
        verify(cl, times(1)).loadClass("java.lang.String");

        assertThat(Misc.isLoadable("my.sample.Class", cl), is(false));
        verify(cl, times(1)).loadClass("my.sample.Class");
    }

}
