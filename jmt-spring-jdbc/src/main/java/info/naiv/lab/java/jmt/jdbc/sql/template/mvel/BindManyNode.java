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

import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.StringJoiner;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class BindManyNode extends Node {

    private static final StringJoiner joiner = StringJoiner.valueOf(", ");

    private static final long serialVersionUID = 1L;

    public BindManyNode() {
    }

    public BindManyNode(int begin, String name, char[] template, int start, int end) {
        super(begin, name, template, start, end);
    }

    public BindManyNode(int begin, String name, char[] template, int start, int end, Node next) {
        super(begin, name, template, start, end, next);
    }

    @Override
    public boolean demarcate(Node terminatingNode, char[] template) {
        return false;
    }

    @Override
    public Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object value = MVEL.eval(contents, ctx, factory);
        int count = 1;
        if (value != null) {
            Object[] arr = Misc.asObjectArray(value);
            if (arr != null) {
                count = arr.length;
                appendCollectionToCtx(ctx, Arrays.asList(arr));
            }
            else if (value instanceof Collection) {
                Collection list = (Collection) value;
                count = list.size();
                appendCollectionToCtx(ctx, list);
            }
            else if (value instanceof Iterable) {
                Collection list = Misc.toList((Iterable) value);
                count = list.size();
                appendCollectionToCtx(ctx, list);
            }
            else {
                appendToCtx(ctx, value);
            }
        }
        appender.append(joiner.join(Misc.repeat(count, "?")));
        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }

    protected void appendCollectionToCtx(Object ctx, Collection<Object> value) {
        if (ctx instanceof Collection) {
            ((Collection) ctx).addAll(value);
        }
    }

    protected void appendToCtx(Object ctx, Object value) {
        if (ctx instanceof Collection) {
            ((Collection) ctx).add(value);
        }
    }

    @Override
    public String toString() {
        return Misc.join("BindManyNode:", name, "{",
                         (contents == null ? "" : new String(contents)),
                         "} (start=", begin, ";end=", end + ")");
    }

}
