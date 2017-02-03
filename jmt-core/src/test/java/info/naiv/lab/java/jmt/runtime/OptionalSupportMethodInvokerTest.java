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

import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.monad.Optional;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import lombok.SneakyThrows;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class OptionalSupportMethodInvokerTest {

    public OptionalSupportMethodInvokerTest() {
    }

    /**
     * Test of getParameterTypes method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testGetParameterTypes() {
        Method[] ms = TestClass.class.getDeclaredMethods();
        for (Method m : ms) {
            MethodInvoker mi = new OptionalSupportMethodInvoker(m);
            assertThat(m.getName(), mi.getParameterTypes(), is(mi.getParameterTypes()));
        }
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s1() {
        testInvokeCore("s1", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat(a1.invoke(null), is(nullValue()));
                       assertThat(TestClass.s1c, is(1));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s10() {
        testInvokeCore("s10", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s10(2)", a1.invoke(null, 2), is((Object) 2L));
                       assertThat("s10(1, 2)", a1.invoke(null, 1, 2), is((Object) 3L));
                       assertThat("s10(1, 2, 3)", a1.invoke(null, 1, 2, 3), is((Object) 6L));
                       assertThat("s10(1, 2, 3, 4)", a1.invoke(null, 1, 2, 3, 4), is((Object) 10L));
                       assertThat("s10(0, 1, {2, 3})", a1.invoke(null, 0, 1, new int[]{2, 3}), is((Object) 6L));
                       assertThat("s10(1, opt(1), 1)", a1.invoke(null, 1, Optional.of(1), 1), is((Object) 3L));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s2() {
        testInvokeCore("s2", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s2(2)", a1.invoke(null, 2), is(nullValue()));
                       assertThat("s2(2)", TestClass.s2c, is(2));
                       assertThat("s2(1)", a1.invoke(null, 1), is(nullValue()));
                       assertThat("s2(1)", TestClass.s2c, is(3));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s3() {
        testInvokeCore("s3", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s3(2)", a1.invoke(null, 2), is(nullValue()));
                       assertThat("s3(2)", TestClass.s3c, is(2));
                       assertThat("s3(1, 2)", a1.invoke(null, 1, 2), is(nullValue()));
                       assertThat("s3(1, 2)", TestClass.s3c, is(3));
                       assertThat("s3(1, 2, 3)", a1.invoke(null, 1, 2, 3), is(nullValue()));
                       assertThat("s3(1, 2, 3)", TestClass.s3c, is(6));
                       assertThat("s3(0, {1, 2})", a1.invoke(null, 0, new int[]{1, 2}), is(nullValue()));
                       assertThat("s3(0, {1, 2})", TestClass.s3c, is(3));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s4() {
        testInvokeCore("s4", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s4(2)", a1.invoke(null, 2), is(nullValue()));
                       assertThat("s4(2)", TestClass.s4c, is(2));
                       assertThat("s4(1, 2)", a1.invoke(null, 1, 2), is(nullValue()));
                       assertThat("s4(1, 2)", TestClass.s4c, is(3));
                       assertThat("s4(1, opt(1))", a1.invoke(null, 1, Optional.of(1)), is(nullValue()));
                       assertThat("s4(1, opt(1))", TestClass.s4c, is(2));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s5() {
        testInvokeCore("s5", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s5(2)", a1.invoke(null, 2), is(nullValue()));
                       assertThat("s5(2)", TestClass.s5c, is(2));
                       assertThat("s5(1, 2)", a1.invoke(null, 1, 2), is(nullValue()));
                       assertThat("s5(1, 2)", TestClass.s5c, is(3));
                       assertThat("s5(1, 2, 3)", a1.invoke(null, 1, 2, 3), is(nullValue()));
                       assertThat("s5(1, 2, 3)", TestClass.s5c, is(6));
                       assertThat("s5(1, 2, 3, 4)", a1.invoke(null, 1, 2, 3, 4), is(nullValue()));
                       assertThat("s5(1, 2, 3, 4)", TestClass.s5c, is(10));
                       assertThat("s5(0, 1, {2, 3})", a1.invoke(null, 0, 1, new int[]{2, 3}), is(nullValue()));
                       assertThat("s5(0, 1, {2, 3})", TestClass.s5c, is(6));
                       assertThat("s5(1, opt(1), 1)", a1.invoke(null, 1, Optional.of(1), 1), is(nullValue()));
                       assertThat("s5(1, opt(1), 1)", TestClass.s5c, is(3));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s6() {
        testInvokeCore("s6", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat(a1.invoke(null), is((Object) "1"));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s7() {
        testInvokeCore("s7", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s7(2)", a1.invoke(null, 2), is((Object) "2"));
                       assertThat("s7(1)", a1.invoke(null, 1), is((Object) "1"));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s8() {
        testInvokeCore("s8", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s8(2)", a1.invoke(null, 2), is((Object) 2));
                       assertThat("s8(1, 2)", a1.invoke(null, 1, 2), is((Object) 3));
                       assertThat("s8(1, 2, 3)", a1.invoke(null, 1, 2, 3), is((Object) 6));
                       assertThat("s8(0, {1, 2})", a1.invoke(null, 0, new int[]{1, 2}), is((Object) 3));
                   }
               });
    }

    /**
     * Test of invoke method, of class OptionalSupportMethodInvoker.
     */
    @Test
    public void testInvoke_s9() {
        testInvokeCore("s9", new Consumer1<OptionalSupportMethodInvoker>() {
                   @SneakyThrows
                   @Override
                   public void accept(OptionalSupportMethodInvoker a1) {
                       assertThat("s9(2)", a1.invoke(null, 2), is((Object) BigDecimal.valueOf(2)));
                       assertThat("s9(1, 2)", a1.invoke(null, 1, 2), is((Object) BigDecimal.valueOf(3)));
                       assertThat("s9(1, opt(1))", a1.invoke(null, 1, Optional.of(1)), is((Object) BigDecimal.valueOf(2)));
                   }
               });
    }

    private void testInvokeCore(String name, Consumer1<OptionalSupportMethodInvoker> c) {
        Method[] ma = TestClass.class.getDeclaredMethods();
        TestClass.reset();
        for (Method m : ma) {
            if (name.equals(m.getName())) {
                OptionalSupportMethodInvoker mi = new OptionalSupportMethodInvoker(m);
                c.accept(mi);
                return;
            }
        }
    }

    static class TestClass {

        static int s1c = 0;
        static int s2c = 0;
        static int s3c = 0;
        static int s4c = 0;
        static int s5c = 0;

        public static void reset() {
            s1c = 0;
            s2c = 0;
            s3c = 0;
            s4c = 0;
            s5c = 0;
        }

        public static void s1() {
            ++s1c;
        }

        public static void s2(int x) {
            s2c += x;
        }

        public static void s3(int x, int... more) {
            s3c = x;
            for (int a : more) {
                s3c += a;
            }
        }

        public static void s4(int x, Optional<Integer> y) {
            s4c = x;
            for (int a : y) {
                s4c += a;
            }
        }

        public static void s5(int x, Optional<Integer> y, int... more) {
            s5c = x;
            for (int a : y) {
                s5c += a;
            }
            for (int a : more) {
                s5c += a;
            }
        }

        public static String s6() {
            return "1";
        }

        public static String s7(int x) {
            return "" + x;
        }

        public static int s8(int x, int... more) {
            for (int a : more) {
                x += a;
            }
            return x;
        }

        public static BigDecimal s9(int x, Optional<Integer> y) {
            return BigDecimal.valueOf(x + y.orElse(0));
        }

        public static long s10(int x, Optional<Integer> y, int... more) {
            for (int a : y) {
                x += a;
            }
            for (int a : more) {
                x += a;
            }
            return x;
        }
    }
}
