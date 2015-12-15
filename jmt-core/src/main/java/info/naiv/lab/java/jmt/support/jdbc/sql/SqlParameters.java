/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.support.jdbc.sql;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import static java.util.Arrays.asList;

/**
 *
 * @author enlo
 */
public class SqlParameters implements Iterable<SqlParameter>, Cloneable {

    private boolean debug;

    private final ArrayList<SqlParameter> parameters;

    public SqlParameters(SqlParameters other) {
        this.debug = other.debug;
        this.parameters = new ArrayList<>(other.parameters);
    }

    public SqlParameters() {
        this.parameters = new ArrayList<>();
    }

    public void add(SqlParameter sqlParameter) {
        nonNull(sqlParameter, "sqlParameter");
        parameters.add(sqlParameter);
    }

    public void addAll(SqlParameter... sqlParameters) {
        nonNull(sqlParameters, "sqlParameters");
        parameters.addAll(asList(sqlParameters));
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public SqlParameters clone() {
        try {
            SqlParameters cloned;
            cloned = (SqlParameters) super.clone();
            return cloned;
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    public SqlParameters copy() {
        return new SqlParameters(this);
    }

    @ReturnNonNull
    public SqlParameter get(int index) {
        return parameters.get(index);
    }

    public List<SqlParameter> getAll(String name) {
        List<SqlParameter> result = new ArrayList<>();
        for (SqlParameter p : parameters) {
            if (Objects.equals(p.getName(), name)) {
                result.add(p);
            }
        }
        return result;
    }

    @ReturnNonNull
    public SqlParameter getFirst(String name) {
        SqlParameter p = getFirstOrNull(name);
        if (p == null) {
            throw new IllegalArgumentException(name + " is not found.");
        }
        return p;
    }

    public SqlParameter getFirstOrNull(String name) {
        for (SqlParameter p : parameters) {
            if (Objects.equals(p.getName(), name)) {
                return p;
            }
        }
        return null;
    }

    @ReturnNonNull
    public Object[] getValues() {
        Object[] result = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            SqlParameter x = parameters.get(i);
            result[i] = x.getValue();
        }
        return result;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    @ReturnNonNull
    public Iterator<SqlParameter> iterator() {
        return parameters.iterator();
    }

    @ReturnNonNull
    public SqlParameter put(SqlParameter sqlParameter) {
        nonNull(sqlParameter, "sqlParameter");
        return put(sqlParameter.getName(), sqlParameter);
    }

    @ReturnNonNull
    public SqlParameter put(String name, SqlParameter sqlParameter) {
        nonNull(name, "name");
        nonNull(sqlParameter, "sqlParameter");
        int i = indexOf(name);
        if (i < 0) {
            parameters.add(sqlParameter);
            return null;
        }
        else {
            return parameters.set(i, sqlParameter);
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @ReturnNonNull
    public PreparedStatement setTo(PreparedStatement ps) throws SQLException {
        nonNull(ps, "ps");
        ps.clearParameters();
        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).setTo(ps, i + 1);
        }
        return ps;
    }

    @ReturnNonNull
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++) {
            SqlParameter x = parameters.get(i);
            String name = x.getName();
            if (name == null) {
                name = "arg" + i;
            }
            result.put(name, x.getValue());
        }
        return result;
    }
    
    @ReturnNonNull
    public List<Object> toValueList() {
        return asList(getValues());
    }

    protected int indexOf(String name) {
        for (int i = 0; i < parameters.size(); i++) {
            if (Objects.equals(parameters.get(i).getName(), name)) {
                return i;
            }
        }
        return -1;
    }
}
