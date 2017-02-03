/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.datetime.DateOnly;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Consumer2;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.iteration.BreakException;
import info.naiv.lab.java.jmt.iteration.ContinueException;
import info.naiv.lab.java.jmt.iteration.LoopCondition;
import info.naiv.lab.java.jmt.monad.Iteratee;
import info.naiv.lab.java.jmt.monad.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Misc.class, Class.class})
public class MiscTest {

    static final Date NULL_DATE = null;

    static final Map NULL_MAP = null;
    static final Number NULL_NUMBER = null;
    static final String NULL_STRING = null;

    ClassPathXmlApplicationContext context;

    /**
     *
     */
    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("/META-INF/test-application-context.xml");
    }

    /**
     *
     */
    @After
    public void tearDown() {
        context.close();
    }

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
     * Test of compareEqual method, of class Misc.
     */
    @Test
    public void testCompareEqual() {
        BigDecimal bd1 = new BigDecimal("1.0");
        BigDecimal bd2 = new BigDecimal("1");
        BigDecimal bd3 = new BigDecimal("1");
        BigDecimal bd4 = new BigDecimal("1.1");

        assertThat(bd1, not(is(equalTo(bd2))));
        assertThat(bd2, (is(equalTo(bd3))));
        assertThat(bd3, not(is(equalTo(bd4))));
        assertThat(Misc.compareEqual(bd1, bd2), is(true));
        assertThat(Misc.compareEqual(bd2, bd3), is(true));
        assertThat(Misc.compareEqual(bd3, bd4), is(false));
    }

    /**
     * Test of concatnate method, of class Misc.
     */
    @Test
    public void testConcatnate() {
        String nil = null;
        assertThat(Misc.concatnate(), is(""));
        assertThat(Misc.concatnate(nil), is(""));
        assertThat(Misc.concatnate("ABC"), is("ABC"));
        assertThat(Misc.concatnate("A", "B", "CDEFG"), is("ABCDEFG"));
        assertThat(Misc.concatnate("A", nil, "B", "CDEFG"), is("ABCDEFG"));
    }

    /**
     * Test of contains method, of class Misc.
     */
    @Test
    public void testContains() {

        List<BigDecimal> source = Arrays.asList(
                BigDecimal.TEN,
                new BigDecimal("0.0"),
                BigDecimal.ONE
        );

        assertThat(Misc.contains(source, StandardFunctions.equal(BigDecimal.ZERO)), is(source.contains(BigDecimal.ZERO)));
        assertThat(Misc.contains(source, StandardFunctions.equal(new BigDecimal("0.0"))), is(source.contains(new BigDecimal("0.0"))));
        assertThat(Misc.contains(source, StandardFunctions.equal(BigDecimal.TEN)), is(source.contains(BigDecimal.TEN)));
    }

    /**
     * Test of containsCompareEquals method, of class Misc.
     */
    @Test
    public void testContainsCompareEquals() {

        List<BigDecimal> source = Arrays.asList(
                BigDecimal.TEN,
                new BigDecimal("0.0"),
                BigDecimal.ONE
        );
        assertThat(source.contains(BigDecimal.ZERO), is(false));
        assertThat(Misc.containsCompareEquals(source, BigDecimal.ZERO), is(true));
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
     *
     */
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
     * Test of equal method, of class Misc.
     */
    @Test
    public void testEqual() {
        assertThat("equals(1, 2)", Misc.equal(1, 2), is(false));
        assertThat("equals(4, 2)", Misc.equal(4, 2), is(false));
        assertThat("equals(3, 3)", Misc.equal(3, 3), is(true));
        assertThat("equals(null, 4)", Misc.equal(null, 4), is(false));
        assertThat("equals(5, null)", Misc.equal(5, null), is(false));
        assertThat("equals(null, null)", Misc.equal(null, null), is(true));
    }

    /**
     * Test of filter method, of class Misc.
     */
    @Test
    public void testFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3, 3, 4, 4, 5, 8);
        Iterable<Integer> a1 = Misc.filter(list, new Predicate1<Integer>() {
                                       @Override
                                       public boolean test(Integer obj) {
                                           return obj % 2 == 0;
                                       }
                                   });
        assertThat(a1, is(contains(2, 4, 4, 8)));
    }

    /**
     * Test of filter method, of class Misc.
     */
    @Test
    public void testFilter2() {
        List<Integer> list = Arrays.asList(1, 2, 3, 3, 4, 4, 5, 8);
        Iterable<Integer> a1 = Misc.filter(list, null);
        assertThat(a1, is(contains(1, 2, 3, 3, 4, 4, 5, 8)));
    }

    /**
     * Test of filter method, of class Misc.
     */
    @Test
    public void testFilter3() {
        Iterable<Integer> a1 = Misc.filter(null, new Predicate1<Integer>() {
                                       @Override
                                       public boolean test(Integer obj) {
                                           return obj % 2 == 0;
                                       }
                                   });
        assertThat(a1, is(emptyIterable()));
    }

    /**
     * Test of filterNonNull method, of class Misc.
     */
    @Test
    public void testFilterNonNull() {
        Iteratee<Object> iter1 = Misc.filterNonNull(null);
        assertThat(iter1, is(emptyIterable()));

        List<String> list1 = Arrays.asList();
        Iteratee<String> iter2 = Misc.filterNonNull(list1);
        assertThat(iter2, is(emptyIterable()));

        List<String> list2 = Arrays.asList(null, "123", null, "456", null);
        Iteratee<String> iter3 = Misc.filterNonNull(list2);
        assertThat(iter3, is(contains("123", "456")));
    }

    /**
     * Test of flat method, of class Misc.
     */
    @Test
    public void testFlat() {

        List<Integer> in1 = Arrays.asList(1, 2, 3);
        List<Integer> in2 = Arrays.asList(4, 5, 6);
        Iterable<Integer> result = Misc.flat(Arrays.asList(in1, in2));
        assertThat(result, is(contains(1, 2, 3, 4, 5, 6)));

    }

    /**
     * Test of forEach method, of class Misc.
     */
    @Test
    public void testForEach_Iterable_Consumer1() {
        final AtomicInteger ai = new AtomicInteger();
        Consumer1<Integer> x = new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                ai.addAndGet(a1);
            }
        };

        ai.set(0);
        Misc.forEach(null, x);
        assertThat(ai.get(), is(0));

        ai.set(0);
        Misc.forEach(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), x);
        assertThat(ai.get(), is(55));

    }

    /**
     * Test of forEach method, of class Misc.
     */
    @Test
    public void testForEach_Iterable_Consumer1_2() {
        final AtomicInteger ai = new AtomicInteger();
        Consumer1<Integer> x = new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                if (a1 == 6) {
                    throw new ContinueException();
                }
                if (a1 == 9) {
                    throw new BreakException();
                }
                ai.addAndGet(a1);
            }
        };

        ai.set(0);
        Misc.forEach(null, x);
        assertThat(ai.get(), is(0));

        ai.set(0);
        Misc.forEach(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), x);
        assertThat(ai.get(), is(30));

    }

    /**
     * Test of forEach method, of class Misc.
     */
    @Test
    public void testForEach_Iterable_Consumer2() {
        final AtomicInteger ai = new AtomicInteger();
        Consumer2<Integer, LoopCondition> x = new Consumer2<Integer, LoopCondition>() {

            @Override
            public void accept(Integer a1, LoopCondition a2) {
                assertThat(a2.index(), is(a1 - 1));
                ai.addAndGet(a1);
            }
        };

        ai.set(0);
        Misc.forEach(null, x);
        assertThat(ai.get(), is(0));

        ai.set(0);
        Misc.forEach(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), x);
        assertThat(ai.get(), is(55));
    }

    /**
     * Test of forEach method, of class Misc.
     */
    @Test
    public void testForEach_Iterable_Consumer2_2() {
        final AtomicInteger ai = new AtomicInteger();
        Consumer2<Integer, LoopCondition> x = new Consumer2<Integer, LoopCondition>() {

            @Override
            public void accept(Integer a1, LoopCondition a2) {
                if (a1 == 6) {
                    a2.doContinue();
                }
                if (a1 == 9) {
                    a2.doBreak();
                }
                ai.addAndGet(a1);
            }
        };

        ai.set(0);
        Misc.forEach(null, x);
        assertThat(ai.get(), is(0));

        ai.set(0);
        Misc.forEach(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), x);
        assertThat(ai.get(), is(30));

    }

    /**
     * Test of formatBytes method, of class Misc.
     */
    @Test
    public void testFormatBytes() {
        byte[] data = "abcあかさ".getBytes(StandardCharsets.UTF_8);
        assertThat(Misc.formatBytes(data, "%02x"), is("616263e38182e3818be38195"));
        assertThat(Misc.formatBytes(data, "%02X"), is("616263E38182E3818BE38195"));
    }

    /**
     * Test of getFirst method, of class Misc.
     */
    @Test
    public void testGetFirst_Iterable() {
        assertThat(Misc.getFirst(Arrays.asList(1, 2, 3)), is(1));
        assertThat(Misc.getFirst(Collections.EMPTY_LIST), is(nullValue()));
        assertThat(Misc.getFirst(null), is(nullValue()));
    }

    /**
     * Test of getFirst method, of class Misc.
     */
    @Test
    public void testGetFirst_Iterable_Predicate1() {

        Predicate1<Integer> pred = new Predicate1<Integer>() {

            @Override
            public boolean test(Integer obj) {
                return obj % 2 == 0;
            }
        };

        assertThat(Misc.getFirst(Arrays.asList(1, 2, 3), pred), is(2));
        assertThat(Misc.getFirst(Collections.EMPTY_LIST, pred), is(nullValue()));
        assertThat(Misc.getFirst(null, pred), is(nullValue()));
    }

    /**
     *
     */
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
    public void testIsEmpty_CharSequence() {
        assertThat(Misc.isEmpty((CharSequence) null), is(true));
        assertThat(Misc.isEmpty(""), is(true));
        assertThat(Misc.isEmpty("A"), is(false));
    }

    /**
     * Test of isEmpty method, of class Misc.
     */
    @Test
    public void testIsEmpty_Collection() {
        assertThat(Misc.isEmpty((Collection<?>) null), is(true));
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
        assertThat(Misc.isEmpty(NULL_MAP), is(true));
        Map<String, String> map = new HashMap<>();
        assertThat(Misc.isEmpty(map), is(true));
        map.put("key", "value");
        assertThat(Misc.isEmpty(map), is(false));
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
        assertThat(Misc.isNotEmpty((Collection<?>) null), is(false));
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
        Map nullMap = null;
        assertThat(Misc.isNotEmpty(nullMap), is(false));
        Map<String, String> map = new HashMap<>();
        assertThat(Misc.isNotEmpty(map), is(false));
        map.put("key", "value");
        assertThat(Misc.isNotEmpty(map), is(true));
    }

    /**
     * Test of join method, of class Misc.
     */
    @Test
    public void testJoin() {
        List<Object> obj = Arrays.asList((Object) 1, 2, "a");
        assertThat(Misc.join(obj, ","), is("1,2,a"));
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
                Misc.map(result, in, new Function1<Integer, String>() {
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
                Misc.map(result, in, new Function1<Integer, String>() {
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
    public void testMap_Iterable_Function1() {
        List<Integer> in = new ArrayList<>();
        in.add(1);
        in.add(2);
        in.add(3);
        in.add(4);
        in.add(5);
        in.add(6);
        Iterable<Integer> it = in;
        Iterable<Integer> result = Misc.map(it, new Function1<Integer, Integer>() {
                                        @Override
                                        public Integer apply(Integer a1) {
                                            return a1 % 3;
                                        }
                                    });
        assertThat(in.size(), is(6));
        assertThat(in, is(containsInAnyOrder(1, 2, 3, 4, 5, 6)));
        assertThat(result, is(contains(1, 2, 0, 1, 2, 0)));
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
     * Test of minmax method, of class Misc.
     */
    @Test
    public void testMinmax() {
        assertThat(Misc.minmax(1, 2, 5), is(2));
        assertThat(Misc.minmax(2, 2, 5), is(2));
        assertThat(Misc.minmax(3, 2, 5), is(3));
        assertThat(Misc.minmax(4, 2, 5), is(4));
        assertThat(Misc.minmax(5, 2, 5), is(5));
        assertThat(Misc.minmax(6, 2, 5), is(5));
    }

    /**
     * Test of newArrayList method, of class Misc.
     */
    @Test
    public void testNewArrayList() {
        ArrayList<Object> list1 = Misc.newArrayList();
        assertThat(list1, is(empty()));
        list1.add(new Object());
        assertThat(list1, hasSize(1));
    }

    /**
     * Test of newArrayList method, of class Misc.
     */
    @Test
    public void testNewArrayList2() {
        ArrayList<Integer> list1 = Misc.newArrayList(1, 2, 3);
        assertThat(list1, is(contains(1, 2, 3)));
        list1.add(4);
        assertThat(list1, is(contains(1, 2, 3, 4)));
    }

    /**
     * Test of newInstance method, of class Misc.
     */
    @Test
    public void testNewInstance_Class() {
        String str = Misc.newInstance(String.class);
        assertThat(str, is((Object) ""));
    }

    /**
     * Test of newInstance method, of class Misc.
     */
    @Test
    public void testNewInstance_Optional() {
        Optional<Class<String>> clz1 = Optional.of(String.class);
        Optional<String> opt1 = Misc.newInstance(clz1);
        assertThat(opt1.isPresent(), is(true));
        assertThat(opt1.get(), is(instanceOf(String.class)));
        assertThat(opt1.get(), is((Object) ""));

        Optional opt2 = Misc.newInstance(Optional.EMPTY);
        assertThat(opt2.isPresent(), is(false));
    }

    /**
     * Test of newInstance method, of class Misc.
     */
    @Test
    public void testNewInstance_String() {
        Optional opt1 = Misc.newInstance("java.lang.String");
        assertThat(opt1.isPresent(), is(true));
        assertThat(opt1.get(), is(instanceOf(String.class)));
        assertThat(opt1.get(), is((Object) ""));

        Optional opt2 = Misc.newInstance("test.Sample");
        assertThat(opt2.isPresent(), is(false));
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
     * Test of normalizeLineSeparator method, of class Misc.
     */
    @Test
    public void testNormalizeLineSeparator_String() {
        String text = "あいうえお\rかきくけこ\r\nさしすせそ\nたちつてと\u2028なにぬねの";
        String expected = "あいうえお\r\nかきくけこ\r\nさしすせそ\r\nたちつてと\r\nなにぬねの";
        String actual = Misc.normalizeLineSeparator(text);
        assertThat(actual, is(expected));
    }

    /**
     * Test of normalizeLineSeparator method, of class Misc.
     */
    @Test
    public void testNormalizeLineSeparator_String_String() {
        String text = "あいうえお\rかきくけこ\r\nさしすせそ\nたちつてと\u2028なにぬねの";
        String expected = "あいうえお<br>かきくけこ<br>さしすせそ<br>たちつてと<br>なにぬねの";
        String actual = Misc.normalizeLineSeparator(text, "<br>");
        assertThat(actual, is(expected));
    }

    /**
     * Test of objectToDouble method, of class Misc.
     */
    @Test
    public void testObjectToDouble() {
        Object ival = 12.0d;
        assertThat(Misc.objectToDouble(ival, 0), is(12d));
        ival = "-10.1235";
        assertThat(Misc.objectToDouble(ival, 0), is(-10.1235));
        ival = "Not a number";
        assertThat(Misc.objectToDouble(ival, 0), is(0d));
        ival = 4.5;
        assertThat(Misc.objectToDouble(ival, 0), is(4.5));
    }

    /**
     * Test of objectToInt method, of class Misc.
     */
    @Test
    public void testObjectToInt() {
        Object ival = 12;
        assertThat(Misc.objectToInt(ival, 0), is(12));
        ival = "11";
        assertThat(Misc.objectToInt(ival, 0), is(11));
        ival = "Not a number";
        assertThat(Misc.objectToInt(ival, 0), is(0));
        ival = 4.5;
        assertThat(Misc.objectToInt(ival, 0), is(4));
    }

    /**
     * Test of objectToLong method, of class Misc.
     */
    @Test
    public void testObjectToLong() {
        Object ival = 120L;
        assertThat(Misc.objectToLong(ival, 0), is(120L));
        ival = "11";
        assertThat(Misc.objectToLong(ival, 0), is(11L));
        ival = "Not a number";
        assertThat(Misc.objectToLong(ival, 0), is(0L));
        ival = 4.5;
        assertThat(Misc.objectToLong(ival, 0), is(4L));
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
     * Test of resolveClassName method, of class Misc.
     *
     * @throws java.lang.ClassNotFoundException
     */
    @Test
    public void testResolveClassName_3args() throws ClassNotFoundException, Exception {

        ClassLoader cl = ClassLoader.getSystemClassLoader();

        PowerMockito.spy(Class.class);
        PowerMockito.spy(Misc.class);

        Optional<Class<?>> clz1 = Optional.<Class<?>>of(String.class);
        Optional<Class<?>> clz2 = Optional.<Class<?>>of(Integer.class);
        assertThat(Misc.resolveClassName("java.lang.String", false, cl), is(clz1));
        assertThat(Misc.resolveClassName("java.lang.Integer", true, cl), is(clz2));

        PowerMockito.verifyStatic(times(1));
        Class.forName("java.lang.String", false, cl);

        PowerMockito.verifyStatic(times(1));
        Class.forName("java.lang.Integer", true, cl);

        assertThat(Misc.resolveClassName("test.Sample", true, cl), is(Optional.EMPTY));
    }

    /**
     * Test of resolveClassName method, of class Misc.
     */
    @Test
    public void testResolveClassName_String() {
        Optional<Class<?>> clz1 = Optional.<Class<?>>of(String.class);
        Optional<Class<?>> clz2 = Optional.<Class<?>>of(Integer.class);
        assertThat(Misc.resolveClassName("java.lang.String"), is(clz1));
        assertThat(Misc.resolveClassName("java.lang.Integer"), is(clz2));
        assertThat(Misc.resolveClassName("test.Sample"), is(Optional.EMPTY));
    }

    /**
     * Test of toBigDecimal method, of class Misc.
     */
    @Test
    public void testToBigDecimal_Number_BigDecimal() {
        assertThat(Misc.toBigDecimal(NULL_NUMBER, null), is(nullValue()));
        assertThat(Misc.toBigDecimal(NULL_NUMBER, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal(12, BigDecimal.ONE), is(BigDecimal.valueOf(12)));
        assertThat(Misc.toBigDecimal(12.5, BigDecimal.ONE), is(BigDecimal.valueOf(12.5)));
        assertThat(Misc.toBigDecimal(BigInteger.TEN, BigDecimal.ONE), is(BigDecimal.TEN));
    }

    /**
     * Test of toBigDecimal method, of class Misc.
     */
    @Test
    public void testToBigDecimal_String_BigDecimal() {
        assertThat(Misc.toBigDecimal(NULL_STRING, null), is(nullValue()));
        assertThat(Misc.toBigDecimal("", BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal("INT", BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(Misc.toBigDecimal("12", BigDecimal.ONE), is(BigDecimal.valueOf(12)));
        assertThat(Misc.toBigDecimal("12.5", BigDecimal.ONE), is(BigDecimal.valueOf(12.5)));
        assertThat(Misc.toBigDecimal("0.0000", BigDecimal.ONE), is(new BigDecimal("0.0000")));
    }

    /**
     * Test of toBoolean method, of class Misc.
     */
    @Test
    public void testToBoolean_Boolean_boolean() {
        Boolean nil = null;
        assertThat(Misc.toBoolean(nil, true), is(true));
        assertThat(Misc.toBoolean(nil, false), is(false));
        assertThat(Misc.toBoolean(Boolean.FALSE, true), is(false));
        assertThat(Misc.toBoolean(Boolean.TRUE, false), is(true));
    }

    /**
     * Test of toBoolean method, of class Misc.
     */
    @Test
    public void testToBoolean_Number_boolean() {
    }

    /**
     * Test of toBoolean method, of class Misc.
     */
    @Test
    public void testToBoolean_String_boolean() {
    }

    /**
     * Test of toByteArray method, of class Misc.
     */
    @Test
    public void testToByteArray_Resource() {
        String text = "あいうABC漢字テスト";
        String charset = "MS932";
        Charset cs = Charset.forName(charset);
        byte[] expected = text.getBytes(cs);

        Resource res = new ByteArrayResource(expected);
        assertArrayEquals(new byte[]{}, Misc.toByteArray(null));
        assertArrayEquals(expected, Misc.toByteArray(res));
    }

    /**
     * Test of toByteArray method, of class Misc.
     */
    @Test
    public void testToByteArray_String_String() {

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
        assertThat(Misc.toCalendar(NULL_DATE, null), is(nullValue()));
        assertThat(Misc.toCalendar(NULL_DATE, d), is(d));
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
        assertThat(Misc.toCalendar(NULL_STRING, null), is(nullValue()));
        assertThat(Misc.toCalendar(NULL_STRING, d), is(d));
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
        assertThat(Misc.toDoubleNum(NULL_NUMBER, 1d), is(1d));
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
        assertThat(Misc.toFloatNum(NULL_NUMBER, 1f), is(1f));
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
        assertThat(Misc.toIntNum(NULL_NUMBER, null), is(nullValue()));
        assertThat(Misc.toIntNum(NULL_NUMBER, 1), is(1));
        assertThat(Misc.toIntNum(2, 1), is(2));
        assertThat(Misc.toIntNum(12.5, 1), is(12));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToIntNum_String_Integer() {
        assertThat(Misc.toIntNum(NULL_STRING, null), is(nullValue()));
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
        assertThat(Misc.toInt(NULL_NUMBER, 1), is(1));
        assertThat(Misc.toInt(2, 1), is(2));
        assertThat(Misc.toInt(12.5, 1), is(12));
    }

    /**
     * Test of toInt method, of class Misc.
     */
    @Test
    public void testToInt_String_int() {
        assertThat(Misc.toInt(NULL_STRING, 1), is(1));
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
        assertThat(Misc.toLongNum(NULL_NUMBER, null), is(nullValue()));
        assertThat(Misc.toLongNum(NULL_NUMBER, 1L), is(1L));
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
        assertThat(Misc.toLongNum(NULL_STRING, null), is(nullValue()));
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
        assertThat(Misc.toLong(NULL_NUMBER, 1L), is(1L));
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
        assertThat(Misc.toLong(NULL_STRING, 1L), is(1L));
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
        assertThat(Misc.toNumber(NULL_STRING, null, null), is(nullValue()));
        assertThat(Misc.toNumber("2", 1, BigDecimal.class), is((Number) new BigDecimal(2)));
        assertThat(Misc.toNumber("12.5", 1L, Long.class), is((Number) 12L));
        assertThat(Misc.toNumber("123,456,789,910", 1L, Long.class), is((Number) 123456789910L));
    }

    /**
     * Test of toNumber method, of class Misc.
     */
    @Test
    public void testToNumber_String_Number() {
        assertThat(Misc.toNumber(NULL_STRING, null), is(nullValue()));
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
     * Test of toURL method, of class Misc.
     *
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testToURL() throws MalformedURLException {
        URL url = new URL("https://github.com/enlo/jmt-projects");
        assertThat(Misc.toURL("https://github.com/enlo/jmt-projects"), is(url));
    }

    /**
     * Test of toURL method, of class Misc.
     *
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testToURL_2() throws MalformedURLException {
        assertThat(Misc.toURL("jmt-projects"), is(nullValue()));
    }

    /**
     * Test of stringize method, of class Misc.
     */
    @Test
    public void testStringize_charArr() {
        assertThat(Misc.stringize(Misc.nullOf(char[].class)), is(""));
        assertThat(Misc.stringize(new char[]{}), is(""));
        assertThat(Misc.stringize(new char[]{'A'}), is("A"));
        assertThat(Misc.stringize(new char[]{'A', 'B'}), is("AB"));
    }

    /**
     * Test of stringize method, of class Misc.
     */
    @Test
    public void testStringize_Object() {
        assertThat(Misc.stringize(Misc.nullOf(Object.class)), is(""));
    }

    /**
     * Test of stringize method, of class Misc.
     */
    @Test
    public void testStringize_byteArr_Charset() {
        Charset cs1 = StandardCharsets.UTF_8;
        Charset cs2 = StandardCharsets.UTF_16;
        byte[] nil = null;
        byte[] utf8 = "いろはにほへと".getBytes(cs1);
        byte[] utf16 = "いろはにほへと".getBytes(cs2);

        assertThat(Misc.stringize(nil, cs1), is(""));
        assertThat(Misc.stringize(nil, cs2), is(""));
        assertThat("UTF8", Misc.stringize(utf8, cs1), is("いろはにほへと"));
        assertThat("UTF16", Misc.stringize(utf16, cs2), is("いろはにほへと"));
    }

    /**
     * Test of stringize method, of class Misc.
     */
    @Test
    public void testStringize_ByteBuffer_Charset() {
        Charset cs1 = StandardCharsets.UTF_8;
        Charset cs2 = StandardCharsets.UTF_16;
        ByteBuffer nil = null;
        ByteBuffer utf8 = cs1.encode("いろはにほへと");
        ByteBuffer utf16 = cs2.encode("いろはにほへと");

        assertThat(Misc.stringize(nil, cs1), is(""));
        assertThat(Misc.stringize(nil, cs2), is(""));
        assertThat("UTF8", Misc.stringize(utf8, cs1), is("いろはにほへと"));
        assertThat("UTF16", Misc.stringize(utf16, cs2), is("いろはにほへと"));
    }

    /**
     * Test of stringize method, of class Misc.
     */
    @Test
    public void testStringize_Object_Format() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String sym = format.getCurrency().getSymbol();
        assertThat(Misc.stringize(null, format), is(""));
        assertThat(Misc.stringize(123456789, null), is("123456789"));
        assertThat(Misc.stringize(123456789, format), is(sym + "123,456,789"));
    }

    /**
     * Test of toCharArray method, of class Misc.
     */
    @Test
    public void testToCharArray() {
        assertArrayEquals(new char[]{}, Misc.toCharArray(null));
        assertArrayEquals(new char[]{}, Misc.toCharArray(""));
        assertArrayEquals(new char[]{'A', 'B', 'C'}, Misc.toCharArray("ABC"));
        assertArrayEquals(new char[]{'A', 'B', 'C'}, Misc.toCharArray(CharBuffer.wrap("ABC")));
    }

    /**
     * Test of nullOf method, of class Misc.
     */
    @Test
    public void testNullOf() {
        assertThat(Misc.nullOf(Object.class), is(nullValue()));
        assertThat(Misc.nullOf(String.class), is(nullValue()));
        assertThat(Misc.nullOf(Integer.class), is(nullValue()));
        assertThat(Misc.nullOf(int[].class), is(nullValue()));
    }

    /**
     * Test of isNotEmpty method, of class Misc.
     */
    @Test
    public void testIsNotEmpty_CharSequence() {
        assertThat(Misc.isNotEmpty((String) null), is(false));
        assertThat(Misc.isNotEmpty(""), is(false));
        assertThat(Misc.isNotEmpty("A"), is(true));
    }

    /**
     * Test of splitKeyValue method, of class Misc.
     */
    @Test
    public void testSplitKeyValue() {

        String text1 = "abc = 1234";
        String text2 = "abc := 1234";
        String text3 = "abc ::= 1234";

        assertThat(Misc.splitKeyValue(text1, "=", true), is(KeyValuePair.of("abc", "1234")));
        assertThat(Misc.splitKeyValue(text1, "=", false), is(KeyValuePair.of("abc ", " 1234")));
        assertThat(Misc.splitKeyValue(text2, "=", true), is(KeyValuePair.of("abc :", "1234")));
        assertThat(Misc.splitKeyValue(text2, ":=", true), is(KeyValuePair.of("abc", "1234")));
        assertThat(Misc.splitKeyValue(text2, ":=", false), is(KeyValuePair.of("abc ", " 1234")));
        assertThat(Misc.splitKeyValue(text2, "::=", false), is(nullValue()));
        assertThat(Misc.splitKeyValue(text3, "::=", true), is(KeyValuePair.of("abc", "1234")));
        assertThat(Misc.splitKeyValue(text3, "::=", false), is(KeyValuePair.of("abc ", " 1234")));
        assertThat(Misc.splitKeyValue(null, "::=", false), is(nullValue()));
        assertThat(Misc.splitKeyValue("", ":=", false), is(nullValue()));

    }

    /**
     * Test of toStringList method, of class Misc.
     */
    @Test
    public void testToStringList() {
        Collection<?> items = Arrays.asList(1, "AAA", 2.5);
        assertThat(Misc.toStringList(items), contains("1", "AAA", "2.5"));

        Iterable<?> iter = Iteratee.of("abc", 22.0, "あいう");
        assertThat(Misc.toStringList(iter), contains("abc", "22.0", "あいう"));

        assertThat(Misc.toStringList(null), is(empty()));
        assertThat(Misc.toStringList(Collections.emptyList()), is(empty()));
    }

    /**
     * Test of asInt method, of class Misc.
     */
    @Test
    public void testAsInt() {
        assertThat(Misc.asInt(null, -1), is(-1));
        assertThat(Misc.asInt("", 2), is(2));
        assertThat(Misc.asInt("", 0), is(0));
        assertThat(Misc.asInt("4", 0), is(4));
        assertThat(Misc.asInt(new BigDecimal("5.5"), 0), is(5));
    }

    /**
     * Test of getOrDefault method, of class Misc.
     */
    @Test
    public void testGetOrDefault_3args_1() {
        String[] arr = null;
        assertThat(Misc.getOrDefault(arr, -1, "A"), is("A"));
        assertThat(Misc.getOrDefault(arr, 0, "B"), is("B"));
        assertThat(Misc.getOrDefault(arr, 1, "C"), is("C"));
        arr = new String[]{"X", "Y", "Z"};
        assertThat(Misc.getOrDefault(arr, -1, "A"), is("A"));
        assertThat(Misc.getOrDefault(arr, 0, "B"), is("X"));
        assertThat(Misc.getOrDefault(arr, 1, "C"), is("Y"));
        assertThat(Misc.getOrDefault(arr, 2, "D"), is("Z"));
        assertThat(Misc.getOrDefault(arr, 3, "E"), is("E"));
    }

    /**
     * Test of getOrDefault method, of class Misc.
     */
    @Test
    public void testGetOrDefault_3args_2() {
        List<String> list = null;
        assertThat(Misc.getOrDefault(list, -1, "A"), is("A"));
        assertThat(Misc.getOrDefault(list, 0, "B"), is("B"));
        assertThat(Misc.getOrDefault(list, 1, "C"), is("C"));
        list = Arrays.asList("X", "Y", "Z");
        assertThat(Misc.getOrDefault(list, -1, "A"), is("A"));
        assertThat(Misc.getOrDefault(list, 0, "B"), is("X"));
        assertThat(Misc.getOrDefault(list, 1, "C"), is("Y"));
        assertThat(Misc.getOrDefault(list, 2, "D"), is("Z"));
        assertThat(Misc.getOrDefault(list, 3, "E"), is("E"));
    }

    /**
     * Test of toLocale method, of class Misc.
     */
    @Test
    public void testToLocale() {
        assertThat(Misc.toLocale(null, Locale.ENGLISH), is(Locale.ENGLISH));
        assertThat(Misc.toLocale("ja_JP", Locale.ENGLISH), is(Locale.JAPAN));
        assertThat(Misc.toLocale(Locale.CANADA, Locale.ENGLISH), is(Locale.CANADA));
        Locale.Builder lb = new Locale.Builder();
        lb.setRegion("GB");
        lb.setLanguage("en");
        assertThat(Misc.toLocale(lb, Locale.ENGLISH), is(Locale.UK));
        assertThat(Misc.toLocale(1, Locale.ENGLISH), is(Locale.ENGLISH));
    }

    /**
     * Test of copyProperties method, of class Misc.
     */
    @Test
    public void testCopyProperties_2args() {
        SimpleBeanCopierTest.TestBeanS2 s = new SimpleBeanCopierTest.TestBeanS2();
        SimpleBeanCopierTest.TestBeanD2 d = new SimpleBeanCopierTest.TestBeanD2();
        Misc.copyProperties(s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
        assertThat(d.getAge(), is(s.getAge()));
        assertThat(d.getTel(), is(s.getTel()));
    }

    /**
     * Test of copyProperties method, of class Misc.
     */
    @Test
    public void testCopyProperties_3args() {
        SimpleBeanCopierTest.TestBeanS2 s = new SimpleBeanCopierTest.TestBeanS2();
        SimpleBeanCopierTest.TestBeanD2 d = new SimpleBeanCopierTest.TestBeanD2();
        Misc.copyProperties(s, d, "name");
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(nullValue()));
        assertThat(d.getAge(), is(s.getAge()));
        assertThat(d.getTel(), is(s.getTel()));
    }

    /**
     * Test of copyProperties method, of class Misc.
     */
    @Test
    public void testCopyProperties_3args_2() {
        SimpleBeanCopierTest.TestBeanS2 s = new SimpleBeanCopierTest.TestBeanS2();
        SimpleBeanCopierTest.TestBeanD2 d = new SimpleBeanCopierTest.TestBeanD2();
        Misc.copyProperties(s, d, "name", "id");
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(0));
        assertThat(d.getName(), is(nullValue()));
        assertThat(d.getAge(), is(s.getAge()));
        assertThat(d.getTel(), is(s.getTel()));
    }

    /**
     * Test of copyProperties method, of class Misc.
     */
    @Test
    public void testCopyProperties_4args() {
        SimpleBeanCopierTest.TestBeanS2 s = new SimpleBeanCopierTest.TestBeanS2();
        SimpleBeanCopierTest.TestBeanD2 d = new SimpleBeanCopierTest.TestBeanD2();
        Misc.copyProperties(SimpleBeanCopierTest.TestBeanS.class, SimpleBeanCopierTest.TestBeanD.class, s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
        assertThat(d.getAge(), is(0));
        assertThat(d.getTel(), is(nullValue()));
    }
    
    /**
     * Test of copyProperties method, of class Misc.
     */
    @Test
    public void testCopyProperties_5args() {
        SimpleBeanCopierTest.TestBeanS2 s = new SimpleBeanCopierTest.TestBeanS2();
        SimpleBeanCopierTest.TestBeanD2 d = new SimpleBeanCopierTest.TestBeanD2();
        Misc.copyProperties(SimpleBeanCopierTest.TestBeanS.class, SimpleBeanCopierTest.TestBeanD.class, s, d, "name", "id");
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(0));
        assertThat(d.getName(), is(nullValue()));
        assertThat(d.getAge(), is(0));
        assertThat(d.getTel(), is(nullValue()));
    }
    

    /**
     * Test of removeByteOrderMark method, of class Misc.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRemoveByteOrderMark() throws IOException {
        Resource nobomres = context.getResource("classpath:TEXT/nobomtext.txt");
        Resource bomres = context.getResource("classpath:TEXT/bomtext.txt");
        try (InputStream nobomis = nobomres.getInputStream();
             InputStream bomis = bomres.getInputStream()) {
            String nobomtext = IOUtils.toString(nobomis, StandardCharsets.UTF_8);
            String bomtext = IOUtils.toString(bomis, StandardCharsets.UTF_8);
            assertThat(bomtext, is(not(nobomtext)));

            String actual = Misc.removeByteOrderMark(bomtext);
            assertThat(actual, is(nobomtext));
        }
    }

}
