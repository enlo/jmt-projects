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
import info.naiv.lab.java.jmt.StringJoiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author enlo
 */
@Data
public class NamedParameterBinder implements ParameterBinder {

    private static final StringJoiner joiner = StringJoiner.valueOf(", ");

    private Map<String, Object> namedParameters = new HashMap<>();

    private String prefix;

    public NamedParameterBinder(String prefix) {
        this.prefix = prefix;
    }

    public List<String> addParameters(Iterable<?> items, SqlQueryContext context) {
        List<String> keys = new ArrayList<>();
        for (Object item : items) {
            String key = bind(item, context);
            keys.add(key);
        }
        return keys;
    }

    @Override
    public String bind(Object value, SqlQueryContext context) {
        String key = prefix + "param" + namedParameters.size();
        context.addParameter(value);
        namedParameters.put(key, value);
        return key;
    }

    @Override
    public String bindMany(Object value, SqlQueryContext context) {
        List<String> keys;
        if (value instanceof Iterable) {
            keys = addParameters((Iterable) value, context);
        }
        else {
            Object[] arr = asObjectArray(value);
            if (arr != null) {
                keys = addParameters(Arrays.asList(arr), context);
            }
            else {
                keys = addParameters(Arrays.asList(value), context);
            }
        }
        return joiner.join(keys).toString();
    }

}
