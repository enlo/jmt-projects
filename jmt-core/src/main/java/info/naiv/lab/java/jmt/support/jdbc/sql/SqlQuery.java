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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author enlo
 */
public class SqlQuery {

    public static SqlQuery rebind(SqlQuery query, SqlParameter... params) {
        SqlParameters newParams = query.parameters.copy();
        for (SqlParameter param : params) {
            newParams.put(param);
        }
        return new SqlQuery(query.sql, newParams);
    }

    private final SqlParameters parameters;
    private final String sql;

    public SqlQuery(String sql, SqlParameters params) {
        nonNull(sql, "sql");
        nonNull(params, "params");
        this.sql = sql;
        this.parameters = params;
    }

    @ReturnNonNull
    public SqlParameters getParameters() {
        return parameters;
    }

    @ReturnNonNull
    public String getSql() {
        return sql;
    }

    @ReturnNonNull
    public PreparedStatement prepareStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(getSql());
        getParameters().setTo(ps);
        return ps;
    }

}
