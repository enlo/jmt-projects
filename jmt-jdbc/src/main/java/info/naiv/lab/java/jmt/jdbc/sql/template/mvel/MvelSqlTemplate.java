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

import info.naiv.lab.java.jmt.jdbc.sql.SqlQueryContext;
import info.naiv.lab.java.jmt.jdbc.sql.Query;
import info.naiv.lab.java.jmt.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.jdbc.sql.dialect.Dialect;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Value;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
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
    private final Dialect dialect;

    String name;
    CompiledTemplate template;

    public MvelSqlTemplate(String name, CompiledTemplate template, Dialect dialect) {
        this.name = name;
        this.template = template;
        this.dialect = dialect;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getTemplateText() {
        return new String(template.getTemplate());
    }

    @Override
    public <T> SqlQuery merge(Map<String, T> parameters) {
        return mergeMap(parameters);
    }

    @Override
    public SqlQuery merge(SqlParameterSource parameters) {
        return mergeParameterSource(parameters);
    }

    @Override
    public SqlQuery merge(Object bean) {
        VariableResolverFactory factory = createFactory(bean);
        return createQuery(factory, bean);
    }

    @Override
    public Query merge() {
        return merge(new HashMap<>());
    }

    @Override
    public Query mergeContext(SqlQueryContext context) {
        VariableResolverFactory factory = createFactory(context.getParameterSource());
        factory.createVariable("dialect", context.getDialect(), Dialect.class);
        String sql = (String) TemplateRuntime.execute(template, context, factory);
        return new SqlQuery(sql, context);
    }

    @Override
    public <T> SqlQuery mergeMap(Map<String, T> parameters) {
        VariableResolverFactory factory = new MapVariableResolverFactory(parameters);
        return createQuery(factory, parameters);
    }

    @Override
    public SqlQuery mergeParameterSource(SqlParameterSource parameters) {
        VariableResolverFactory factory = new SqlParameterSourceVariableResolverFactory(parameters);
        return createQuery(factory, parameters);
    }

    private VariableResolverFactory createFactory(Object value) {
        if (value instanceof Map) {
            return new MapVariableResolverFactory((Map) value);
        }
        else if (value instanceof SqlParameterSource) {
            return new SqlParameterSourceVariableResolverFactory((SqlParameterSource) value);
        }
        else if (value instanceof VariableResolverFactory) {
            return (VariableResolverFactory) value;
        }
        else {
            SqlParameterSource ps = new BeanPropertySqlParameterSource(value);
            return new SqlParameterSourceVariableResolverFactory(ps);
        }
    }

    protected SqlQuery createQuery(VariableResolverFactory factory, Object sourceBean) {
        factory.createVariable("dialect", dialect, Dialect.class);
        SqlQueryContext context = new SqlQueryContext(dialect);
        context.setParameterSource(sourceBean);
        String sql = (String) TemplateRuntime.execute(template, context, factory);
        return new SqlQuery(sql, context);
    }

}
