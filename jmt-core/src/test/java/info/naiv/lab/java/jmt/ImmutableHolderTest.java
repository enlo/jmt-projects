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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class ImmutableHolderTest {

    public ImmutableHolderTest() {
    }

    /**
     * Test of getContent method, of class ImmutableHolder.
     */
    @Test
    public void testGetContent() {
        ImmutableHolder<String> instance = new ImmutableHolder<>("ABC");
        assertThat(instance.getContent(), is("ABC"));
    }

    /**
     * Test of getContent method, of class ImmutableHolder.
     */
    @Test
    public void testGetContent02() {
        ImmutableHolder<String> instance = new ImmutableHolder<>(null, String.class);
        assertThat(instance.getContent(), is(nullValue()));
    }
    
    /**
     * Test of getContentType method, of class ImmutableHolder.
     */
    @Test
    public void testGetContentType() {
        ImmutableHolder<String> instance = new ImmutableHolder<>("ABC");
        assertThat(instance.getContentType(), is((Object)String.class));
    }

    /**
     * Test of getContentType method, of class ImmutableHolder.
     */
    @Test(expected = NullPointerException.class)
    public void testGetContentType02() {
        ImmutableHolder<String> instance = new ImmutableHolder<>("ABC", null);
        instance.getContentType();
    }
}
