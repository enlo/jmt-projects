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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class AbstractSqlTemplateLoaderTest<T extends AbstractSqlTemplateLoader> {

    protected abstract T newConcrete();

    public AbstractSqlTemplateLoaderTest() {
    }

    protected T instance;

    @Before
    public void init() {
        instance = newConcrete();
    }

    /**
     * Test of formString method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public abstract void testFormString();

    /**
     * Test of load method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public abstract void testLoad_String_String();

    /**
     * Test of load method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public abstract void testLoad_3args();

    /**
     * Test of loadCategory method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public abstract void testLoadCategory_String();

    /**
     * Test of loadCategory method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public abstract void testLoadCategory_String_Charset();

    /**
     * Test of setParent method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public void testSetParent() {
        assertThat(instance.load("123", "aaa"), is(nullValue()));

        SqlTemplateLoader parent = mock(SqlTemplateLoader.class);
        SqlTemplate templ = mock(SqlTemplate.class);
        when(parent.load("123", "aaa", UTF_8)).thenReturn(templ);

        instance.setParent(parent);
        assertThat(instance.load("123", "aaa"), is(sameInstance(templ)));
    }

    /**
     * Test of getSuffix method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public void testGetSuffix() {
        assertThat(instance.getSuffix(), is(defaultSuffix()));
    }

    protected String defaultSuffix() {
        return "";
    }

    /**
     * Test of setSuffix method, of class AbstractSqlTemplateLoader.
     */
    @Test
    public void testSetSuffix() {
        assertThat(instance.getSuffix(), is(defaultSuffix()));
        instance.setSuffix("postgres");
        assertThat(instance.getSuffix(), is("postgres"));
    }

}
