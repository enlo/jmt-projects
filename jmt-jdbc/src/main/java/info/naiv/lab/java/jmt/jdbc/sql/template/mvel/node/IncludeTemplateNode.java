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

import info.naiv.lab.java.jmt.jdbc.sql.Query;
import info.naiv.lab.java.jmt.jdbc.sql.SqlQueryContext;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.MvelSqlTemplate;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class IncludeTemplateNode extends CustomNode {

    private static final long serialVersionUID = 1L;

    @Override
    protected void onEval(Object value, TemplateRuntime runtime, TemplateOutputStream appender, SqlQueryContext ctx, VariableResolverFactory factory) {
        if (value instanceof MvelSqlTemplate) {
            MvelSqlTemplate templ = (MvelSqlTemplate) value;
            CompiledTemplate ct = templ.getTemplate();
            String compiled = (String) TemplateRuntime.execute(ct, ctx, factory);
            appender.append(compiled);
        }
        else if (value instanceof SqlTemplate) {
            SqlTemplate templ = (SqlTemplate) value;
            Query ct = templ.merge(ctx.getParameterSource());
            appender.append(ct.getMergedSql());
        }
    }

}
