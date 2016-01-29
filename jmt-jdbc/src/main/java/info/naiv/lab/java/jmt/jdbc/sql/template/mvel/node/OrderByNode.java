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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.asObjectArray;
import info.naiv.lab.java.jmt.jdbc.sql.SqlQueryContext;
import info.naiv.lab.java.jmt.jdbc.sql.template.OrderBy;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class OrderByNode extends CustomNode {

    private static final long serialVersionUID = 1L;
    protected static final String ORDER_BY_PREFIX = " order by ";

    @Override
    public void onEval(Object value, TemplateRuntime runtime, TemplateOutputStream appender, SqlQueryContext ctx, VariableResolverFactory factory) {
        if (value instanceof Iterable) {
            Iterable<?> ordItems = (Iterable<?>) value;
            new Joiner(appender).join(ordItems);
        }
        else if (value instanceof CharSequence) {
            CharSequence seq = (CharSequence) value;
            if (seq.length() > 0) {
                appender.append(ORDER_BY_PREFIX);
                appender.append((CharSequence) value);
            }
        }
        else {
            Object[] ordItems = asObjectArray(value);
            if (ordItems != null) {
                new Joiner(appender).join(ordItems);
            }
            else if (value != null) {
                appender.append(ORDER_BY_PREFIX);
                appender.append(value.toString());
            }
        }
    }

    protected void appendOrder(TemplateOutputStream appender, OrderBy ord) {
        appender.append(ord.getOrderItem()).append(" ").append(ord.getOrder().name());
    }

    private static class Joiner extends TemplateOutputStreamJoiner<Object> {

        Joiner(TemplateOutputStream output) {
            super(output);
        }

        @Override
        protected TemplateOutputStream append(TemplateOutputStream output, Object value) {
            return output.append(value.toString());
        }

        @Override
        protected TemplateOutputStream appendFirst(TemplateOutputStream output, Object value) {
            output.append(ORDER_BY_PREFIX);
            return output.append(value.toString());
        }

    }
}
