/*
 * Copyright (c) 2015, enlo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.naiv.lab.java.jmt.infrastructure;

import info.naiv.lab.java.jmt.infrastructure.annotation.ServicePriority;
import java.util.Objects;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.Order;

/**
 *
 * @author enlo
 */
public abstract class AbstractServiceContainerTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of id method, of class AbstractServiceContainer.
     */
    @Test
    public void testId() {
        ServiceContainer first = createContainer();
        ServiceContainer second = createContainer();
        assertThat(first.id(), is(notNullValue()));
        assertThat(second.id(), is(notNullValue()));
        assertThat(first.id(), is(not(second.id())));
    }

    /**
     * Test of registerService method, of class AbstractServiceContainer.
     */
    @Test
    public void testRegisterService_3args() {

        TestServiceImpl1 service = new TestServiceImpl1(1);
        AbstractServiceContainer container = createContainer();

        int size = container.connections.size();

        ServiceConnection first = container.registerService(2, service, Tag.of(1));
        assertThat(first, is(notNullValue()));
        assertThat(first.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(first.getTag(), is(new Tag(1)));
        assertThat("p1", first.getPriority(), is(2));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) first)));
        assertThat(container.connections.size(), is(size + 1));

        ServiceConnection second = container.registerService(2, service);
        assertThat(second, is(not(sameInstance(first))));
        assertThat(second.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(second.getTag(), is(Tag.NONE));
        assertThat("p2", second.getPriority(), is(2));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) second)));
        assertThat(container.connections.size(), is(size + 2));

        ServiceConnection third = container.registerService(4, service, Tag.of(2));
        assertThat(third, is(not(sameInstance(first))));
        assertThat(third.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(third.getTag(), is(new Tag(2)));
        assertThat("p3", third.getPriority(), is(4));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) third)));
        assertThat(container.connections.size(), is(size + 3));

        ServiceConnection forth = container.registerService(service, Tag.of(1));
        assertThat(forth, is(sameInstance(first)));
        assertThat(forth.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(forth.getTag(), is(new Tag(1)));
        assertThat("p4", forth.getPriority(), is(1));
        assertThat(container.connections.size(), is(size + 3));

        first.close();
        assertThat(container.connections, is(not(hasItem((AbstractServiceConnection) first))));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) second)));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) third)));
        assertThat(container.connections.size(), is(size + 2));
    }

    /**
     * Test of registerService method, of class AbstractServiceContainer.
     */
    @Test
    public void testRegisterService_Object() {
        TestServiceImpl1 service = new TestServiceImpl1(1);
        AbstractServiceContainer container = createContainer();

        int size = container.connections.size();

        ServiceConnection first = container.registerService(service);
        assertThat(first, is(notNullValue()));
        assertThat(first.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(first.getTag(), is(Tag.NONE));
        assertThat(first.getPriority(), is(1));

        assertThat(container.connections, is(hasItem((AbstractServiceConnection) first)));
        assertThat(container.connections.size(), is(size + 1));

        ServiceConnection second = container.registerService(service);
        assertThat(second, is(sameInstance(first)));
        assertThat(container.connections.size(), is(size + 1));

        first.close();
        assertThat(container.connections, is(not(hasItem((AbstractServiceConnection) first))));
        assertThat(container.connections.size(), is(size));
    }

    /**
     * Test of registerService method, of class AbstractServiceContainer.
     */
    @Test
    public void testRegisterService_Object_Tag() {

        TestServiceImpl1 service = new TestServiceImpl1(1);
        AbstractServiceContainer container = createContainer();

        int size = container.connections.size();

        ServiceConnection first = container.registerService(service, Tag.of(1));
        assertThat(first, is(notNullValue()));
        assertThat(first.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(first.getTag(), is(new Tag(1)));
        assertThat(first.getPriority(), is(1));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) first)));
        assertThat(container.connections.size(), is(size + 1));

        ServiceConnection second = container.registerService(service);
        assertThat(second, is(not(sameInstance(first))));
        assertThat(second.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(second.getTag(), is(Tag.NONE));
        assertThat(second.getPriority(), is(1));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) second)));
        assertThat(container.connections.size(), is(size + 2));

        ServiceConnection third = container.registerService(service, Tag.of(2));
        assertThat(third, is(not(sameInstance(first))));
        assertThat(third.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(third.getTag(), is(new Tag(2)));
        assertThat(third.getPriority(), is(1));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) third)));
        assertThat(container.connections.size(), is(size + 3));

        ServiceConnection forth = container.registerService(service, Tag.of(1));
        assertThat(forth, is(sameInstance(first)));
        assertThat(forth.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(forth.getTag(), is(new Tag(1)));
        assertThat(forth.getPriority(), is(1));
        assertThat(container.connections.size(), is(size + 3));

        first.close();
        assertThat(container.connections, is(not(hasItem((AbstractServiceConnection) first))));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) second)));
        assertThat(container.connections, is(hasItem((AbstractServiceConnection) third)));
        assertThat(container.connections.size(), is(size + 2));
    }

    /**
     * Test of registerService method, of class AbstractServiceContainer.
     */
    @Test
    public void testRegisterService_int_Object() {
        TestServiceImpl1 service = new TestServiceImpl1(1);
        AbstractServiceContainer container = createContainer();

        int size = container.connections.size();

        ServiceConnection first = container.registerService(2, service);
        assertThat(first, is(notNullValue()));
        assertThat(first.getContainer(), is(sameInstance((ServiceContainer) container)));
        assertThat(first.getTag(), is(Tag.NONE));
        assertThat(first.getPriority(), is(2));

        assertThat(container.connections, is(hasItem((AbstractServiceConnection) first)));
        assertThat(container.connections.size(), is(size + 1));

        ServiceConnection second = container.registerService(3, service);
        assertThat(second, is(sameInstance(first)));
        assertThat(second.getPriority(), is(3));
        assertThat(container.connections.size(), is(size + 1));

        first.close();
        assertThat(container.connections, is(not(hasItem((AbstractServiceConnection) first))));
        assertThat(container.connections.size(), is(size));
    }

    /**
     * Test of resolveService method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveService_Class() {
        TestServiceImpl1 s1 = new TestServiceImpl1(1);
        ServiceContainer container = createContainer();

        assertThat(container.resolveService(TestService.class), is(nullValue()));
        assertThat(container.resolveService(TestService1.class), is(nullValue()));
        assertThat(container.resolveService(TestService2.class), is(nullValue()));

        container.registerService(s1);
        assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s1)));
        assertThat(container.resolveService(TestService1.class), is(sameInstance((TestService1) s1)));
        assertThat(container.resolveService(TestService2.class), is(nullValue()));

    }

    /**
     * Test of resolveService method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveService_Class_priority() {
        TestServiceImpl1 s1 = new TestServiceImpl1(1);
        TestServiceImpl1 s2 = new TestServiceImpl1(2);
        TestServiceImpl1 s3 = new TestServiceImpl1(3);

        {
            ServiceContainer container = createContainer();
            container.registerService(1, s1);
            container.registerService(2, s2);
            container.registerService(3, s3);
            assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s1)));
        }
        {
            ServiceContainer container = createContainer();
            container.registerService(2, s1);
            container.registerService(1, s2);
            container.registerService(3, s3);
            assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s2)));
        }
        {
            ServiceContainer container = createContainer();
            container.registerService(3, s1);
            container.registerService(2, s2);
            container.registerService(1, s3);
            assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s3)));
        }

    }

    /**
     * Test of resolveService method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveService_Class_priority2() {
        TestService s1 = new TestAnnotatedServiceImpl1();
        TestService s2 = new TestAnnotatedServiceImpl2();

        ServiceContainer container = createContainer();
        container.registerService(s1);
        container.registerService(s2);
        assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s2)));

    }

    /**
     * Test of resolveService method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveService_Class_priority3() {
        TestService s1 = new TestAnnotatedServiceImpl3();
        TestService s2 = new TestAnnotatedServiceImpl4();

        ServiceContainer container = createContainer();
        container.registerService(s1);
        container.registerService(s2);
        assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s2)));

    }

    /**
     * Test of resolveService method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveService_Class_Tag() {
        TestServiceImpl1 s1 = new TestServiceImpl1(1);
        ServiceContainer container = createContainer();

        assertThat(container.resolveService(TestService.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService1.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService2.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService1.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService2.class, Tag.of(2)), is(nullValue()));

        container.registerService(s1);
        assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s1)));
        assertThat(container.resolveService(TestService1.class), is(sameInstance((TestService1) s1)));
        assertThat(container.resolveService(TestService2.class), is(nullValue()));
        assertThat(container.resolveService(TestService.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService1.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService2.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService1.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService2.class, Tag.of(2)), is(nullValue()));

        container.registerService(s1, Tag.of(1));
        assertThat(container.resolveService(TestService.class), is(sameInstance((TestService) s1)));
        assertThat(container.resolveService(TestService1.class), is(sameInstance((TestService1) s1)));
        assertThat(container.resolveService(TestService2.class), is(nullValue()));
        assertThat(container.resolveService(TestService.class, Tag.of(1)), is(sameInstance((TestService) s1)));
        assertThat(container.resolveService(TestService1.class, Tag.of(1)), is(sameInstance((TestService1) s1)));
        assertThat(container.resolveService(TestService2.class, Tag.of(1)), is(nullValue()));
        assertThat(container.resolveService(TestService.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService1.class, Tag.of(2)), is(nullValue()));
        assertThat(container.resolveService(TestService2.class, Tag.of(2)), is(nullValue()));

    }

    /**
     * Test of resolveServices method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveServices_Class() {
        TestServiceImpl1 s1 = new TestServiceImpl1(1);
        TestServiceImpl1 s2 = new TestServiceImpl1(2);
        TestServiceImpl2 s3 = new TestServiceImpl2(1);
        ServiceContainer container = createContainer();

        assertThat(container.resolveServices(TestService.class), is(empty()));
        assertThat(container.resolveServices(TestService1.class), is(empty()));
        assertThat(container.resolveServices(TestService2.class), is(empty()));

        container.registerService(s1);
        container.registerService(s2);
        container.registerService(s3);
        assertThat(container.resolveServices(TestService.class), is(containsInAnyOrder((TestService) s1, s2, s3)));
        assertThat(container.resolveServices(TestService1.class), is(containsInAnyOrder((TestService1) s1, s2)));
        assertThat(container.resolveServices(TestService2.class), is(contains((TestService2) s3)));
    }

    /**
     * Test of resolveServices method, of class AbstractServiceContainer.
     */
    @Test
    public void testResolveServices_Class_Tag() {
        TestServiceImpl1 s1 = new TestServiceImpl1(1);
        TestServiceImpl1 s2 = new TestServiceImpl1(2);
        TestServiceImpl2 s3 = new TestServiceImpl2(1);
        ServiceContainer container = createContainer();

        assertThat(container.resolveServices(TestService.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService1.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService2.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService1.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService2.class, Tag.of(2)), is(empty()));

        container.registerService(s1);
        container.registerService(s2);
        container.registerService(s3);
        assertThat(container.resolveServices(TestService.class), is(containsInAnyOrder((TestService) s1, s2, s3)));
        assertThat(container.resolveServices(TestService1.class), is(containsInAnyOrder((TestService1) s1, s2)));
        assertThat(container.resolveServices(TestService2.class), is(contains((TestService2) s3)));
        assertThat(container.resolveServices(TestService.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService1.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService2.class, Tag.of(1)), is(empty()));
        assertThat(container.resolveServices(TestService.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService1.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService2.class, Tag.of(2)), is(empty()));

        container.registerService(s1, Tag.of(1));
        container.registerService(s2, Tag.of(1));
        container.registerService(s3, Tag.of(1));
        assertThat(container.resolveServices(TestService.class), is(containsInAnyOrder((TestService) s1, s2, s3)));
        assertThat(container.resolveServices(TestService1.class), is(containsInAnyOrder((TestService1) s1, s2)));
        assertThat(container.resolveServices(TestService2.class), is(contains((TestService2) s3)));
        assertThat(container.resolveServices(TestService.class, Tag.of(1)), is(containsInAnyOrder((TestService) s1, s2, s3)));
        assertThat(container.resolveServices(TestService1.class, Tag.of(1)), is(containsInAnyOrder((TestService1) s1, s2)));
        assertThat(container.resolveServices(TestService2.class, Tag.of(1)), is(containsInAnyOrder((TestService2) s3)));
        assertThat(container.resolveServices(TestService.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService1.class, Tag.of(2)), is(empty()));
        assertThat(container.resolveServices(TestService2.class, Tag.of(2)), is(empty()));

    }

    protected abstract AbstractServiceContainer createContainer();

    public static interface TestService {
    }

    public static interface TestService1 extends TestService {
    }

    public static interface TestService2 extends TestService {
    }

    public static class AbstractTestService {

        private final long id;

        public AbstractTestService(int x, int y) {
            id = x * 10 + y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbstractTestService other = (AbstractTestService) obj;
            return Objects.equals(this.id, other.id);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + Objects.hashCode(this.id);
            return hash;
        }

        @Override
        public String toString() {
            return Long.toString(id);
        }
    }

    public static class TestServiceImpl1 extends AbstractTestService implements TestService1 {

        public TestServiceImpl1(int i) {
            super(1, i);
        }
    }

    public static class TestServiceImpl2 extends AbstractTestService implements TestService2 {

        public TestServiceImpl2(int i) {
            super(2, i);
        }
    }

    @ServicePriority(2)
    public static class TestAnnotatedServiceImpl1 extends AbstractTestService implements TestService {

        public TestAnnotatedServiceImpl1() {
            super(1, 1);
        }
    }

    @ServicePriority(1)
    public static class TestAnnotatedServiceImpl2 extends AbstractTestService implements TestService {

        public TestAnnotatedServiceImpl2() {
            super(2, 2);
        }
    }

    @Order(2)
    public static class TestAnnotatedServiceImpl3 extends AbstractTestService implements TestService {

        public TestAnnotatedServiceImpl3() {
            super(1, 1);
        }
    }

    @Order(1)
    public static class TestAnnotatedServiceImpl4 extends AbstractTestService implements TestService {

        public TestAnnotatedServiceImpl4() {
            super(2, 2);
        }
    }
}
