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

import info.naiv.lab.java.jmt.jdbc.sql.dialect.StandardDialect;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.node.CustomNodes;
import lombok.Builder;
import lombok.Data;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author enlo
 */
public class SqlParameterSourceVariableResolverFactoryTest {

    public SqlParameterSourceVariableResolverFactoryTest() {
    }

    /**
     * Test of createVariable method, of class
     * SqlParameterSourceVariableResolverFactory.
     */
    @Test
    public void test001() {
        MvelSqlTemplate t = build("@if{ name != empty }@{name}@end{}");
        String result = t.merge(Cond.builder().name("ABCD").build()).getMergedSql();
        assertThat(result, is("ABCD"));
    }

    /**
     * Test of createVariable method, of class
     * SqlParameterSourceVariableResolverFactory.
     */
    @Test
    public void test002() {
        MvelSqlTemplate t = build("@if{ name.startsWith('*') }"
                + "@{'%'+ name.substring(1) + '%'}"
                + "@else{}"
                + "@{ name + '%' }"
                + "@end{}");
        String result1 = t.merge(Cond.builder().name("ABCD").build()).getMergedSql();
        assertThat(result1, is("ABCD%"));
        String result2 = t.merge(Cond.builder().name("*ABCD").build()).getMergedSql();
        assertThat(result2, is("%ABCD%"));
    }

    /**
     * Test of createVariable method, of class
     * SqlParameterSourceVariableResolverFactory.
     */
    @Test
    public void test003() {
        MvelSqlTemplate t = build("@if{ name != empty }@{name}@end{}");

        SqlParameterSource params = new MapSqlParameterSource("name", null);
        String result = t.merge(params).getMergedSql();
        assertThat(result, is(""));
    }

    MvelSqlTemplate build(String templ) {
        CompiledTemplate ct = TemplateCompiler.compileTemplate(templ, CustomNodes.NODES);
        return new MvelSqlTemplate("template", ct, new StandardDialect());
    }

    @Data
    @Builder
    static class Cond {

        int age;
        String country;
        String name;
    }
}
