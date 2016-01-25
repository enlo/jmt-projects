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
 * @param <T>
 */
public abstract class AbstractStringJoinerTest<T> extends JoinerTest<T, StringBuilder> {

    final Joiner.Adder<T, StringBuilder> ADDER1 = new Joiner.Adder<T, StringBuilder>() {
        @Override
        public StringBuilder add(StringBuilder obj, T value, int idx) {
            return obj;
        }
    };

    final Joiner.Adder<T, StringBuilder> ADDER2 = new Joiner.Adder<T, StringBuilder>() {
        @Override
        public StringBuilder add(StringBuilder obj, T value, int idx) {
            return obj;
        }
    };

    public AbstractStringJoinerTest() {
    }

    @Test
    public void testCreateResult() {
        assertThat(new AbstractStringJoinerImpl(ADDER1).createResult(), is(not(nullValue())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor01() {
        Misc.nop(new AbstractStringJoinerImpl(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor02() {
        Misc.nop(new AbstractStringJoinerImpl(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor03() {
        Misc.nop(new AbstractStringJoinerImpl(ADDER1, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor04() {
        Misc.nop(new AbstractStringJoinerImpl(null, ADDER2));
    }

    @Test
    public void testGetFirst() {
        Joiner.Adder<T, StringBuilder> addr = new AbstractStringJoinerImpl(ADDER1, ADDER2).getFirst();
        assertThat(addr, sameInstance(ADDER1));
    }

    @Test
    public void testGetMore() {
        Joiner.Adder<T, StringBuilder> more = new AbstractStringJoinerImpl(ADDER1, ADDER2).getMore();
        assertThat(more, sameInstance(ADDER2));
    }

    private class AbstractStringJoinerImpl extends AbstractStringJoiner<T> {

        AbstractStringJoinerImpl(Adder<T, StringBuilder> first, Adder<T, StringBuilder> more) {
            super(first, more);
        }

        AbstractStringJoinerImpl(Adder<T, StringBuilder> adder) {
            super(adder);
        }
    }

}
