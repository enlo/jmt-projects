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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.tquery.QueryContext;
import java.io.Serializable;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public abstract class CustomNode extends Node {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    protected Serializable ce;

    /**
     *
     */
    public CustomNode() {
    }

    /**
     *
     * @param terminatingNode
     * @param template
     * @return
     */
    @Override
    public boolean demarcate(Node terminatingNode, char[] template) {
        return false;
    }

    /**
     *
     * @param runtime
     * @param appender
     * @param ctx
     * @param factory
     * @return
     */
    @Override
    public final Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object value = MVEL.executeExpression(ce, ctx, factory);
        if (ctx instanceof QueryContext) {
            onEval(value, runtime, appender, (QueryContext) ctx, factory);
        }
        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }

    /**
     *
     * @param contents
     */
    @Override
    public void setContents(char[] contents) {
        super.setContents(contents);
        ce = MVEL.compileExpression(contents);
    }

    @Override
    public String toString() {
        return arrayToString(getClass().getSimpleName(), name, "{",
                             (contents == null ? "" : new String(contents)),
                             "} (start=", begin, ";end=", end + ")");

    }

    /**
     *
     * @param value
     * @param runtime
     * @param appender
     * @param ctx
     * @param factory
     */
    protected abstract void onEval(Object value, TemplateRuntime runtime, TemplateOutputStream appender, QueryContext ctx, VariableResolverFactory factory);
}
