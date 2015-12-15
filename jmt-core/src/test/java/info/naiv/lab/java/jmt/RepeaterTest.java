/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class RepeaterTest {

    public RepeaterTest() {
    }

    /**
     * Test of getValue method, of class Repeater.
     */
    @Test
    public void testGetValue() {
        Repeater<Integer> instance = new Repeater<>(0, 1);
        assertThat(instance.getValue(), is(1));
    }

    /**
     * Test of iterator method, of class Repeater.
     */
    @Test
    public void testIterator() {
        Repeater<Integer> r1 = new Repeater<>(0, 1);
        assertThat(r1, is(emptyIterable()));

        Repeater<Integer> r2 = new Repeater<>(4, 1);
        assertThat(r2, is(contains(1, 1, 1, 1)));
    }

}
