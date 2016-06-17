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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class CommandParameterTest {

    public CommandParameterTest() {
    }

    /**
     * Test of clone method, of class CommandParameter.
     */
    @Test
    public void testClone() {
        CommandParameter p1 = new CommandParameter("A", 123);
        CommandParameter p2 = p1.clone();

        assertThat(p2, is(not(sameInstance(p1))));
        assertThat(p2, is(p1));
    }

    /**
     * Test of getKey method, of class CommandParameter.
     */
    @Test
    public void testGetKey() {
        CommandParameter p1 = new CommandParameter("A", 123);
        CommandParameter p2 = new CommandParameter("B", 456);
        assertThat(p1.getKey(), is("A"));
        assertThat(p2.getKey(), is("B"));
    }

    /**
     * Test of getValue method, of class CommandParameter.
     */
    @Test
    public void testGetValue() {
        CommandParameter p1 = new CommandParameter("A", 123);
        CommandParameter p2 = new CommandParameter("B", 456);
        assertThat(p1.getValue(), is((Object) 123));
        assertThat(p2.getValue(), is((Object) 456));
    }

}
