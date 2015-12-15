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

import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 *
 * @author enlo
 */
@RunWith(Theories.class)
public class AutoCreateHolderTest {

    @Ignore
    public static class CtorFixture<T> {

        public T expected;
        public T object;
        public Class<? extends T> clazz;
        public Function1<T, Class<? extends T>> creator;

        public CtorFixture(T object, Class<? extends T> clazz, Function1<T, Class<? extends T>> creator) {
            this.object = object;
            this.clazz = clazz;
            this.creator = creator;
        }

        public CtorFixture(T expected, T object, Class<? extends T> clazz, Function1<T, Class<? extends T>> creator) {
            this.expected = expected;
            this.object = object;
            this.clazz = clazz;
            this.creator = creator;
        }

    }
    
    /**
     *
     */
    @DataPoints("nullCtor")
    public static CtorFixture[] nullCtorParams = new CtorFixture[]{
        new CtorFixture(null, null, null),
        new CtorFixture(null, Integer.class, null),
        new CtorFixture(1, null, StandardFunctions.newInstance(Integer.class, 0)),
        new CtorFixture(1, Integer.class, null),};

    @DataPoints("ctor")
    public static CtorFixture[] ctorParams = new CtorFixture[]{
        new CtorFixture(0, null, Integer.class, StandardFunctions.newInstance(Integer.class, 0)),
        new CtorFixture(1, 1, Integer.class, StandardFunctions.newInstance(Integer.class, 0)),
        new CtorFixture(10, null, Integer.class, StandardFunctions.newInstance(Integer.class, 10)),};

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void testCtorNull01() {
        Misc.nop(new AutoCreateHolder<>(null));
    }

    @Theory
    public void testCtorNull02(@FromDataPoints("nullCtor") CtorFixture f) {
        ex.expect(IllegalArgumentException.class);
        Misc.nop(new AutoCreateHolder<>(f.clazz, f.creator));
    }

    @Theory
    public void testCtorNull03(@FromDataPoints("nullCtor") CtorFixture f) {
        ex.expect(IllegalArgumentException.class);
        Misc.nop(new AutoCreateHolder<>(f.object, f.clazz, f.creator));
    }

    @Test
    public void testCtor01() {
        AutoCreateHolder<String> holder = new AutoCreateHolder<>(String.class);
        assertThat((Object) holder.getContentType(), sameInstance((Object) String.class));
        assertThat(holder.getContent(), is(new String()));
    }

    @Theory
    public void testCtor02(@FromDataPoints("ctor") CtorFixture f) {
        AutoCreateHolder<?> holder = new AutoCreateHolder<>(f.object, f.clazz, f.creator);
        assertThat((Object) holder.getContentType(), sameInstance((Object) f.clazz));
        assertThat(holder.getContent(), is(f.expected));
    }

    /**
     * Test of getContent method, of class AutoCreateHolder.
     */
    @Test
    public void testGetContent() {
        AutoCreateHolder<String> instance = new AutoCreateHolder<>(String.class);
        assertThat(instance.getContent(), is(new String()));
    }

    /**
     * Test of setContent method, of class AutoCreateHolder.
     */
    @Test
    public void testSetContent() {
        AutoCreateHolder<String> instance = new AutoCreateHolder<>(String.class);
        instance.setContent("123");
        assertThat(instance.getContent(), is("123"));
    }

}
