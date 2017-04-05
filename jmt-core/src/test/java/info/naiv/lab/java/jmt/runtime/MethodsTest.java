/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.runtime;

import java.lang.reflect.Method;
import java.util.Arrays;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class MethodsTest {

    public MethodsTest() {
    }

    static double div(int x, int y) {
        return x * 1.0 / y;
    }

    /**
     * Test of bind1st method, of class Methods.
     *
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testBind1st_Method_Object() throws ReflectiveOperationException {
        Method madd = MethodsTest.class.getDeclaredMethod("div", int.class, int.class);
        MethodInvoker mi = Methods.bind1st(madd, 8);
        assertThat((double) mi.invoke(null, 1), is(8.0));
        assertThat((double) mi.invoke(null, 4), is(2.0));
    }

    /**
     * Test of bind1st method, of class Methods.
     *
     * @throws java.lang.ReflectiveOperationException
     */
    @Test
    public void testBind1st_MethodInvoker_Object() throws ReflectiveOperationException {
        MethodInvoker madd = new SimpleMethodInvoker(MethodsTest.class.getDeclaredMethod("div", int.class, int.class));
        MethodInvoker mi = Methods.bind1st(madd, 8);
        assertThat((double) mi.invoke(null, 1), is(8.0));
        assertThat((double) mi.invoke(null, 4), is(2.0));
    }

    /**
     * Test of duck method, of class Methods.
     */
    @Test
    public void testDuck_Object_GenericType() {
        Duck duck1 = Methods.<Duck>duck(new SilentDuck());
        Duck duck2 = Methods.<Duck>duck(new LoudDuck());
        Duck duck3 = Methods.<Duck>duck(new DuckImpl());
        Duck duck4 = Methods.<Duck>duck(new TalentedDog());

        assertThat(duck1.quack(), is("quack"));
        assertThat(duck2.quack(), is("QUACK"));
        assertThat(duck3.quack(), is("Quack"));
        assertThat(duck3, is(instanceOf(Duck.class)));
        assertThat(duck4.quack(), is("quacklikesound []"));
    }

    /**
     * Test of duck method, of class Methods.
     */
    @Test(expected = IllegalStateException.class)
    public void testDuck_Object_GenericType2() {
        Duck duck1 = Methods.<Duck>duck(new AverageDog());
        assertThat(duck1, is(not(nullValue())));
        duck1.quack();
    }

    /**
     * Test of duck method, of class Methods.
     */
    @Test
    public void testDuck_Object_Class() {
        Duck duck1 = Methods.duck(Duck.class, new SilentDuck());
        Duck duck2 = Methods.duck(Duck.class, new LoudDuck());
        Duck duck3 = Methods.duck(Duck.class, new DuckImpl());
        Duck duck4 = Methods.duck(Duck.class, new TalentedDog());

        assertThat(duck1.quack(), is("quack"));
        assertThat(duck2.quack(), is("QUACK"));
        assertThat(duck3.quack(), is("Quack"));
        assertThat(duck3, is(instanceOf(Duck.class)));
        assertThat(duck4.quack(), is("quacklikesound []"));
    }

    /**
     * Test of checkInvoke method, of class Methods.
     */
    @Test
    public void testCheckInvoke() {
    }

    public class SilentDuck {

        public String quack() {
            return "quack";
        }
    }

    public class LoudDuck {

        public String quack() {
            return "QUACK";
        }
    }

    public class AverageDog {

        public String bark() {
            return "bark";
        }
    }

    public class TalentedDog {

        public String bark() {
            return "superior bark";
        }

        public String quack(String... cond) {
            return "quacklikesound " + Arrays.toString(cond);
        }
    }

    public class DuckImpl implements Duck {

        @Override
        public String quack() {
            return "Quack";
        }
    }

    public interface Duck {

        String quack();
    }

}
