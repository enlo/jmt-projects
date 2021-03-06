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
package info.naiv.lab.java.jmt.bean;

import info.naiv.lab.java.jmt.datetime.DateOnly;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class PDBasedBeanCopierTest {

    public PDBasedBeanCopierTest() {
    }

    /**
     * Test of copyProperties method, of class PDBasedBeanCopier.
     */
    @Test
    public void testCopyProperties() {
        PDBasedBeanCopier beanCopier = PDBasedBeanCopierFactory.createInstance(TestBeanS.class, TestBeanD.class);
        TestBeanD d = new TestBeanD();
        TestBeanS s = new TestBeanS();
        beanCopier.copyProperties(s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
    }

    /**
     * Test of copyProperties method, of class PDBasedBeanCopier.
     */
    @Test
    public void testCopyProperties_2() {
        PDBasedBeanCopier beanCopier = PDBasedBeanCopierFactory.createInstance(TestBeanS2.class, TestBeanD.class);
        TestBeanD d = new TestBeanD();
        TestBeanS2 s = new TestBeanS2();
        beanCopier.copyProperties(s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
    }

    /**
     * Test of copyProperties method, of class PDBasedBeanCopier.
     */
    @Test
    public void testCopyProperties_3() {
        PDBasedBeanCopier beanCopier = PDBasedBeanCopierFactory.createInstance(TestBeanS2.class, TestBeanD2.class);
        TestBeanD2 d = new TestBeanD2();
        TestBeanS2 s = new TestBeanS2();
        beanCopier.copyProperties(s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
        assertThat(d.getAge(), is(s.getAge()));
        assertThat(d.getTel(), is(s.getTel()));
    }

    /**
     * Test of copyProperties method, of class PDBasedBeanCopier.
     */
    @Test
    public void testCopyProperties_4() {
        PDBasedBeanCopier beanCopier = PDBasedBeanCopierFactory.createInstance(TestBeanS.class, TestBeanD2.class);
        TestBeanD2 d = new TestBeanD2();
        TestBeanS2 s = new TestBeanS2();
        beanCopier.copyProperties(s, d);
        assertThat(d.getDate(), is(s.getDate()));
        assertThat(d.getDateOnly(), is(DateOnly.valueOf(s.getDateOnly())));
        assertThat(d.getDest(), is(nullValue()));
        assertThat(d.getId(), is(s.getId()));
        assertThat(d.getName(), is(s.getName()));
        assertThat(d.getAge(), is(0));
        assertThat(d.getTel(), is(nullValue()));
    }

    @Data
    public static class TestBeanD {

        private Date date;
        private DateOnly dateOnly;
        private String dest;
        private int id;
        private String name;
        private boolean valid;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TestBeanD2 extends TestBeanD {

        private int age;
        private String tel;

    }

    @Data
    public static class TestBeanS {

        private Date date = new Date();
        private Date dateOnly = new Date();
        private int id = 1;
        private String name = "CopyPropertiesTest";
        private String src = "source";
        private boolean valid = true;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TestBeanS2 extends TestBeanS {

        private int age = 40;
        private String tel = "1234-567-890";
    }
}
