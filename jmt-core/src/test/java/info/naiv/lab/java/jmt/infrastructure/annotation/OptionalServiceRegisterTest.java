/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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
package info.naiv.lab.java.jmt.infrastructure.annotation;

import info.naiv.lab.java.jmt.infrastructure.ServiceConnection;
import info.naiv.lab.java.jmt.infrastructure.ServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.infrastructure.SimpleServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.annotation.test.OptionalTestClass1;
import info.naiv.lab.java.jmt.infrastructure.annotation.test.OptionalTestClass2;
import info.naiv.lab.java.jmt.infrastructure.annotation.test.OptionalTestClass3;
import info.naiv.lab.java.jmt.infrastructure.annotation.test.OptionalTestClass4;
import info.naiv.lab.java.jmt.infrastructure.annotation.test.OptionalTestClass6;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 *
 * @author enlo
 */
public class OptionalServiceRegisterTest {

    public OptionalServiceRegisterTest() {
    }

    /**
     * Test of register method, of class OptionalServiceRegister.
     */
    @Test
    public void testRegister_ServiceContainer_String() {
        SimpleServiceContainer container = new SimpleServiceContainer();
        OptionalServiceRegister instance = newInstance();
        List<ServiceConnection> conn = instance.register(container, "info.naiv.lab.java.jmt.infrastructure.annotation.test");
        assertThat(conn, is(hasSize(3)));
        assertThat(container.resolveService(OptionalTestClass1.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass2.class), is(notNullValue()));
        assertThat(container.resolveService(OptionalTestClass3.class), is(notNullValue()));
        assertThat(container.resolveService(OptionalTestClass4.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass6.class), is(notNullValue()));
    }

    /**
     * Test of register method, of class OptionalServiceRegister.
     */
    @Test
    public void testRegister_String() {
        ServiceContainer container = ServiceProviders.getSystemContainer();
        assertThat(container.resolveService(OptionalTestClass1.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass2.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass3.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass4.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass6.class), is(nullValue()));

        OptionalServiceRegister instance = newInstance();
        List<ServiceConnection> conn = instance.register("info.naiv.lab.java.jmt.infrastructure.annotation.test");
        assertThat(conn, is(hasSize(3)));
        assertThat(container.resolveService(OptionalTestClass1.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass2.class), is(notNullValue()));
        assertThat(container.resolveService(OptionalTestClass3.class), is(notNullValue()));
        assertThat(container.resolveService(OptionalTestClass4.class), is(nullValue()));
        assertThat(container.resolveService(OptionalTestClass6.class), is(notNullValue()));
    }

    protected OptionalServiceRegister newInstance() {
        OptionalServiceRegister instance = new OptionalServiceRegister();
        ResourcePatternResolver res = new PathMatchingResourcePatternResolver();
        instance.getResolver().setResourcePatternResolver(res);
        return instance;
    }

}
