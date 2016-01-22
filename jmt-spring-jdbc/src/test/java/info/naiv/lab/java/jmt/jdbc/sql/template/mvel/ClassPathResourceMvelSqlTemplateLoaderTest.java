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
package info.naiv.lab.java.jmt.jdbc.sql.template.mvel;

import info.naiv.lab.java.jmt.jdbc.sql.template.AbstractClassPathResourceSqlTemplateLoaderTest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class ClassPathResourceMvelSqlTemplateLoaderTest
        extends AbstractClassPathResourceSqlTemplateLoaderTest<ClassPathResourceMvelSqlTemplateLoader> {

    public ClassPathResourceMvelSqlTemplateLoaderTest() {
    }

    @Override
    public void testFormString() {
        MvelSqlTemplate templ = instance.doFromString("select * from Test");
        assertThat(templ, is(notNullValue()));
        assertThat(new String(templ.getTemplate().getTemplate()), is("select * from Test"));
    }

    @Override
    public void testLoadCategory_String() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testLoadCategory_String_Charset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testLoad_3args() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testLoad_String_String() {
        MvelSqlTemplate templ = (MvelSqlTemplate) instance.load("C1", "test");
        assertThat(new String(templ.getTemplate().getTemplate()), is("select top(1) * from Emploee where id = @bind{id}"));
    }

    @Override
    protected ClassPathResourceMvelSqlTemplateLoader newConcrete() {
        return new ClassPathResourceMvelSqlTemplateLoader();
    }

}
