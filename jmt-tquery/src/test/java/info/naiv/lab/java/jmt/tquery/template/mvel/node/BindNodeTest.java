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
package info.naiv.lab.java.jmt.tquery.template.mvel.node;

import info.naiv.lab.java.jmt.template.DefaultTemplateSourceResolver;
import info.naiv.lab.java.jmt.tquery.QueryContext;
import info.naiv.lab.java.jmt.tquery.command.CommandParameter;
import info.naiv.lab.java.jmt.tquery.command.DefaultCommandParameters;
import info.naiv.lab.java.jmt.tquery.command.NamedParameterBinder;
import info.naiv.lab.java.jmt.tquery.template.mvel.MvelQueryTemplate;
import info.naiv.lab.java.jmt.tquery.template.mvel.MvelQueryTemplateBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 */
public class BindNodeTest {

    MvelQueryTemplateBuilder builder = new MvelQueryTemplateBuilder();

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

        MvelQueryTemplate templ = (MvelQueryTemplate) builder.build("template1",
                                                                    new DefaultTemplateSourceResolver(template));
        QueryContext ctx = new QueryContext();
        String actualSql = (String) TemplateRuntime.execute(templ.getTemplateObject(), ctx, map);

        assertThat(actualSql, is(expectedSql));
        assertThat(ctx.getParameters(), contains(DefaultCommandParameters.builder(category, names.get(0), names.get(1), names.get(2)).toArray()));
    }

    /**
     * Test of eval method, of class BindNode.
     */
    @Test
    public void testEvalNamed() {

        String template
                = "select * from Users where category = @bind{ category }"
                + " and (@foreach{ name : names } name = @bind{name} @end{ 'or' })";

        int category = 1;
        List<String> names = Arrays.asList("jone", "doe", "mike");

        String expectedSql
                = "select * from Users where category = :p0"
                + " and ( name = :p1 or name = :p2 or name = :p3 )";

        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("names", names);

        CompiledTemplate templ = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        QueryContext ctx = new QueryContext();
        NamedParameterBinder binder = new NamedParameterBinder(":p");
        ctx.setParameterBinder(binder);
        String actualSql = (String) TemplateRuntime.execute(templ, ctx, map);

        assertThat(actualSql, is(expectedSql));

        Map<String, Object> named = ctx.getParameters().toMap();
        assertThat(named, hasEntry(":p0", (Object) category));
        assertThat(named, hasEntry(":p1", (Object) names.get(0)));
        assertThat(named, hasEntry(":p2", (Object) names.get(1)));
        assertThat(named, hasEntry(":p3", (Object) names.get(2)));
    }

    /**
     * Test of eval method, of class BindNode.
     */
    @Test
    public void testEval_2() {

        String template
                = "select * from Users where category = @bind{ category }"
                + " and (@foreach{ name : names } name = @bind{name, type:=java.sql.Types.VARCHAR } @end{ 'or' })";

        int category = 1;
        List<String> names = Arrays.asList("jone", "doe", "mike");

        String expectedSql
                = "select * from Users where category = ?"
                + " and ( name = ? or name = ? or name = ? )";

        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("names", names);

        CompiledTemplate templ = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        QueryContext ctx = new QueryContext();
        String actualSql = (String) TemplateRuntime.execute(templ, ctx, map);

        assertThat(actualSql, is(expectedSql));
        assertThat(ctx.getParameters(), contains(
                   DefaultCommandParameters.builder(
                           category,
                           new CommandParameter(1, names.get(0), java.sql.Types.VARCHAR),
                           new CommandParameter(2, names.get(1), java.sql.Types.VARCHAR),
                           new CommandParameter(3, names.get(2), java.sql.Types.VARCHAR)).toArray()));
    }

}
