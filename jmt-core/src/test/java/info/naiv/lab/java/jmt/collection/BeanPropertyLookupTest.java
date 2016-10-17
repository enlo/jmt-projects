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
package info.naiv.lab.java.jmt.collection;

import static info.naiv.lab.java.jmt.test.CustomMatchers.entryIs;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import org.springframework.beans.BeansException;

/**
 *
 * @author enlo
 */
public class BeanPropertyLookupTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TestUser {

        String name;

        int age;

        String address;

        boolean enabled;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TestAddress {

        String postal;
        String country;
        String pref;
        String city;
        String street;

    }

    TestUser testUser1;
    BeanPropertyLookup testLookup1;

    public BeanPropertyLookupTest() {
    }

    @Before
    public void init() {
        this.testUser1 = new TestUser("John Doe", 24, "ABC Street, USA.", true);
        this.testLookup1 = new BeanPropertyLookup(this.testUser1);
    }

    /**
     * Test of containsKey method, of class BeanPropertyLookup.
     */
    @Test
    public void testContainsKey() {
    }

    /**
     * Test of get method, of class BeanPropertyLookup.
     */
    @Test
    public void testGet() {

        Object name = this.testLookup1.get("name");
        Object age = this.testLookup1.get("age");
        Object address = this.testLookup1.get("address");
        Object enabled = this.testLookup1.get("enabled");
        Object clazz = this.testLookup1.get("class");

        assertThat(name, is((Object) this.testUser1.getName()));
        assertThat(age, is((Object) this.testUser1.getAge()));
        assertThat(address, is((Object) this.testUser1.getAddress()));
        assertThat(enabled, is((Object) this.testUser1.isEnabled()));
        assertThat(clazz, is((Object) this.testUser1.getClass()));

        this.testUser1.setAge(30);
        age = this.testLookup1.get("age");
        assertThat(age, is((Object) 30));

        this.testUser1.setEnabled(false);
        enabled = this.testLookup1.get("enabled");
        assertThat(enabled, is((Object) false));
    }

    /**
     * Test of get method, of class BeanPropertyLookup.
     */
    @Test(expected = BeansException.class)
    public void testGet_Error() {
        this.testLookup1.get("company");
    }

    /**
     * Test of iterator method, of class BeanPropertyLookup.
     */
    @Test
    public void testIterator() {

        Iterable<Map.Entry<String, Object>> iter = this.testLookup1;

        assertThat(iter, containsInAnyOrder(
                   entryIs("name", (Object) this.testUser1.getName()),
                   entryIs("age", (Object) this.testUser1.getAge()),
                   entryIs("address", (Object) this.testUser1.getAddress()),
                   entryIs("enabled", (Object) this.testUser1.isEnabled()),
                   entryIs("class", (Object) this.testUser1.getClass()))
        );

    }

    /**
     * Test of size method, of class BeanPropertyLookup.
     */
    @Test
    public void testSize() {

        assertThat(this.testLookup1.size(), is(5));

        BeanPropertyLookup bpl = new BeanPropertyLookup(new TestAddress());
        assertThat(bpl.size(), is(6));
    }

}
