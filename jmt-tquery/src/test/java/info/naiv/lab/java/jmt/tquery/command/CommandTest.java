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

import info.naiv.lab.java.jmt.tquery.QueryContext;
import java.util.List;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author enlo
 */
public class CommandTest {

    public CommandTest() {
    }

    static final String TEST_QUERY = "select * from Table where id = ?";
    List<CommandParameter> testParameters;
    Command testTarget;

    @Before
    public void setup() {

        QueryContext ctx = new QueryContext();
        ctx.getParameters().addValue(123);

        testParameters = ctx.getParameters().clone();
        testTarget = new Command(TEST_QUERY, ctx.getParameters());
    }

    /**
     * Test of getParameterValues method, of class Command.
     */
    @Test
    public void testGetParameterValues() {
        assertThat(testTarget.getParameterValues(), contains((Object) 123));
    }

    /**
     * Test of clone method, of class Command.
     */
    @Test
    public void testClone() {
        Command cloned = testTarget.clone();
        assertThat(cloned, is(not(Matchers.sameInstance(testTarget))));
        assertThat(cloned, is(testTarget));
    }

    /**
     * Test of getQuery method, of class Command.
     */
    @Test
    public void testGetQuery() {
        assertThat(testTarget.getQuery(), is(TEST_QUERY));
    }

    /**
     * Test of getParameters method, of class Command.
     */
    @Test
    public void testGetParameters() {
        assertThat(testTarget.getParameters(), is(testParameters));
    }

    /**
     * Test of setQuery method, of class Command.
     */
    @Test
    public void testSetQuery() {
        testTarget.setQuery("aaaa");
        assertThat(testTarget.getQuery(), is("aaaa"));
    }

    /**
     * Test of setParameters method, of class Command.
     */
    @Test
    public void testSetParameters() {
        testTarget.setParameters(new CommandParameters());
        assertThat(testTarget.getParameters(), is(emptyCollectionOf(CommandParameter.class)));
    }

}
