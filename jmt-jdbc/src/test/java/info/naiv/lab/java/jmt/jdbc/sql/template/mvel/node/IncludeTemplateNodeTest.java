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
package info.naiv.lab.java.jmt.jdbc.sql.template.mvel.node;

import info.naiv.lab.java.jmt.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.jdbc.sql.dialect.StandardDialect;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.MvelSqlTemplate;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;

/**
 *
 * @author enlo
 */
public class IncludeTemplateNodeTest {

    public IncludeTemplateNodeTest() {
    }

    @Test
    public void testEval() {

        MvelSqlTemplate base = compile("base", "select * from ABC where name = @bind{name} @includeTemplate{where}");
        MvelSqlTemplate where = compile("where", "and (age between @bind{ageFr} and @bind{ageTo})");

        Map<String, Object> map = new HashMap<>();
        map.put("name", "John Doe");
        map.put("ageFr", 15);
        map.put("ageTo", 45);
        map.put("where", where);
        SqlQuery q = base.merge(map);

        String expected = "select * from ABC where name = ? and (age between ? and ?)";
        assertThat(q.getMergedSql(), is(expected));
        assertThat(q.getContext().getParameters(), contains((Object) "John Doe", 15, 45));
    }

    /**
     *
     * @param name
     * @param template
     * @return
     */
    MvelSqlTemplate compile(String name, String template) {
        CompiledTemplate ct = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        return new MvelSqlTemplate(name, ct, new StandardDialect());
    }

}
