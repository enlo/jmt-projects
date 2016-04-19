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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.collection.MapBuilder;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class MapBuilderTest {

    /**
     *
     */
    public MapBuilderTest() {
    }

    /**
     * Test of concurrentHashMap method, of class MapBuilder.
     */
    @Test
    public void testConcurrentHashMap() {
        MapBuilder<Integer, String> mb = MapBuilder.concurrentHashMap(1, "text");
        assertThat(mb, is(notNullValue()));
        Map<Integer, String> map = mb.toMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(instanceOf(ConcurrentHashMap.class)));
        assertThat(map.size(), is(1));
        assertThat(map, is(hasEntry(1, "text")));
    }

    /**
     * Test of hashMap method, of class MapBuilder.
     */
    @Test
    public void testHashMap() {
        MapBuilder<String, Integer> mb = MapBuilder.hashMap("key", 1);
        assertThat(mb, is(notNullValue()));
        Map<String, Integer> map = mb.toMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(instanceOf(HashMap.class)));
        assertThat(map.size(), is(1));
        assertThat(map, is(hasEntry("key", 1)));
    }

    /**
     * Test of put method, of class MapBuilder.
     */
    @Test
    public void testPut() {
        MapBuilder<String, Integer> mb = MapBuilder.hashMap("key1", 1);
        mb.put("key2", 2);
        Map<String, Integer> map = mb.toMap();
        assertThat(map.size(), is(2));
        assertThat(map, is(hasEntry("key1", 1)));
        assertThat(map, is(hasEntry("key2", 2)));
    }

    /**
     * Test of toMap method, of class MapBuilder.
     */
    @Test
    public void testToMap() {
        MapBuilder<String, Integer> mb = MapBuilder.hashMap("key1", 1);
        mb.put("key2", 2);
        Map<String, Integer> map = mb.toMap();
        mb.put("key3", 3);
        assertThat(map.size(), is(2));
        assertThat(map, is(hasEntry("key1", 1)));
        assertThat(map, is(hasEntry("key2", 2)));
    }

    /**
     * Test of treeMap method, of class MapBuilder.
     */
    @Test
    public void testTreeMap_3args() {

        MapBuilder<String, String> mb = MapBuilder.treeMap(String.CASE_INSENSITIVE_ORDER, "In", "Out");
        assertThat(mb, is(notNullValue()));
        Map<String, String> map = mb.toMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(instanceOf(TreeMap.class)));
        assertThat(map.size(), is(1));
        assertThat(map, is(hasEntry("In", "Out")));

        TreeMap tm = (TreeMap) map;
        Comparator comp = String.CASE_INSENSITIVE_ORDER;
        assertThat(tm.comparator(), is(sameInstance(comp)));
    }

    /**
     * Test of treeMap method, of class MapBuilder.
     */
    @Test
    public void testTreeMap_GenericType_GenericType() {
        MapBuilder<Date, Double> mb = MapBuilder.treeMap(new Date(0), 1.0);
        assertThat(mb, is(notNullValue()));
        Map<Date, Double> map = mb.toMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(instanceOf(TreeMap.class)));
        assertThat(map.size(), is(1));
        assertThat(map, is(hasEntry(new Date(0), 1.0)));
    }

}
