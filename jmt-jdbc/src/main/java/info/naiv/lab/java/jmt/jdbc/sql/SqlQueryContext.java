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

import info.naiv.lab.java.jmt.jdbc.sql.dialect.Dialect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
@Data
public class SqlQueryContext {

    @NonNull
    private ParameterBinder parameterBinder = new DefaultParameterBinder();

    Dialect dialect;

    Object parameterSource;

    List<Object> parameters;

    public SqlQueryContext(Dialect dialect) {
        this.parameters = new ArrayList<>();
        this.dialect = dialect;
    }

    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public int addParameters(Collection<?> parameters) {
        this.parameters.addAll(parameters);
        return parameters.size();
    }

    public int addParameters(Object[] parameters) {
        return addParameters(Arrays.asList(parameters));
    }

    public int addParameters(Iterable<?> parameters) {
        int count = 0;
        for (Object parameter : parameters) {
            addParameter(parameter);
            count++;
        }
        return count;
    }
}
