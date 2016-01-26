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
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.StringJoiner;
import java.util.Arrays;
import java.util.Collection;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class BindManyNode extends CustomNode {

    private static final StringJoiner joiner = StringJoiner.valueOf(", ");

    private static final long serialVersionUID = 1L;

    @Override
    public void onEval(Object value, TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        int count = 1;
        if (value instanceof Collection) {
            Collection list = (Collection) value;
            count = appendCollectionToCtx(ctx, list);
        }
        else if (value instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) value;
            count = appendIterableToCtx(ctx, iter);
        }
        else {
            Object[] arr = asObjectArray(value);
            if (arr != null) {
                count = appendCollectionToCtx(ctx, Arrays.asList(arr));
            }
            else {
                count = appendToCtx(ctx, value);
            }
        }
        appender.append(joiner.join(Misc.repeat(count, "?")));
    }

    protected int appendCollectionToCtx(Object ctx, Collection<Object> value) {
        if (ctx instanceof Collection) {
            ((Collection) ctx).addAll(value);
            return value.size();
        }
        return 0;
    }

    protected int appendIterableToCtx(Object ctx, Iterable<?> value) {
        int count = 0;
        if (ctx instanceof Collection) {
            Collection col = (Collection) ctx;
            for (Object obj : value) {
                count++;
                col.add(obj);
            }
        }
        return count;
    }

    protected int appendToCtx(Object ctx, Object value) {
        if (ctx instanceof Collection) {
            ((Collection) ctx).add(value);
            return 1;
        }
        return 0;
    }

}
