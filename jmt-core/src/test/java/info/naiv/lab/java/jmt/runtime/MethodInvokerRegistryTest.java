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

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class MethodInvokerRegistryTest {

    final MethodInvoker[] getMessageMethods;

    @SneakyThrows
    public MethodInvokerRegistryTest() {
        getMessageMethods = new MethodInvoker[]{
            new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage")),
            new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage", String.class)),
            new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage", int.class))
        };
    }

    /**
     * Test of containsKey method, of class MethodInvokerRegistry.
     */
    @Test
    public void testContainsKey() {
        MethodInvokerRegistry mir1 = new MethodInvokerRegistry(TestClass.class, false);
        assertThat(mir1.containsKey("getName"), is(false));
        assertThat(mir1.containsKey("getMessage"), is(true));
        assertThat(mir1.containsKey("getmessage"), is(false));

        MethodInvokerRegistry mir2 = new MethodInvokerRegistry(TestClass.class, true);
        assertThat(mir2.containsKey("getName"), is(false));
        assertThat(mir2.containsKey("getMessage"), is(true));
        assertThat(mir2.containsKey("getmessage"), is(true));
    }

    /**
     * Test of get method, of class MethodInvokerRegistry.
     *
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testGet() throws NoSuchMethodException {
        MethodInvokerRegistry mir1 = new MethodInvokerRegistry(TestClass.class, false);
        assertThat(mir1.get("getName"), is(emptyIterable()));
        assertThat(mir1.get("getMessage"), is(containsInAnyOrder(getMessageMethods)));
        assertThat(mir1.get("GetMESSAGE"), is(emptyIterable()));

        MethodInvokerRegistry mir2 = new MethodInvokerRegistry(TestClass.class, true);
        assertThat(mir2.get("getName"), is(emptyIterable()));
        assertThat(mir2.get("getMessage"), is(containsInAnyOrder(getMessageMethods)));
        assertThat(mir2.get("GetMESSAGE"), is(containsInAnyOrder(getMessageMethods)));
    }

    /**
     * Test of getFirst method, of class MethodInvokerRegistry.
     *
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testGetFirst() throws NoSuchMethodException {
        MethodInvokerRegistry mir1 = new MethodInvokerRegistry(TestClass.class, false);
        assertThat(mir1.getFirst("getName"), is(nullValue()));
        assertThat(mir1.getFirst("getMessage"), is(Matchers.anyOf(
                   is((MethodInvoker) new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage"))),
                   is((MethodInvoker) new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage", String.class))),
                   is((MethodInvoker) new OptionalSupportMethodInvoker(TestClass.class.getDeclaredMethod("getMessage", int.class)))
           )));
    }

    /**
     * Test of getTargetClass method, of class MethodInvokerRegistry.
     */
    @Test
    public void testGetTargetClass() {
        MethodInvokerRegistry mir = new MethodInvokerRegistry(TestClass.class, false);
        assertThat(mir.getTargetClass(), is((Class) TestClass.class));
    }

    /**
     * Test of prepare method, of class MethodInvokerRegistry.
     *
     * @throws java.lang.NoSuchMethodException
     */
    @Test
    public void testPrepare() throws NoSuchMethodException {
        MethodInvokerRegistry mir1 = new MethodInvokerRegistry(TestClass.class, false);
        assertThat(mir1.methodInvokers, is((nullValue())));
        mir1.prepare();
        assertThat(mir1.methodInvokers, is(not(nullValue())));
        assertThat(mir1.methodInvokers.containsKey("getMessage"), is(true));
        assertThat(mir1.methodInvokers.containsKey("getmessage"), is(false));

        MethodInvokerRegistry mir2 = new MethodInvokerRegistry(TestClass.class, true);
        assertThat(mir2.methodInvokers, is((nullValue())));
        mir2.prepare();
        assertThat(mir2.methodInvokers, is(not(nullValue())));
        assertThat(mir2.methodInvokers.containsKey("getMessage"), is(true));
        assertThat(mir2.methodInvokers.containsKey("getmessage"), is(true));
    }

    static class TestClass {

        String getMessage() {
            return "hello. ";
        }

        String getMessage(String name) {
            return "hello, " + name;
        }

        String getMessage(int i) {
            return "No." + i;
        }
    }
}
