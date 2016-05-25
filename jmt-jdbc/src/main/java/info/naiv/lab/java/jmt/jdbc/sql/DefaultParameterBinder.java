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
package info.naiv.lab.java.jmt.jdbc.sql;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.asObjectArray;
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.StringJoiner;
import java.util.Collection;

/**
 *
 * @author enlo
 */
public class DefaultParameterBinder implements ParameterBinder {

    private static final StringJoiner joiner = StringJoiner.valueOf(", ");

    @Override
    public String bind(Object value, SqlQueryContext context) {
        context.addParameter(value);
        return "?";
    }

    @Override
    public String bindMany(Object value, SqlQueryContext context) {
        int count;
        if (value instanceof Collection) {
            Collection list = (Collection) value;
            count = context.addParameters(list);
        }
        else if (value instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) value;
            count = context.addParameters(iter);
        }
        else {
            Object[] arr = asObjectArray(value);
            if (arr != null) {
                count = context.addParameters(arr);
            }
            else {
                context.addParameter(value);
                count = 1;
            }
        }
        return joiner.join(Misc.repeat(count, "?")).toString();
    }

}
