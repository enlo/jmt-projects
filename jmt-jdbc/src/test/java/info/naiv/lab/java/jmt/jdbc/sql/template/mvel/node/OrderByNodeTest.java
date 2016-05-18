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
import info.naiv.lab.java.jmt.jdbc.sql.template.OrderBy;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 */
public class OrderByNodeTest {

    /**
     *
     */
    public OrderByNodeTest() {
    }

    /**
     * Test of demarcate method, of class OrderByNode.
     */
    @Test
    public void testDemarcate() {
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", "");
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users "));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_2() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", null);
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users "));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_3() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", "AAA");
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA"));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_4() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", OrderBy.asc("AAA"));
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA ASC"));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_5() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", OrderBy.desc("AAA"));
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA DESC"));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_6() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", asList(OrderBy.desc("AAA"), OrderBy.asc("BBB")));
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA DESC, BBB ASC"));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_7() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", new OrderBy[]{OrderBy.desc("AAA"), OrderBy.asc("BBB")});
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA DESC, BBB ASC"));
    }

    /**
     * Test of eval method, of class OrderByNode.
     */
    @Test
    public void testEval_8() {
        String template = "select * from Users @orderBy{ orders }";
        Map<String, Object> map = new HashMap<>();
        map.put("orders", new String[]{"AAA", "BBB desc"});
        String actualSql = makeSql(template, map);
        assertThat(actualSql, is("select * from Users  order by AAA, BBB desc"));
    }

    /**
     * Test of toString method, of class OrderByNode.
     */
    @Test
    public void testToString() {
        OrderByNode node = new OrderByNode();
        assertThat(node.toString(), is(startsWith("OrderByNode")));
    }

    /**
     *
     * @param template
     * @param map
     * @return
     */
    protected String makeSql(String template, Map<String, Object> map) {
        CompiledTemplate templ = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        SqlQueryContext ctx = new SqlQueryContext(null);
        String actualSql = (String) TemplateRuntime.execute(templ, ctx, map);
        return actualSql;
    }

}
