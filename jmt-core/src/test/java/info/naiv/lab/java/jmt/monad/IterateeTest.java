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
package info.naiv.lab.java.jmt.monad;

import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class IterateeTest {

    /**
     *
     */
    public IterateeTest() {
    }

    /**
     * Test of bind method, of class Iteratee.
     */
    @Test
    public void testBind() {
        Iteratee<Integer> im = Iteratee.of(1, 2, 3, 4, 5);
        final StringBuilder sb = new StringBuilder();
        assertThat(im.bind(new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                sb.append(a1);
            }
        }), is(sameInstance(im)));
        assertThat(sb.toString(), is("12345"));
    }

    /**
     * Test of bind method, of class Iteratee.
     */
    @Test
    public void testBind_empty() {
        Iteratee<Integer> im = Iteratee.empty();
        final StringBuilder sb = new StringBuilder();
        assertThat(im.bind(new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                sb.append(a1);
            }
        }), is(sameInstance(im)));
        assertThat(sb.toString(), is(""));
    }

    /**
     * Test of empty method, of class Iteratee.
     */
    @Test
    public void testEmpty() {
        Iteratee<Integer> im = Iteratee.empty();
        assertThat(im, is(emptyIterable()));
    }

    /**
     * Test of filter method, of class Iteratee.
     */
    @Test
    public void testFilter() {
        Iteratee<Integer> im = Iteratee.of(1, 2, 3, 4, 5);
        assertThat(im.filter(new Predicate1<Integer>() {
            @Override
            public boolean test(Integer obj) {
                return obj % 2 == 0;
            }
        }), is(contains(2, 4)));
    }

    /**
     * Test of filter method, of class Iteratee.
     */
    @Test
    public void testFilter_empty() {
        Iteratee<Integer> im = Iteratee.of();
        assertThat(im.filter(new Predicate1<Integer>() {
            @Override
            public boolean test(Integer obj) {
                return obj % 2 == 0;
            }
        }), is(emptyIterable()));
    }

    /**
     * Test of flatMap method, of class Iteratee.
     */
    @Test
    public void testFlatMap() {
        List<Integer> l1 = Arrays.asList(1, 2, 3);
        List<Integer> l2 = Arrays.asList(4, 5, 6);
        Iteratee<List<Integer>> im = Iteratee.of(l1, l2);
        assertThat(im.flatMap(new Function1<List<Integer>, Iteratee<Integer>>() {
            @Override
            public Iteratee<Integer> apply(List<Integer> obj) {
                return Iteratee.of(obj);
            }
        }), is(contains(1, 2, 3, 4, 5, 6)));
    }

    /**
     * Test of flatMap method, of class Iteratee.
     */
    @Test
    public void testFlatMap_empty() {
        Iteratee<List<Integer>> im = Iteratee.empty();
        assertThat(im.flatMap(new Function1<List<Integer>, Iteratee<Integer>>() {
            @Override
            public Iteratee<Integer> apply(List<Integer> obj) {
                return Iteratee.of(obj);
            }
        }), is(emptyIterable()));
    }

    /**
     * Test of iterator method, of class Iteratee.
     */
    @Test
    public void testIterator() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iteratee<Integer> im = new Iteratee(list);
        assertThat(im, contains(1, 2, 3));
    }

    /**
     * Test of map method, of class Iteratee.
     */
    @Test
    public void testMap() {
        Iteratee<Integer> im = Iteratee.of(1, 2, 3, 4, 5);
        assertThat(im.map(new Function1<Integer, String>() {
            @Override
            public String apply(Integer obj) {
                return obj.toString();
            }
        }), is(contains("1", "2", "3", "4", "5")));
    }

    /**
     * Test of map method, of class Iteratee.
     */
    @Test
    public void testMap_empty() {
        Iteratee<Integer> im = Iteratee.empty();
        assertThat(im.map(new Function1<Integer, String>() {
            @Override
            public String apply(Integer obj) {
                return obj.toString();
            }
        }), is(emptyIterable()));
    }

    /**
     * Test of of method, of class Iteratee.
     */
    @Test
    public void testOf_GenericType() {
        String[] list = {"1", "2", "3"};
        Iteratee<String> im = Iteratee.of(list);
        assertThat(im, contains("1", "2", "3"));
    }

    /**
     * Test of of method, of class Iteratee.
     */
    @Test
    public void testOf_Iterable() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iteratee<Integer> im = Iteratee.of(list);
        assertThat(im, contains(1, 2, 3));
    }

    /**
     * Test of toList method, of class Iteratee.
     */
    @Test
    public void testToList() {
        Iteratee<Integer> im = Iteratee.of(10, 20, 30);
        List<Integer> list = im.toList();
        assertThat(list, is(contains(10, 20, 30)));
    }

    /**
     * Test of toList method, of class Iteratee.
     */
    @Test
    public void testToList_empty() {
        Iteratee<Integer> im = Iteratee.empty();
        List<Integer> list = im.toList();
        assertThat(list, is(emptyIterable()));
    }

    /**
     * Test of toMap method, of class Iteratee.
     */
    @Test
    public void testToMap() {
        Iteratee<Integer> im = Iteratee.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Map<String, Integer> map = im.toMap(new Function1<Integer, String>() {
            @Override
            public String apply(Integer a1) {
                return Integer.toString(a1);
            }
        });
        assertThat(map.size(), is(10));
        for (int i : Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) {
            assertThat(map, hasEntry(String.valueOf(i), i));
        }
    }
}
