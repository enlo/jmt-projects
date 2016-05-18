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

import info.naiv.lab.java.jmt.jdbc.sql.SqlQueryContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 */
public class BindNodeTest {

    /**
     *
     */
    public BindNodeTest() {
    }

    /**
     * Test of eval method, of class BindNode.
     */
    @Test
    public void testEval() {

        String template
                = "select * from Users where category = @bind{ category }"
                + " and (@foreach{ name : names } name = @bind{name} @end{ 'or' })";

        int category = 1;
        List<String> names = Arrays.asList("jone", "doe", "mike");

        String expectedSql
                = "select * from Users where category = ?"
                + " and ( name = ? or name = ? or name = ? )";

        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("names", names);

        CompiledTemplate templ = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        SqlQueryContext ctx = new SqlQueryContext(null);
        String actualSql = (String) TemplateRuntime.execute(templ, ctx, map);

        assertThat(actualSql, is(expectedSql));
        assertThat(ctx.getParameters(), is(contains((Object) category, names.get(0), names.get(1), names.get(2))));
    }

}
