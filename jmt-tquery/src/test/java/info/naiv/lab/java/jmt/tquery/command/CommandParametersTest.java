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
package info.naiv.lab.java.jmt.tquery.command;

import java.util.Map;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 *
 * @author enlo
 */
public class CommandParametersTest {

    public CommandParametersTest() {
    }

    /**
     * Test of addCapacity method, of class CommandParameters.
     */
    @Test
    public void testAddCapacity() {
        CommandParameters pl = spy(new CommandParameters());
        pl.addCapacity(10);
        verify(pl).ensureCapacity(10);

        pl.addValue("aaa");
        pl.addCapacity(10);
        verify(pl).ensureCapacity(11);
    }

    /**
     * Test of addNamedValue method, of class CommandParameters.
     */
    @Test
    public void testAddNamedValue() {
        CommandParameters pl = new CommandParameters();
        String key = pl.addNamedValue("id", 12345);
        assertThat(key, is("id"));
        assertThat(pl, hasSize(1));

        CommandParameter p = pl.get(0);
        assertThat(p.getKey(), is("id"));
        assertThat(p.getValue(), is((Object) 12345));

        key = pl.addNamedValue("name", "John");
        assertThat(key, is("name"));
        assertThat(pl, hasSize(2));

        p = pl.get(1);
        assertThat(p.getKey(), is("name"));
        assertThat(p.getValue(), is((Object) "John"));
    }

    /**
     * Test of addValue method, of class CommandParameters.
     */
    @Test
    public void testAddValue() {
        CommandParameters pl = new CommandParameters();
        String key = pl.addValue(12345);
        assertThat(key, is("p0"));
        assertThat(pl, hasSize(1));

        CommandParameter p = pl.get(0);
        assertThat(p.getKey(), is("p0"));
        assertThat(p.getValue(), is((Object) 12345));

        key = pl.addValue("John");
        assertThat(key, is("p1"));
        assertThat(pl, hasSize(2));

        p = pl.get(1);
        assertThat(p.getKey(), is("p1"));
        assertThat(p.getValue(), is((Object) "John"));
    }

    /**
     * Test of addValueWithPrefix method, of class CommandParameters.
     */
    @Test
    public void testAddValueWithPrefix() {
        CommandParameters pl = new CommandParameters();
        String key = pl.addValueWithPrefix(":p", 12345);
        assertThat(key, is(":p0"));
        assertThat(pl, hasSize(1));

        CommandParameter p = pl.get(0);
        assertThat(p.getKey(), is(":p0"));
        assertThat(p.getValue(), is((Object) 12345));

        key = pl.addValueWithPrefix(":a", "John");
        assertThat(key, is(":a1"));
        assertThat(pl, hasSize(2));

        p = pl.get(1);
        assertThat(p.getKey(), is(":a1"));
        assertThat(p.getValue(), is((Object) "John"));
    }

    /**
     * Test of builder method, of class CommandParameters.
     */
    @Test
    public void testBuilder_0args() {
        CommandParametersBuilder builder = CommandParameters.builder();
        assertThat(builder, is(not(nullValue())));
        CommandParameters pl = builder.build();
        assertThat(pl, is(emptyCollectionOf(CommandParameter.class)));
    }

    /**
     * Test of builder method, of class CommandParameters.
     */
    @Test
    public void testBuilder_ObjectArr() {
        CommandParametersBuilder builder = CommandParameters.builder(123, "aaa");
        assertThat(builder, is(not(nullValue())));
        CommandParameters pl = builder.build();
        assertThat(pl, contains(new CommandParameter(0, 123), new CommandParameter(1, "aaa")));
    }

    /**
     * Test of toArray method, of class CommandParameters.
     */
    @Test
    public void testToArray() {
        CommandParametersBuilder builder = CommandParameters.builder(123, "aaa");
        assertThat(builder, is(not(nullValue())));
        CommandParameters pl = builder.build();

        CommandParameter[] arr = pl.toArray();
        assertThat(arr, arrayContaining(new CommandParameter(0, 123), new CommandParameter(1, "aaa")));
    }

    /**
     * Test of clone method, of class CommandParameters.
     */
    @Test
    public void testClone() {
        CommandParametersBuilder builder = CommandParameters.builder(123, "aaa");
        CommandParameters p1 = builder.build();
        CommandParameters p2 = p1.clone();

        assertThat(p2, is(not(sameInstance(p1))));
        assertThat(p2, is(p1));
    }

    /**
     * Test of toMap method, of class CommandParameters.
     */
    @Test
    public void testToMap() {
        CommandParametersBuilder builder = CommandParameters.builder(123, "aaa");
        CommandParameters p1 = builder.build();
        Map<String, Object> map = p1.toMap();

        assertThat(map.size(), is(2));
        assertThat(map, hasEntry("p0", (Object) 123));
        assertThat(map, hasEntry("p1", (Object) "aaa"));
    }

}
