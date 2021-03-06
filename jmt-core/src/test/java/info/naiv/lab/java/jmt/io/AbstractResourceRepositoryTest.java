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
package info.naiv.lab.java.jmt.io;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public abstract class AbstractResourceRepositoryTest {

    /**
     *
     */
    public AbstractResourceRepositoryTest() {
    }

    /**
     *
     * @return
     */
    public abstract AbstractResourceRepository newConcrete();

    /**
     * Test of getParent method, of class AbstractResourceRepository.
     */
    @Test
    public void testGetParent() {
        AbstractResourceRepository repository = newConcrete();
        assertThat(repository.getParent(), is(nullValue()));
    }

    /**
     * Test of getResource method, of class AbstractResourceRepository.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetResource() throws Exception {
        AbstractResourceRepository repository = newConcrete();
        assertThat(repository.getResource("abc", "abc"), is(nullValue()));
    }

    /**
     * Test of getResourceLoader method, of class AbstractResourceRepository.
     */
    @Test
    public void testGetResourceLoader() {
        AbstractResourceRepository repository = newConcrete();
        assertThat(repository.getResourceLoader(), is(notNullValue()));
    }

    /**
     * Test of getResources method, of class AbstractResourceRepository.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetResources_String() throws Exception {
        AbstractResourceRepository repository = newConcrete();
        assertThat(repository.getResources("abc", "abc"), is(nullValue()));
    }

    /**
     * Test of getResources method, of class AbstractResourceRepository.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetResources_String_String() throws Exception {
    }

    /**
     * Test of setParent method, of class AbstractResourceRepository.
     */
    @Test
    public void testSetParent() {
    }

    /**
     * Test of setResourceLoader method, of class AbstractResourceRepository.
     */
    @Test
    public void testSetResourceLoader() {
    }

}
