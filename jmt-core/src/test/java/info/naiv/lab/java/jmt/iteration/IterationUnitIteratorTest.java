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
package info.naiv.lab.java.jmt.iteration;

import info.naiv.lab.java.jmt.StandardIterationUnits;
import java.util.Iterator;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class IterationUnitIteratorTest {

    /**
     *
     */
    public IterationUnitIteratorTest() {
    }

    /**
     * Test of hasNext method, of class IterationUnitIterator.
     */
    @Test
    public void testHasNext() {
        Iterator<Integer> it = new IterationUnitIterator<>(1, 1, StandardIterationUnits.INTEGER);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(false));
    }

    /**
     * Test of next method, of class IterationUnitIterator.
     */
    @Test
    public void testNext() {
        Iterator<Integer> it = new IterationUnitIterator<>(1, 2, StandardIterationUnits.INTEGER);
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
    }

    /**
     * Test of remove method, of class IterationUnitIterator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Iterator<Integer> it = new IterationUnitIterator<>(1, 2, StandardIterationUnits.INTEGER);
        it.remove();
    }

}
