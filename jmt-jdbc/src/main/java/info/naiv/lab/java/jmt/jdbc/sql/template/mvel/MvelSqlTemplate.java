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

import info.naiv.lab.java.jmt.jdbc.sql.Query;
import info.naiv.lab.java.jmt.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Value;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author enlo
 */
@Value
public class MvelSqlTemplate implements SqlTemplate, Serializable {

    private static final long serialVersionUID = 1L;

    String name;
    CompiledTemplate template;

    public MvelSqlTemplate(String name, CompiledTemplate template) {
        this.name = name;
        this.template = template;
    }

    public String getTemplateText() {
        return new String(template.getTemplate());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SqlQuery merge(Map<String, Object> parameters) {
        SqlParametersBuilder builder = new SqlParametersBuilder();
        String sql = (String) TemplateRuntime.execute(template, builder, parameters);
        return new SqlQuery(sql, builder);
    }

    @Override
    public SqlQuery merge(SqlParameterSource parameters) {
        SqlParametersBuilder builder = new SqlParametersBuilder();
        VariableResolverFactory factory = new SqlParameterSourceVariableResolverFactory(parameters);
        String sql = (String) TemplateRuntime.execute(template, builder, factory);
        return new SqlQuery(sql, builder);
    }

    @Override
    public SqlQuery merge(Object bean) {
        return merge(new BeanPropertySqlParameterSource(bean));
    }

    @Override
    public Query merge() {
        return merge(new HashMap<>());
    }

}