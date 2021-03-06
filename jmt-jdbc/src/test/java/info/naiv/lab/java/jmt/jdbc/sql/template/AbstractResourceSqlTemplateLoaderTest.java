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
package info.naiv.lab.java.jmt.jdbc.sql.template;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class AbstractResourceSqlTemplateLoaderTest<T extends AbstractResourceSqlTemplateLoader>
        extends AbstractSqlTemplateLoaderTest<T> {

    /**
     *
     */
    public AbstractResourceSqlTemplateLoaderTest() {
    }

    /**
     * Test of getExtension method, of class
     * AbstractClassPathResourceSqlTemplateLoader.
     */
    @Test
    public void testGetExtension() {
        assertThat(instance.getExtension(), is(defaultExntension()));
    }

    /**
     * Test of getResourceRepository method, of class
     * AbstractClassPathResourceSqlTemplateLoader.
     */
    @Test
    public void testGetResourceRepository() {
        assertThat(instance.getResourceRepository(), is(notNullValue()));
    }

    /**
     * Test of setExtension method, of class
     * AbstractClassPathResourceSqlTemplateLoader.
     */
    @Test
    public void testSetExtension() {
        instance.setExtension("txt");
        assertThat(instance.getExtension(), is("txt"));
    }

    /**
     *
     * @return
     */
    protected String defaultExntension() {
        return "sql";
    }
}
