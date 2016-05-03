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

import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.jdbc.sql.template.AbstractClassPathResourceSqlTemplateLoaderTest;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import java.nio.charset.Charset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 *
 * @author enlo
 */
public class ClassPathResourceMvelSqlTemplateLoaderTest
        extends AbstractClassPathResourceSqlTemplateLoaderTest<ClassPathResourceMvelSqlTemplateLoader> {

    public ClassPathResourceMvelSqlTemplateLoaderTest() {
    }

    @Override
    public void testFromString() {
        MvelSqlTemplate templ = (MvelSqlTemplate) instance.fromString("select * from Test");
        assertThat(templ, is(notNullValue()));
        assertThat(new String(templ.getTemplate().getTemplate()), is("select * from Test"));
    }

    @Override
    public void testLoadCategory_String() {
        Iterable<SqlTemplate> templ = instance.loadCategory("C1");
        assertThat(templ, Matchers.<SqlTemplate>iterableWithSize(3));
        assertThat(Misc.map(templ, new Function1<SqlTemplate, String>() {
            @Override
            public String apply(SqlTemplate a1) {
                return ((MvelSqlTemplate) a1).getTemplateText();
            }
        }), containsInAnyOrder("select top(1) * from Emploee where id = @bind{id}",
                               "\r\nselect * from Emploee where id = @bind{id}",
                               "\nselect * from Emp;\n\n"));
    }

    @Override
    public void testLoadCategory_String_Charset() {
        Iterable<SqlTemplate> templ = instance.loadCategory("C2", Charset.forName("MS932"));
        assertThat(templ, Matchers.<SqlTemplate>iterableWithSize(2));
        assertThat(Misc.map(templ, new Function1<SqlTemplate, String>() {
            @Override
            public String apply(SqlTemplate a1) {
                return ((MvelSqlTemplate) a1).getTemplateText();
            }
        }), containsInAnyOrder("select * from 日本語テーブル", "select * from 日本語テーブル where 名前 = @bind{name}"));
    }

    @Override
    public void testLoad_3args() {
        MvelSqlTemplate templ = (MvelSqlTemplate) instance.load("C2", "test_sjis", Charset.forName("MS932"));
        assertThat(new String(templ.getTemplate().getTemplate()), is("select * from 日本語テーブル"));
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
