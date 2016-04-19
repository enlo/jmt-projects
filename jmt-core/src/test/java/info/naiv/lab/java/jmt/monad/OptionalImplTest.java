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
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.fx.Supplier;
import java.io.IOException;
import java.util.Set;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class OptionalImplTest {

    /**
     *
     */
    public OptionalImplTest() {
    }

    /**
     * Test of bind method, of class OptionalImpl.
     */
    @Test
    public void testBind() {
        final StringBuilder sb = new StringBuilder();
        Optional<Integer> opt = OptionalImpl.of(123);
        assertThat(opt.bind(new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                sb.append(a1);
            }
        }), is(sameInstance(opt)));
        assertThat(sb.toString(), is("123"));
    }

    /**
     * Test of empty method, of class OptionalImpl.
     */
    @Test
    public void testEmpty() {
        assertThat(OptionalImpl.empty(), is(sameInstance((Object) OptionalImpl.EMPTY)));
        assertThat(OptionalImpl.empty().isPresent(), is(false));
    }

    /**
     * Test of equals method, of class OptionalImpl.
     */
    @Test
    public void testEquals() {
        Optional<Integer> opt = new OptionalImpl(123);
        assertThat(OptionalImpl.of(123), is(opt));
        assertThat(OptionalImpl.of(456), is(not(opt)));
        assertThat(OptionalImpl.<Integer>empty(), is(not(opt)));
        assertThat(OptionalImpl.<Integer>empty(), is(OptionalImpl.<Integer>ofNullable(null)));
        OptionalImpl<String> opt2 = new OptionalImpl("abc");
        assertThat(opt.equals(opt2), is(false));
    }

    /**
     * Test of filter method, of class OptionalImpl.
     */
    @Test
    public void testFilter() {
        Optional<Integer> opt = OptionalImpl.of(123);
        assertThat(opt.filter(StandardFunctions.equal(123)), is(opt));
        assertThat(opt.filter(StandardFunctions.equal(456)), is(OptionalImpl.EMPTY));
    }

    /**
     * Test of flatMap method, of class OptionalImpl.
     */
    @Test
    public void testFlatMap() {
        Optional<Integer> opt = OptionalImpl.of(123);
        Function1<Integer, Optional<Integer>> fx = new Function1<Integer, Optional<Integer>>() {
            @Override
            public Optional<Integer> apply(Integer a1) {
                return OptionalImpl.of(a1);
            }
        };
        assertThat(opt.flatMap(fx), is(opt));
        assertThat(OptionalImpl.<Integer>empty().flatMap(fx), is(OptionalImpl.EMPTY));
    }

    /**
     * Test of get method, of class OptionalImpl.
     */
    @Test
    public void testGet() {
        assertThat(OptionalImpl.of("123").get(), is("123"));
        assertThat(OptionalImpl.of(456).get(), is(456));
    }

    /**
     * Test of getValue method, of class OptionalImpl.
     */
    @Test
    public void testGetValue() {
        assertThat(OptionalImpl.of(123).get(), is(123));
        assertThat(OptionalImpl.empty().getValue(), is(nullValue()));
    }

    /**
     * Test of hashCode method, of class OptionalImpl.
     */
    @Test
    public void testHashCode() {
        OptionalImpl<Integer> opt = new OptionalImpl(123);
        assertThat(OptionalImpl.of(123).hashCode(), is(opt.hashCode()));
        assertThat(OptionalImpl.empty().hashCode(), is(OptionalImpl.ofNullable(null).hashCode()));
    }

    /**
     * Test of ifPresent method, of class OptionalImpl.
     */
    @Test
    public void testIfPresent() {
        final StringBuilder sb = new StringBuilder();
        Optional<Integer> opt = OptionalImpl.of(123);
        Consumer1<Integer> fx = new Consumer1<Integer>() {
            @Override
            public void accept(Integer a1) {
                sb.append(a1);
            }
        };
        opt.ifPresent(fx);
        assertThat(sb.toString(), is("123"));
    }

    /**
     * Test of isPresent method, of class OptionalImpl.
     */
    @Test
    public void testIsPresent() {
        Optional<Integer> opt = OptionalImpl.of(123);
        assertThat(opt.isPresent(), is(true));
    }

    /**
     * Test of iterator method, of class OptionalImpl.
     */
    @Test
    public void testIterator() {
        assertThat(OptionalImpl.of(123), is(contains(123)));
        assertThat(OptionalImpl.empty(), is(emptyIterable()));
    }

    /**
     * Test of map method, of class OptionalImpl.
     */
    @Test
    public void testMap() {
        Optional<Integer> opt = OptionalImpl.of(123);
        Function1<Integer, String> fx = new Function1<Integer, String>() {
            @Override
            public String apply(Integer a1) {
                return a1.toString();
            }
        };
        assertThat(opt.map(fx), is(OptionalImpl.of("123")));
        assertThat(OptionalImpl.<Integer>empty().map(fx), is(OptionalImpl.EMPTY));
    }

    /**
     * Test of of method, of class OptionalImpl.
     */
    @Test
    public void testOf() {
        assertThat(OptionalImpl.of("123"), is((Optional) new OptionalImpl<>("123")));
    }

    /**
     * Test of ofNullable method, of class OptionalImpl.
     */
    @Test
    public void testOfNullable() {
        assertThat(OptionalImpl.ofNullable("123"), is((Optional) new OptionalImpl<>("123")));
        assertThat(OptionalImpl.ofNullable(null), is(OptionalImpl.EMPTY));
    }

    /**
     * Test of of method, of class OptionalImpl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOf_empty() {
        OptionalImpl.of(null);
    }

    /**
     * Test of orElse method, of class OptionalImpl.
     */
    @Test
    public void testOrElse() {
        assertThat(OptionalImpl.of("123").orElse("456"), is("123"));
        assertThat(OptionalImpl.<String>empty().orElse("456"), is("456"));
        assertThat(OptionalImpl.<String>empty().orElse(null), is(nullValue()));
    }

    /**
     * Test of orElseGet method, of class OptionalImpl.
     */
    @Test
    public void testOrElseGet() {
        Supplier<String> fx = new Supplier<String>() {
            @Override
            public String get() {
                return "456";
            }
        };
        assertThat(OptionalImpl.of("123").orElseGet(fx), is("123"));
        assertThat(OptionalImpl.<String>empty().orElseGet(fx), is("456"));
    }

    /**
     * Test of orElseThrow method, of class OptionalImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testOrElseThrow() throws IOException {
        Supplier<IOException> fx = new Supplier<IOException>() {
            @Override
            public IOException get() {
                return new IOException();
            }
        };
        assertThat(OptionalImpl.of("123").orElseThrow(fx), is("123"));
    }

    /**
     * Test of orElseThrow method, of class OptionalImpl.
     *
     * @throws java.io.IOException
     */
    @Test(expected = IOException.class)
    public void testOrElseThrow_exception() throws IOException {
        Supplier<IOException> fx = new Supplier<IOException>() {
            @Override
            public IOException get() {
                return new IOException();
            }
        };
        OptionalImpl.empty().orElseThrow(fx);
    }

    /**
     * Test of toSet method, of class OptionalImpl.
     */
    @Test
    public void testToSet() {
        Set<Integer> set = OptionalImpl.of(123).toSet();
        assertThat(set, containsInAnyOrder(123));
    }

    /**
     * Test of toString method, of class OptionalImpl.
     */
    @Test
    public void testToString() {
        OptionalImpl<Integer> opt = new OptionalImpl(123);
        assertThat(opt.toString(), is(not(nullValue())));
    }

}
