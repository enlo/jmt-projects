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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.isArrayOf;
import static info.naiv.lab.java.jmt.Misc.isNotEmpty;
import info.naiv.lab.java.jmt.StringJoiner;
import info.naiv.lab.java.jmt.jdbc.sql.template.OrderBy;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class OrderByNode extends Node {

    private static final long serialVersionUID = 1L;

    public OrderByNode() {
    }

    public OrderByNode(int begin, String name, char[] template, int start, int end) {
        super(begin, name, template, start, end);
    }

    public OrderByNode(int begin, String name, char[] template, int start, int end, Node next) {
        super(begin, name, template, start, end, next);
    }

    @Override
    public boolean demarcate(Node terminatingNode, char[] template) {
        return false;
    }

    @Override
    public Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object value = MVEL.eval(contents, ctx, factory);
        if (value instanceof CharSequence) {
            CharSequence seq = (CharSequence) value;
            if (seq.length() > 0) {
                appender.append(ORDER_BY_PREFIX);
                appender.append((CharSequence) value);
            }
        }
        else if (value instanceof OrderBy) {
            OrderBy ord = (OrderBy) value;
            appender.append(ORDER_BY_PREFIX);
            appendOrder(appender, ord);
        }
        else if (value instanceof Iterable) {
            Iterable<OrderBy> ordItems = (Iterable<OrderBy>) value;
            if (isNotEmpty(ordItems)) {
                TemplateOutputStreamJoiner joiner = new Joiner(appender);
                joiner.join(ordItems);
            }
        }
        else if (isArrayOf(value, String.class)) {
            String[] ordArray = (String[]) value;
            if (isNotEmpty(ordArray)) {
                appender.append(ORDER_BY_PREFIX);
                appender.append(StringJoiner.valueOf(", ").join(ordArray));
            }
        }
        else if (isArrayOf(value, OrderBy.class)) {
            OrderBy[] ordArray = (OrderBy[]) value;
            if (isNotEmpty(ordArray)) {
                TemplateOutputStreamJoiner joiner = new Joiner(appender);
                joiner.join(ordArray);
            }
        }
        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }
    protected static final String ORDER_BY_PREFIX = " order by ";

    protected void appendOrder(TemplateOutputStream appender, OrderBy ord) {
        appender.append(ord.getOrderItem()).append(" ").append(ord.getOrder().name());
    }

    @Override
    public String toString() {
        return arrayToString("OrderByNode:", name, "{",
                             (contents == null ? "" : new String(contents)),
                             "} (start=", begin, ";end=", end + ")");
    }

    private static class Joiner extends TemplateOutputStreamJoiner<OrderBy> {

        public Joiner(TemplateOutputStream output) {
            super(output);
        }

        @Override
        protected TemplateOutputStream preLoop(TemplateOutputStream r) {
            return r.append(ORDER_BY_PREFIX);
        }

        @Override
        protected TemplateOutputStream append(TemplateOutputStream output, OrderBy value) {
            return output.append(value.getOrderItem()).append(" ").append(value.getOrder().name());
        }
    }
}
