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

import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.mvel.MvelTemplate;
import info.naiv.lab.java.jmt.template.mvel.node.SingleCompiledExpressionNode;
import info.naiv.lab.java.jmt.tquery.command.Command;
import info.naiv.lab.java.jmt.tquery.QueryContext;
import info.naiv.lab.java.jmt.tquery.template.QueryTemplate;
import java.io.Serializable;
import java.util.Objects;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class ExtractTemplateNode extends SingleCompiledExpressionNode {

    private static final long serialVersionUID = 1L;

    @Override
    public String name() {
        return "extractTemplate";
    }

    @Override
    protected void doEval(Serializable compiledExpression, TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object value = MVEL.executeExpression(compiledExpression, ctx, factory);
        QueryContext context = (QueryContext) ctx;
        final String appendable;
        if (value instanceof MvelTemplate) {
            MvelTemplate templ = (MvelTemplate) value;
            CompiledTemplate ct = templ.getTemplateObject();
            appendable = (String) TemplateRuntime.execute(ct, context, factory);
        }
        else if (value instanceof QueryTemplate) {
            QueryTemplate templ = (QueryTemplate) value;
            Command ct = templ.mergeBean(context.getSource());
            appendable = ct.getQuery();
        }
        else if (value instanceof Template) {
            Template templ = (Template) value;
            Object obj = templ.mergeBean(context.getSource());
            appendable = Objects.toString(obj, "");
        }
        else {
            appendable = Objects.toString(value, "");
        }
        appender.append(appendable);
    }

}
